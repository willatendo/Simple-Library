package willatendo.simplelibrary.data.tags;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimplePoiTypeTagsProvider extends SimpleTagsProvider<PoiType> {
	public SimplePoiTypeTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.POINT_OF_INTEREST_TYPE, provider, modId, existingFileHelper);
	}
}
