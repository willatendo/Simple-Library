package willatendo.simplelibrary.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack.Factory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;

public class DataHandler {
	private final FabricDataGenerator fabricDataGenerator;
	private final FabricDataGenerator.Pack pack;

	public DataHandler(FabricDataGenerator fabricDataGenerator) {
		this.fabricDataGenerator = fabricDataGenerator;
		this.pack = fabricDataGenerator.createPack();
	}

	public String getModId() {
		return this.fabricDataGenerator.getModId();
	}

	public <T extends DataProvider> void addProvider(Factory<T> factory) {
		this.pack.addProvider(factory);
	}

	public <T extends DataProvider> void addLanguageProvider(LanguageSupplier languageSupplier, String locale) {
		this.addProvider(fabricDataOutput -> languageSupplier.accept(fabricDataOutput, this.getModId(), locale));
	}

	public <T extends DataProvider> void addBasicLanguageProvider(LanguageSupplier languageSupplier) {
		this.addLanguageProvider(languageSupplier, "en_us");
	}

	@FunctionalInterface
	public static interface LanguageSupplier {
		SimpleLanguageProvider accept(FabricDataOutput fabricDataOutput, String id, String local);
	}
}
