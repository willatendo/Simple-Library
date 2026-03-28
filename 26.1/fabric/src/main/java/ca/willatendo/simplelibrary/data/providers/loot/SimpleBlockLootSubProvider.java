package ca.willatendo.simplelibrary.data.providers.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public abstract class SimpleBlockLootSubProvider extends BlockLootSubProvider {
    public SimpleBlockLootSubProvider(HolderLookup.Provider registries, String modId) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    public void dropNone(Block block) {
        this.add(block, BlockLootSubProvider.noDrop());
    }

    public void dropSelfSlab(Block block) {
        this.add(block, this::createSlabItemTable);
    }
}
