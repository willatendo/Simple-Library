package ca.willatendo.simplelibrary.data.providers.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

public abstract class SimpleEntityLootSubProvider extends EntityLootSubProvider {
    public SimpleEntityLootSubProvider(HolderLookup.Provider registries, String modId) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }
}
