package willatendo.simplelibrary.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import willatendo.simplelibrary.data.impl.InternalBlockTagsProvider;
import willatendo.simplelibrary.data.impl.InternalItemTagsProvider;

public class SimpleLibraryData implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		DataHandler dataHandler = new DataHandler(fabricDataGenerator);
		dataHandler.addTagsProvider(InternalItemTagsProvider::new, InternalBlockTagsProvider::new);
	}
}
