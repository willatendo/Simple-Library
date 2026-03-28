package ca.willatendo.simplelibrary.data.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

public abstract class SimpleKeyTagProvider<T> extends SimpleTagsProvider<T> {
    public SimpleKeyTagProvider(PackOutput output, String modId, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, modId, registryKey, lookupProvider);
    }

    protected TagAppender<ResourceKey<T>, T> tag(TagKey<T> tagKey) {
        TagBuilder tagBuilder = this.getOrCreateRawBuilder(tagKey);
        return TagAppender.forBuilder(tagBuilder);
    }

    protected TagAppender<ResourceKey<T>, T> tag(TagKey<T> tagKey, boolean replace) {
        TagBuilder tagBuilder = this.getOrCreateRawBuilder(tagKey);
        tagBuilder.setReplace(replace);
        return TagAppender.forBuilder(tagBuilder);
    }
}
