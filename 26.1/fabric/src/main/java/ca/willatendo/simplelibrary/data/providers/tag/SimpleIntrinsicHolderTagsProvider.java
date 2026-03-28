package ca.willatendo.simplelibrary.data.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class SimpleIntrinsicHolderTagsProvider<T> extends SimpleTagsProvider<T> {
    private final Function<T, ResourceKey<T>> keyExtractor;

    public SimpleIntrinsicHolderTagsProvider(PackOutput packOutput, String modId, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, Function<T, ResourceKey<T>> keyExtractor) {
        super(packOutput, modId, registryKey, lookupProvider);
        this.keyExtractor = keyExtractor;
    }

    public SimpleIntrinsicHolderTagsProvider(PackOutput packOutput, String modId, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<SimpleTagsProvider.TagLookup<T>> parentProvider, Function<T, ResourceKey<T>> keyExtractor) {
        super(packOutput, modId, registryKey, lookupProvider, parentProvider);
        this.keyExtractor = keyExtractor;
    }

    protected SimpleTagAppender<T, T> tag(TagKey<T> tag) {
        SimpleTagBuilder tagBuilder = this.getOrCreateRawBuilder(tag);
        return SimpleTagAppender.<T>forBuilder(tagBuilder).map(this.keyExtractor);
    }
}
