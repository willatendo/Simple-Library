package ca.willatendo.simplelibrary.data.providers.tag;

import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class SimpleTagsProvider<T> implements DataProvider {
    private final CompletableFuture<Void> contentsDone = new CompletableFuture<>();
    private final Map<Identifier, SimpleTagBuilder> builders = Maps.newLinkedHashMap();
    protected final PackOutput.PathProvider pathProvider;
    private final String modId;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final CompletableFuture<SimpleTagsProvider.TagLookup<T>> parentProvider;
    protected final ResourceKey<? extends Registry<T>> registryKey;

    public SimpleTagsProvider(PackOutput output, String modId, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this(output, modId, registryKey, lookupProvider, CompletableFuture.completedFuture(SimpleTagsProvider.TagLookup.empty()));
    }

    public SimpleTagsProvider(PackOutput output, String modId, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<SimpleTagsProvider.TagLookup<T>> parentProvider) {
        this.pathProvider = output.createRegistryTagsPathProvider(registryKey);
        this.modId = modId;
        this.registryKey = registryKey;
        this.parentProvider = parentProvider;
        this.lookupProvider = lookupProvider;
    }

    protected abstract void addTags(HolderLookup.Provider registries);

    @Override
    public CompletableFuture<?> run(final CachedOutput cache) {
        return this.createContentsProvider().thenApply(provider -> {
            this.contentsDone.complete(null);
            return provider;
        }).thenCombineAsync(this.parentProvider, (provider, tagLookup) -> {
            record CombinedData<T>(HolderLookup.Provider contents, SimpleTagsProvider.TagLookup<T> parent) {
            }

            return new CombinedData<>(provider, tagLookup);
        }, Util.backgroundExecutor()).thenCompose((combinedData) -> {
            HolderLookup.RegistryLookup<T> registryLookup = combinedData.contents.lookupOrThrow(this.registryKey);
            Predicate<Identifier> elementCheck = identifier -> registryLookup.get(ResourceKey.create(this.registryKey, identifier)).isPresent();
            Predicate<Identifier> tagCheck = identifier -> this.builders.containsKey(identifier) || combinedData.parent.contains(TagKey.create(this.registryKey, identifier));
            return CompletableFuture.allOf(this.builders.entrySet().stream().map(entry -> {
                Identifier identifier = entry.getKey();
                SimpleTagBuilder simpleTagBuilder = entry.getValue();
                List<SimpleTagEntry> entries = simpleTagBuilder.build();
                List<SimpleTagEntry> unresolvedEntries = entries.stream().filter(tagEntry -> tagEntry.getId().getNamespace().equals(this.modId) && !tagEntry.verifyIfPresent(elementCheck, tagCheck)).toList();
                if (!unresolvedEntries.isEmpty()) {
                    throw new IllegalArgumentException(String.format(Locale.ROOT, "Couldn't define tag %s as it is missing following references: %s", identifier, unresolvedEntries.stream().map(Objects::toString).collect(Collectors.joining(","))));
                } else {
                    Path path = this.pathProvider.json(identifier);
                    return DataProvider.saveStable(cache, combinedData.contents, SimpleTagFile.CODEC, new SimpleTagFile(entries, simpleTagBuilder.shouldReplace()), path);
                }
            }).toArray(size -> new CompletableFuture<?>[size]));
        });
    }

    @Override
    public String getName() {
        return "SimpleLibrary: Tag Provider for " + this.registryKey.identifier() + " for " + this.modId;
    }

    protected SimpleTagBuilder getOrCreateRawBuilder(TagKey<T> tagKey) {
        return this.builders.computeIfAbsent(tagKey.location(), identifier -> SimpleTagBuilder.create());
    }

    public CompletableFuture<SimpleTagsProvider.TagLookup<T>> contentsGetter() {
        return this.contentsDone.thenApply(ignore -> tagKey -> Optional.ofNullable(this.builders.get(tagKey.location())));
    }

    protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
        return this.lookupProvider.thenApply(registries -> {
            this.builders.clear();
            this.addTags(registries);
            return registries;
        });
    }

    @FunctionalInterface
    public interface TagLookup<T> extends Function<TagKey<T>, Optional<SimpleTagBuilder>> {
        static <T> SimpleTagsProvider.TagLookup<T> empty() {
            return tagKey -> Optional.empty();
        }

        default boolean contains(final TagKey<T> key) {
            return this.apply(key).isPresent();
        }
    }
}
