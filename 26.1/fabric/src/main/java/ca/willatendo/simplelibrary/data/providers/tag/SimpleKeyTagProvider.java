package ca.willatendo.simplelibrary.data.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

public abstract class SimpleKeyTagProvider<T> extends SimpleTagsProvider<T> {
    public SimpleKeyTagProvider(PackOutput output, String modId, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, modId, registryKey, lookupProvider);
    }

    protected SimpleTagAppender<ResourceKey<T>, T> tag(TagKey<T> tagKey) {
        SimpleTagBuilder simpleTagBuilder = this.getOrCreateRawBuilder(tagKey);
        return SimpleTagAppender.forBuilder(simpleTagBuilder);
    }

    protected SimpleTagAppender<ResourceKey<T>, T> tag(TagKey<T> tagKey, boolean replace) {
        SimpleTagBuilder simpleTagBuilder = this.getOrCreateRawBuilder(tagKey);
        simpleTagBuilder.setReplace(replace);
        return SimpleTagAppender.forBuilder(simpleTagBuilder);
    }
}
