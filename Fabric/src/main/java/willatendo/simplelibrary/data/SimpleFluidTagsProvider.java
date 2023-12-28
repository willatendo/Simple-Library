package willatendo.simplelibrary.data;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimpleFluidTagsProvider extends SimpleIntrinsicHolderTagsProvider<Fluid> {
	public SimpleFluidTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.FLUID, provider, (fluid) -> {
			return fluid.builtInRegistryHolder().key();
		}, modId, existingFileHelper);
	}
}
