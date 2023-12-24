package willatendo.simplelibrary.data;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.CatVariant;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimpleCatVarientTagsProvider extends SimpleTagsProvider<CatVariant> {
	public SimpleCatVarientTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.CAT_VARIANT, provider, modId, existingFileHelper);
	}
}
