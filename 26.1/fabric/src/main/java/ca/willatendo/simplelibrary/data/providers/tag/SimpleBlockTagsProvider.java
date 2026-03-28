package ca.willatendo.simplelibrary.data.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public abstract class SimpleBlockTagsProvider extends SimpleIntrinsicHolderTagsProvider<Block> {
    public SimpleBlockTagsProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, modId, Registries.BLOCK, lookupProvider, block -> block.builtInRegistryHolder().key());
    }
}
