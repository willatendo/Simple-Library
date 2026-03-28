package ca.willatendo.simplelibrary.data.providers;

import ca.willatendo.simplelibrary.server.conditions.ConditionalOps;
import ca.willatendo.simplelibrary.server.conditions.ICondition;
import ca.willatendo.simplelibrary.server.conditions.WithConditions;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class SimpleDatapackBuiltinEntriesProvider implements DataProvider {
    private final PackOutput packOutput;
    private final String modId;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final Predicate<String> namespacePredicate;
    private final Map<ResourceKey<?>, List<ICondition>> conditions;
    private final CompletableFuture<HolderLookup.Provider> fullRegistries;

    public SimpleDatapackBuiltinEntriesProvider(PackOutput packOutput, String modId, CompletableFuture<RegistrySetBuilder.PatchedRegistries> registries, Map<ResourceKey<?>, List<ICondition>> conditions, Set<String> modIds) {
        this.packOutput = packOutput;
        this.modId = modId;
        this.registries = registries.thenApply(RegistrySetBuilder.PatchedRegistries::patches);
        Predicate<String> namespacePredicate;
        if (modIds == null) {
            namespacePredicate = namespace -> true;
        } else {
            Objects.requireNonNull(modIds);
            namespacePredicate = modIds::contains;
        }
        this.namespacePredicate = namespacePredicate;
        this.conditions = conditions;
        this.fullRegistries = registries.thenApply(RegistrySetBuilder.PatchedRegistries::full);
    }

    public SimpleDatapackBuiltinEntriesProvider(PackOutput packOutput, String modId, CompletableFuture<RegistrySetBuilder.PatchedRegistries> registries, Consumer<BiConsumer<ResourceKey<?>, ICondition>> conditionsBuilder, Set<String> modIds) {
        this(packOutput, modId, registries, buildConditionsMap(conditionsBuilder), modIds);
    }

    public SimpleDatapackBuiltinEntriesProvider(PackOutput packOutput, String modId, CompletableFuture<RegistrySetBuilder.PatchedRegistries> registries, Set<String> modIds) {
        this(packOutput, modId, registries, consumer -> {
        }, modIds);
    }

    public SimpleDatapackBuiltinEntriesProvider(PackOutput packOutput, String modId, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder, Set<String> modIds) {
        this(packOutput, modId, RegistryPatchGenerator.createLookup(registries, datapackEntriesBuilder), modIds);
    }

    public SimpleDatapackBuiltinEntriesProvider(PackOutput packOutput, String modId, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder, Map<ResourceKey<?>, List<ICondition>> conditions, Set<String> modIds) {
        this(packOutput, modId, RegistryPatchGenerator.createLookup(registries, datapackEntriesBuilder), conditions, modIds);
    }

    public SimpleDatapackBuiltinEntriesProvider(PackOutput packOutput, String modId, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder, Consumer<BiConsumer<ResourceKey<?>, ICondition>> conditionsBuilder, Set<String> modIds) {
        this(packOutput, modId, RegistryPatchGenerator.createLookup(registries, datapackEntriesBuilder), conditionsBuilder, modIds);
    }

    public CompletableFuture<HolderLookup.Provider> getRegistryProvider() {
        return fullRegistries;
    }

    private static <E> CompletableFuture<?> dumpValue(Path path, CachedOutput cache, DynamicOps<JsonElement> ops, Encoder<Optional<WithConditions<E>>> codec, Optional<WithConditions<E>> value) {
        return codec.encodeStart(ops, value).mapOrElse(jsonElement -> DataProvider.saveStable(cache, jsonElement, path), error -> CompletableFuture.failedFuture(new IllegalStateException("Couldn't generate file '" + path + "': " + error.message())));
    }

    private <T> Optional<CompletableFuture<?>> dumpRegistryCap(CachedOutput cachedOutput, HolderLookup.Provider registries, DynamicOps<JsonElement> writeOps, RegistryDataLoader.RegistryData<T> registryData) {
        ResourceKey<? extends Registry<T>> registryKey = registryData.key();
        Codec<Optional<WithConditions<T>>> conditionalCodec = ConditionalOps.createConditionalCodecWithConditions(registryData.elementCodec());
        return registries.lookup(registryKey).map(registryLookup -> {
            PackOutput.PathProvider pathProvider = this.packOutput.createRegistryElementsPathProvider(registryKey);
            return CompletableFuture.allOf(registryLookup.listElements().filter(reference -> this.namespacePredicate.test(reference.key().identifier().getNamespace())).map(reference -> SimpleDatapackBuiltinEntriesProvider.dumpValue(pathProvider.json(reference.key().identifier()), cachedOutput, writeOps, conditionalCodec, Optional.of(new WithConditions<>(this.conditions.getOrDefault(reference.key(), List.of()), reference.value())))).toArray(size -> new CompletableFuture<?>[size]));
        });
    }

    private static Map<ResourceKey<?>, List<ICondition>> buildConditionsMap(Consumer<BiConsumer<ResourceKey<?>, ICondition>> conditionBuilder) {
        Map<ResourceKey<?>, List<ICondition>> conditions = new IdentityHashMap<>();
        conditionBuilder.accept((key, condition) -> conditions.computeIfAbsent(key, k -> new ArrayList<>()).add(condition));
        return conditions;
    }

    public static Stream<RegistryDataLoader.RegistryData<?>> getDataPackRegistriesWithDimensions() {
        return Stream.concat(Stream.concat(DynamicRegistries.getDynamicRegistries().stream(), RegistryDataLoader.WORLDGEN_REGISTRIES.stream()), RegistryDataLoader.DIMENSION_REGISTRIES.stream());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return this.registries.thenCompose((access) -> {
            DynamicOps<JsonElement> registryOps = access.createSerializationContext(JsonOps.INSTANCE);
            return CompletableFuture.allOf(SimpleDatapackBuiltinEntriesProvider.getDataPackRegistriesWithDimensions().flatMap((v) -> this.dumpRegistryCap(cachedOutput, access, registryOps, v).stream()).toArray((x$0) -> new CompletableFuture[x$0]));
        });
    }

    @Override
    public String getName() {
        return "SimpleLibrary: Registries Provider for " + this.modId;
    }
}
