package willatendo.simplelibrary.data.tags;

import java.util.concurrent.CompletableFuture;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public abstract class SimplePoiTypeTagsProvider extends SimpleTagsProvider<PoiType> {
	public SimplePoiTypeTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.POINT_OF_INTEREST_TYPE, provider, modId, existingFileHelper);
	}
}
