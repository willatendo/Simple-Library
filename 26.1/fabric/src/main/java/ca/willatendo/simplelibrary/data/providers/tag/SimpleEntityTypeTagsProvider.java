package ca.willatendo.simplelibrary.data.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public abstract class SimpleEntityTypeTagsProvider extends SimpleIntrinsicHolderTagsProvider<EntityType<?>> {
    public SimpleEntityTypeTagsProvider(PackOutput packOutput, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, modId, Registries.ENTITY_TYPE, lookupProvider, entityType -> entityType.builtInRegistryHolder().key());
    }
}
