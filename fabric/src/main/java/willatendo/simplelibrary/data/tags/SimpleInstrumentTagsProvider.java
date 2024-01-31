package willatendo.simplelibrary.data.tags;

import java.util.concurrent.CompletableFuture;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Instrument;

public abstract class SimpleInstrumentTagsProvider extends SimpleTagsProvider<Instrument> {
	public SimpleInstrumentTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.INSTRUMENT, provider, modId, existingFileHelper);
	}
}
