package willatendo.simplelibrary.data.tags;

import java.util.concurrent.CompletableFuture;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

public abstract class SimpleFluidTagsProvider extends SimpleIntrinsicHolderTagsProvider<Fluid> {
	public SimpleFluidTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.FLUID, provider, (fluid) -> {
			return fluid.builtInRegistryHolder().key();
		}, modId, existingFileHelper);
	}
}
