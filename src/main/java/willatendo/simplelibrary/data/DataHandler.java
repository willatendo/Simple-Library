package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack.Factory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import willatendo.simplelibrary.data.SimpleAdvancementProvider.AdvancementGenerator;
import willatendo.simplelibrary.data.util.ExistingFileHelper;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class DataHandler {
	private final FabricDataGenerator fabricDataGenerator;
	private final FabricDataGenerator.Pack pack;
	private final ExistingFileHelper existingFileHelper;

	public DataHandler(FabricDataGenerator fabricDataGenerator) {
		this.fabricDataGenerator = fabricDataGenerator;
		this.pack = fabricDataGenerator.createPack();

		List<Path> loadedPacks = new ArrayList<>();
		for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
			loadedPacks.addAll(modContainer.getRootPaths());
		}

		Set<String> loadedModIds = new HashSet<>();
		for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
			loadedModIds.add(modContainer.getMetadata().getId());
		}

		this.existingFileHelper = new ExistingFileHelper(loadedPacks, loadedModIds, fabricDataGenerator.isStrictValidationEnabled(), null, null);
	}

	public FabricDataGenerator getFabricDataGenerator() {
		return this.fabricDataGenerator;
	}

	public FabricDataGenerator.Pack getPack() {
		return this.pack;
	}

	public ExistingFileHelper getExistingFileHelper() {
		return this.existingFileHelper;
	}

	public String getModId() {
		return this.fabricDataGenerator.getModId();
	}

	private <T extends DataProvider> void addProvider(Factory<T> factory) {
		this.pack.addProvider(factory);
	}

	// Broad

	public <T extends DataProvider> void addProvider(ProviderSupplier providerSupplier) {
		this.addProvider(fabricDataOutput -> providerSupplier.accept(fabricDataOutput, this.getModId()));
	}

	public <T extends DataProvider> void addProvider(SimpleProviderSupplier simpleProviderSupplier) {
		this.addProvider(fabricDataOutput -> simpleProviderSupplier.accept(fabricDataOutput, this.getModId(), this.getExistingFileHelper()));
	}

	public <T extends DataProvider> void addProvider(WithProviderSupplier withProviderSupplier) {
		this.pack.addProvider((fabricDataOutput, provider) -> withProviderSupplier.accept(fabricDataOutput, provider, this.getModId()));
	}

	public <T extends DataProvider> void addProvider(SimpleWithProviderSupplier simpleWithProviderSupplier) {
		this.pack.addProvider((fabricDataOutput, provider) -> simpleWithProviderSupplier.accept(fabricDataOutput, provider, this.getModId(), this.getExistingFileHelper()));
	}

	// Specific

	public <T extends DataProvider> void addLanguageProvider(LanguageSupplier languageSupplier) {
		this.addProvider(fabricDataOutput -> languageSupplier.accept(fabricDataOutput, this.getModId()));
	}

	public <T extends DataProvider> void generateAdvancements(AdvancementGenerator... advancementGenerators) {
		this.pack.addProvider((fabricDataOutput, provider) -> new SimpleAdvancementProvider(fabricDataOutput, provider, SimpleUtils.toList(advancementGenerators)));
	}

	@FunctionalInterface
	public static interface ProviderSupplier {
		DataProvider accept(FabricDataOutput fabricDataOutput, String modId);
	}

	@FunctionalInterface
	public static interface SimpleProviderSupplier {
		DataProvider accept(FabricDataOutput fabricDataOutput, String modId, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface WithProviderSupplier {
		DataProvider accept(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId);
	}

	@FunctionalInterface
	public static interface SimpleWithProviderSupplier {
		DataProvider accept(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface LanguageSupplier {
		SimpleLanguageProvider accept(FabricDataOutput fabricDataOutput, String modId);
	}
}
