package ca.willatendo.simplelibrary.data.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.material.Fluid;

import java.util.concurrent.CompletableFuture;

public abstract class SimpleFluidTagsProvider extends SimpleIntrinsicHolderTagsProvider<Fluid> {
    public SimpleFluidTagsProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, modId, Registries.FLUID, lookupProvider, (e) -> e.builtInRegistryHolder().key());
    }
}
