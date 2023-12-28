package willatendo.simplelibrary.data;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.decoration.PaintingVariant;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimplePaintingVariantTagsProvider extends SimpleTagsProvider<PaintingVariant> {
	public SimplePaintingVariantTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.PAINTING_VARIANT, provider, modId, existingFileHelper);
	}
}
