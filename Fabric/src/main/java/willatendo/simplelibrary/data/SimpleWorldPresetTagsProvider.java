package willatendo.simplelibrary.data;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimpleWorldPresetTagsProvider extends SimpleTagsProvider<WorldPreset> {
	public SimpleWorldPresetTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.WORLD_PRESET, provider, modId, existingFileHelper);
	}
}
