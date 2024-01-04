package willatendo.simplelibrary.data;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimpleBlockTagsProvider extends SimpleIntrinsicHolderTagsProvider<Block> {
	public SimpleBlockTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.BLOCK, provider, (block) -> {
			return block.builtInRegistryHolder().key();
		}, modId, existingFileHelper);
	}
}
