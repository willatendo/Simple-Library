package willatendo.simplelibrary.data.tags;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import willatendo.simplelibrary.data.tagHelper.TagBuilder;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimpleIntrinsicHolderTagsProvider<T> extends SimpleTagsProvider<T> {
	private final Function<T, ResourceKey<T>> keyExtractor;

	public SimpleIntrinsicHolderTagsProvider(FabricDataOutput fabricDataOutput, ResourceKey<? extends Registry<T>> registry, CompletableFuture<HolderLookup.Provider> provider, Function<T, ResourceKey<T>> keyExtractor, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, registry, provider, modId, existingFileHelper);
		this.keyExtractor = keyExtractor;
	}

	public SimpleIntrinsicHolderTagsProvider(FabricDataOutput fabricDataOutput, ResourceKey<? extends Registry<T>> registry, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<T>> tagLookup, Function<T, ResourceKey<T>> keyExtractor, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, registry, provider, tagLookup, modId, existingFileHelper);
		this.keyExtractor = keyExtractor;
	}

	protected SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> tag(TagKey<T> p_255730_) {
		TagBuilder tagbuilder = this.getOrCreateRawBuilder(p_255730_);
		return new SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<>(tagbuilder, this.keyExtractor, this.modId);
	}

	public static class IntrinsicTagAppender<T> extends SimpleTagsProvider.TagAppender<T> {
		private final Function<T, ResourceKey<T>> keyExtractor;

		private IntrinsicTagAppender(TagBuilder tagBuilder, Function<T, ResourceKey<T>> keyExtractor, String modId) {
			super(tagBuilder, modId);
			this.keyExtractor = keyExtractor;
		}

		public SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> addTag(TagKey<T> tagKey) {
			super.addTag(tagKey);
			return this;
		}

		public final SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> add(T object) {
			this.add(this.keyExtractor.apply(object));
			return this;
		}

		public final SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> add(T... objects) {
			Stream.<T>of(objects).map(this.keyExtractor).forEach(this::add);
			return this;
		}

		public final ResourceKey<T> getKey(T value) {
			return this.keyExtractor.apply(value);
		}

		public SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> remove(T entry) {
			return this.remove(this.getKey(entry));
		}

		public SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> remove(T first, T... entries) {
			this.remove(first);
			for (T entry : entries) {
				this.remove(entry);
			}
			return this;
		}

		public SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> addTags(TagKey<T>... tagKeys) {
			super.addTags(tagKeys);
			return this;
		}

		@Override
		public SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> replace() {
			super.replace();
			return this;
		}

		@Override
		public SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> replace(boolean value) {
			super.replace(value);
			return this;
		}

		@Override
		public SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> remove(final ResourceLocation location) {
			super.remove(location);
			return this;
		}

		@Override
		public SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> remove(final ResourceLocation first, final ResourceLocation... locations) {
			super.remove(first, locations);
			return this;
		}

		@Override
		public SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> remove(final ResourceKey<T> resourceKey) {
			super.remove(resourceKey);
			return this;
		}

		@Override
		public SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> remove(final ResourceKey<T> firstResourceKey, final ResourceKey<T>... resourceKeys) {
			super.remove(firstResourceKey, resourceKeys);
			return this;
		}

		@Override
		public SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> remove(TagKey<T> tag) {
			super.remove(tag);
			return this;
		}

		@Override
		public SimpleIntrinsicHolderTagsProvider.IntrinsicTagAppender<T> remove(TagKey<T> first, TagKey<T>... tags) {
			super.remove(first, tags);
			return this;
		}
	}
}
