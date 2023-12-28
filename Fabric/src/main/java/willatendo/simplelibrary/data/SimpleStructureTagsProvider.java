package willatendo.simplelibrary.data;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimpleStructureTagsProvider extends SimpleTagsProvider<Structure> {
	public SimpleStructureTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.STRUCTURE, provider, modId, existingFileHelper);
	}
}
