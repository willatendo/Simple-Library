package willatendo.simplelibrary.data.tags;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import willatendo.simplelibrary.data.tagHelper.TagBuilder;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimpleItemTagsProvider extends SimpleIntrinsicHolderTagsProvider<Item> {
	private final CompletableFuture<SimpleTagsProvider.TagLookup<Block>> blockTags;
	private final Map<TagKey<Block>, TagKey<Item>> tagsToCopy = new HashMap<>();

	public SimpleItemTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<SimpleTagsProvider.TagLookup<Block>> blockTags, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.ITEM, provider, (item) -> {
			return item.builtInRegistryHolder().key();
		}, modId, existingFileHelper);
		this.blockTags = blockTags;
	}

	protected void copy(TagKey<Block> blockTagKey, TagKey<Item> itemTagKey) {
		this.tagsToCopy.put(blockTagKey, itemTagKey);
	}

	@Override
	protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
		return super.createContentsProvider().thenCombineAsync(this.blockTags, (provider, tagLookup) -> {
			this.tagsToCopy.forEach((blockTagProvider, itemTagProvider) -> {
				TagBuilder tagbuilder = this.getOrCreateRawBuilder(itemTagProvider);
				Optional<TagBuilder> optional = tagLookup.apply(blockTagProvider);
				optional.orElseThrow(() -> {
					return new IllegalStateException("Missing block tag " + itemTagProvider.location());
				}).build().forEach(tagbuilder::add);
			});
			return provider;
		});
	}
}
