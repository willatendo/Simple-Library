package ca.willatendo.simplelibrary.server.data_maps;

import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import ca.willatendo.simplelibrary.server.conditions.ConditionalOps;
import ca.willatendo.simplelibrary.server.conditions.ICondition;
import ca.willatendo.simplelibrary.server.event.DataMapEvents;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.Reader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public final class DataMapLoader implements PreparableReloadListener {
    public static final String PATH = "data_maps";
    private Map<ResourceKey<? extends Registry<?>>, LoadResult<?>> results;
    private final ICondition.IContext conditionContext;
    private final RegistryAccess registryAccess;

    public DataMapLoader(ICondition.IContext conditionContext, RegistryAccess registryAccess) {
        this.conditionContext = conditionContext;
        this.registryAccess = registryAccess;
    }

    @Override
    public CompletableFuture<Void> reload(SharedState sharedState, Executor exectutor, PreparationBarrier barrier, Executor applyExectutor) {
        return this.load(sharedState.resourceManager(), exectutor, Profiler.get()).thenCompose(barrier::wait).thenAcceptAsync(values -> this.results = values, applyExectutor);
    }

    public void apply() {
        this.results.forEach((key, result) -> this.apply((Registry) this.registryAccess.lookupOrThrow(key), result));

        this.results = null;
    }

    private <T> void apply(Registry<T> registry, LoadResult<T> result) {
        registry.clearData();
        result.results().forEach((key, entries) -> registry.putData(key, this.buildDataMap(registry, key, (List) entries)));
        DataMapEvents.UPDATE_DATA_MAPS.invoker().update(this.registryAccess, registry, DataMapEvents.UpdateCause.SERVER_RELOAD);
    }

    private <T, R> Map<ResourceKey<R>, T> buildDataMap(Registry<R> registry, DataMapType<R, T> attachment, List<DataMapFile<T, R>> entries) {
        record WithSource<T, R>(T attachment, Either<TagKey<R>, ResourceKey<R>> source) {
        }
        final Map<ResourceKey<R>, WithSource<T, R>> result = new IdentityHashMap<>();
        final DataMapValueMerger<R, T> merger = attachment instanceof AdvancedDataMapType<R, T, ?> adv ? adv.merger() : DataMapValueMerger.defaultMerger();
        entries.forEach(entry -> {
            if (entry.replace()) {
                result.clear();
            }

            entry.values().forEach((tKey, value) -> {
                if (value.isEmpty()) return;

                this.resolve(registry, tKey, true, holder -> {
                    final var newValue = value.get().carrier();
                    final var key = holder.getKey();
                    final var oldValue = result.get(key);
                    if (oldValue == null || newValue.replace()) {
                        result.put(key, new WithSource<>(newValue.value(), tKey));
                    } else {
                        result.put(key, new WithSource<>(merger.merge(registry, oldValue.source(), oldValue.attachment(), tKey, newValue.value()), tKey));
                    }
                });
            });

            for (var removal : entry.removals()) {
                if (removal.remover().isPresent()) {
                    var remover = removal.remover().orElseThrow();
                    this.resolve(registry, removal.key(), false, holder -> {
                        ResourceKey key = holder.getKey();
                        var oldValue = result.get(key);
                        if (oldValue != null) {
                            Optional<T> newValue = remover.remove(oldValue.attachment(), registry, oldValue.source(), holder.value());
                            if (newValue.isEmpty()) {
                                result.remove(key);
                            } else {
                                result.put(key, new WithSource<>(newValue.get(), oldValue.source()));
                            }
                        }
                    });
                } else {
                    this.resolve(registry, removal.key(), false, holder -> result.remove(holder.getKey()));
                }
            }
        });
        Map<ResourceKey<R>, T> newMap = new IdentityHashMap<>();
        result.forEach((key, withSource) -> newMap.put(key, withSource.attachment()));
        return newMap;
    }


    private <R> void resolve(Registry<R> registry, Either<TagKey<R>, ResourceKey<R>> value, boolean required, Consumer<Holder<R>> consumer) {
        if (value.left().isPresent()) {
            registry.getTagOrEmpty(value.left().orElseThrow()).forEach(consumer);
        } else {
            var object = registry.get(value.right().orElseThrow());
            if (object.isPresent()) {
                consumer.accept(object.get());
            } else if (required) {
                SimpleCoreUtils.LOGGER.error("Object with ID {} specified in data map for registry {} doesn't exist", value.right().orElseThrow().identifier(), registry.key().identifier());
            }
        }
    }

    private CompletableFuture<Map<ResourceKey<? extends Registry<?>>, LoadResult<?>>> load(ResourceManager resourceManager, Executor executor, ProfilerFiller profiler) {
        return CompletableFuture.supplyAsync(() -> load(resourceManager, profiler, registryAccess, conditionContext), executor);
    }

    private static Map<ResourceKey<? extends Registry<?>>, LoadResult<?>> load(ResourceManager manager, ProfilerFiller profiler, RegistryAccess access, ICondition.IContext context) {
        RegistryOps<JsonElement> ops = new ConditionalOps<>(RegistryOps.create(JsonOps.INSTANCE, access), context);

        Map<ResourceKey<? extends Registry<?>>, LoadResult<?>> values = new HashMap<>();
        access.registries().forEach(registryEntry -> {
            ResourceKey<? extends Registry<?>> registryKey = registryEntry.key();
            profiler.push("registry_data_maps/" + registryKey.identifier() + "/locating");
            FileToIdConverter fileToId = FileToIdConverter.json(PATH + "/" + getFolderLocation(registryKey.identifier()));
            for (Map.Entry<Identifier, List<Resource>> entry : fileToId.listMatchingResourceStacks(manager).entrySet()) {
                Identifier key = entry.getKey();
                Identifier attachmentId = fileToId.fileToId(key);
                DataMapType<?, ?> attachment = DataMapRegister.getDataMap((ResourceKey) registryKey, attachmentId);
                if (attachment == null) {
                    SimpleCoreUtils.LOGGER.warn("Found data map file for non-existent data map type '{}' on registry '{}'.", attachmentId, registryKey.identifier());
                    continue;
                }
                profiler.popPush("registry_data_maps/" + registryKey.identifier() + "/" + attachmentId + "/loading");
                values.computeIfAbsent(registryKey, k -> new LoadResult<>(new HashMap<>())).results.put((DataMapType) attachment, readData(ops, attachment, (ResourceKey) registryKey, entry.getValue()));
            }
            profiler.pop();
        });

        return values;
    }

    public static String getFolderLocation(Identifier registryId) {
        return (registryId.getNamespace().equals(Identifier.DEFAULT_NAMESPACE) ? "" : registryId.getNamespace() + "/") + registryId.getPath();
    }

    private static <A, T> List<DataMapFile<A, T>> readData(RegistryOps<JsonElement> ops, DataMapType<T, A> attachmentType, ResourceKey<Registry<T>> registryKey, List<Resource> resources) {
        Codec<DataMapFile<A, T>> codec = DataMapFile.codec(registryKey, attachmentType);
        List<DataMapFile<A, T>> entries = new LinkedList<>();
        for (Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {
                JsonElement jsonelement = JsonParser.parseReader(reader);
                entries.add(codec.decode(ops, jsonelement).getOrThrow().getFirst());
            } catch (Exception exception) {
                SimpleCoreUtils.LOGGER.error("Could not read data map of type {} for registry {}", attachmentType.id(), registryKey, exception);
            }
        }
        return entries;
    }

    private record LoadResult<T>(Map<DataMapType<T, ?>, List<DataMapFile<?, T>>> results) {
    }
}
