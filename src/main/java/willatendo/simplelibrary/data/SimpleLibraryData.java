package willatendo.simplelibrary.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.RegistrySetBuilder.RegistryBootstrap;
import net.minecraft.core.registries.Registries;
import willatendo.simplelibrary.data.impl.InternalBiomeTagsProvider;
import willatendo.simplelibrary.data.impl.InternalBlockTagsProvider;
import willatendo.simplelibrary.data.impl.InternalItemTagsProvider;
import willatendo.simplelibrary.server.entity.damage.SimpleDamageTypes;

public class SimpleLibraryData implements DataGeneratorEntrypoint {
	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, (RegistryBootstrap) SimpleDamageTypes::bootstrap);

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		DataHandler dataHandler = new DataHandler(fabricDataGenerator);
		dataHandler.addTagsProvider(InternalItemTagsProvider::new, InternalBlockTagsProvider::new);
		dataHandler.addProvider(InternalBiomeTagsProvider::new);
		dataHandler.addProvider((fabricDataOutput, provider, modId, existingFileHelper) -> new DatapackEntriesProvider(fabricDataOutput, provider, BUILDER));
	}
}
