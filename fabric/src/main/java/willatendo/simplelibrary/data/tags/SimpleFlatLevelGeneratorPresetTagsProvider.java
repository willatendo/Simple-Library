package willatendo.simplelibrary.data.tags;

import java.util.concurrent.CompletableFuture;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;

public abstract class SimpleFlatLevelGeneratorPresetTagsProvider extends SimpleTagsProvider<FlatLevelGeneratorPreset> {
	public SimpleFlatLevelGeneratorPresetTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.FLAT_LEVEL_GENERATOR_PRESET, provider, modId, existingFileHelper);
	}
}
