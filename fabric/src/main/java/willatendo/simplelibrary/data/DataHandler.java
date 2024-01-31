package willatendo.simplelibrary.data;

import java.util.concurrent.CompletableFuture;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack.Factory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataProvider;
import net.minecraft.world.level.block.Block;
import willatendo.simplelibrary.data.SimpleAdvancementProvider.AdvancementGenerator;
import willatendo.simplelibrary.data.tags.SimpleBlockTagsProvider;
import willatendo.simplelibrary.data.tags.SimpleItemTagsProvider;
import willatendo.simplelibrary.data.tags.SimpleTagsProvider;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class DataHandler {
	private final FabricDataGenerator fabricDataGenerator;
	private final FabricDataGenerator.Pack pack;
	private final ExistingFileHelper existingFileHelper;

	public DataHandler(FabricDataGenerator fabricDataGenerator) {
		this.fabricDataGenerator = fabricDataGenerator;
		this.pack = fabricDataGenerator.createPack();
		this.existingFileHelper = ExistingFileHelper.withResourcesFromArg();
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

	// Broad

	public <T extends DataProvider> T addProvider(Factory<T> factory) {
		T provider = this.pack.addProvider(factory);
		return provider;
	}

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

	public <T extends SimpleLanguageProvider> T addLanguageProvider(String local, LanguageSupplier<T> languageSupplier) {
		return this.addProvider(fabricDataOutput -> languageSupplier.accept(fabricDataOutput, local));
	}

	public <T extends DataProvider> void addTagsProvider(ItemTagSupplier itemTagSupplier, BlockTagSupplier blockTagSupplier) {
		SimpleBlockTagsProvider simpleBlockTagsProvider = this.addProvider((fabricDataOutput, provider, modId, existingFileHelper) -> blockTagSupplier.accept(fabricDataOutput, provider, modId, existingFileHelper));
		this.addProvider((fabricDataOutput, provider, modId, existingFileHelper) -> itemTagSupplier.accept(fabricDataOutput, provider, simpleBlockTagsProvider.contentsGetter(), modId, existingFileHelper));
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
		T accept(FabricDataOutput fabricDataOutput, String local);
	}
}
