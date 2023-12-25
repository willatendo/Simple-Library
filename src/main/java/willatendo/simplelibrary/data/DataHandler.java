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
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataProvider;
import net.minecraft.world.level.block.Block;
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

	private <T extends DataProvider> T addProvider(Factory<T> factory) {
		T provider = this.pack.addProvider(factory);
		return provider;
	}

	// Broad

	public <T extends DataProvider> T addProvider(ProviderSupplier<T> providerSupplier) {
		return this.addProvider(fabricDataOutput -> providerSupplier.accept(fabricDataOutput, this.getModId()));
	}

	public <T extends DataProvider> T addProvider(SimpleProviderSupplier<T> simpleProviderSupplier) {
		return this.addProvider(fabricDataOutput -> simpleProviderSupplier.accept(fabricDataOutput, this.getModId(), this.getExistingFileHelper()));
	}

	public <T extends DataProvider> T addProvider(WithProviderSupplier<T> withProviderSupplier) {
		return this.pack.addProvider((fabricDataOutput, provider) -> withProviderSupplier.accept(fabricDataOutput, provider, this.getModId()));
	}

	public <T extends DataProvider> T addProvider(SimpleWithProviderSupplier<T> simpleWithProviderSupplier) {
		return this.pack.addProvider((fabricDataOutput, provider) -> simpleWithProviderSupplier.accept(fabricDataOutput, provider, this.getModId(), this.getExistingFileHelper()));
	}

	// Specific

	public <T extends DataProvider> void addTagsProvider(ItemTagSupplier itemTagSupplier, BlockTagSupplier blockTagSupplier) {
		SimpleBlockTagsProvider simpleBlockTagsProvider = this.addProvider((fabricDataOutput, provider, modId, existingFileHelper) -> blockTagSupplier.accept(fabricDataOutput, provider, modId, existingFileHelper));
		this.addProvider((fabricDataOutput, provider, modId, existingFileHelper) -> itemTagSupplier.accept(fabricDataOutput, provider, simpleBlockTagsProvider.contentsGetter(), modId, existingFileHelper));
	}

	public <T extends DataProvider> void addLanguageProvider(LanguageSupplier languageSupplier) {
		this.addProvider(fabricDataOutput -> languageSupplier.accept(fabricDataOutput, this.getModId()));
	}

	public <T extends DataProvider> void generateAdvancements(AdvancementGenerator... advancementGenerators) {
		this.pack.addProvider((fabricDataOutput, provider) -> new SimpleAdvancementProvider(fabricDataOutput, provider, SimpleUtils.toList(advancementGenerators)));
	}

	@FunctionalInterface
	public static interface ProviderSupplier<T extends DataProvider> {
		T accept(FabricDataOutput fabricDataOutput, String modId);
	}

	@FunctionalInterface
	public static interface SimpleProviderSupplier<T extends DataProvider> {
		T accept(FabricDataOutput fabricDataOutput, String modId, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface WithProviderSupplier<T extends DataProvider> {
		T accept(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId);
	}

	@FunctionalInterface
	public static interface SimpleWithProviderSupplier<T extends DataProvider> {
		T accept(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface ItemTagSupplier<T extends SimpleItemTagsProvider> {
		T accept(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<SimpleTagsProvider.TagLookup<Block>> blockTags, String modId, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface BlockTagSupplier<T extends SimpleBlockTagsProvider> {
		T accept(FabricDataOutput fabricDataOutput, CompletableFuture<Provider> provider, String modId, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface LanguageSupplier<T extends SimpleLanguageProvider> {
		T accept(FabricDataOutput fabricDataOutput, String modId);
	}
}
