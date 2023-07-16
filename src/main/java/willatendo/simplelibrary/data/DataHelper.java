package willatendo.simplelibrary.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.data.event.GatherDataEvent;

/*
 * Used to automatically setup data collection and generation.
 * Use with provided Providers
 * 
 * @author Willatendo
 */

public class DataHelper {
	/*
	 * Use in a events class with {@Link Mod.EventBusSubscriber.Bus.MOD} and in a method with {@Link GatherDataEvent}
	 * 
	 * Null out any entries that you aren't using. You can add as many genetic ones as needed
	 */
	public static void collectAllData(String id, GatherDataEvent event, SoundDefinitionsSupplier soundDefinitionsSupplier, LanguageSupplier languageSupplier, BlockStateSupplier blockStateSupplier, ItemModelSupplier itemModelSupplier, BlockTagsSupplier blockTagsSupplier, ItemTagsSupplier itemTagsSupplier, FluidTagsSupplier fluidTagsSupplier, BiomeTagsSupplier biomeTagsSupplier, RecipeSupplier recipeSupplier, LootSupplier lootSupplier, AdvancementSupplier advancementSupplier, GenericSupplier... genericSuppliers) {
		DataGenerator dataGenerator = event.getGenerator();
		PackOutput packOutput = dataGenerator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
		boolean doClient = event.includeClient();
		boolean doServer = event.includeServer();

		// Client

		if (soundDefinitionsSupplier != null) {
			dataGenerator.addProvider(doClient, soundDefinitionsSupplier.accept(packOutput, id, existingFileHelper));
		}
		if (languageSupplier != null) {
			dataGenerator.addProvider(doClient, languageSupplier.accept(packOutput, id, "en_us"));
		}
		if (blockStateSupplier != null) {
			dataGenerator.addProvider(doClient, blockStateSupplier.accept(packOutput, id, existingFileHelper));
		}
		if (itemModelSupplier != null) {
			dataGenerator.addProvider(doClient, itemModelSupplier.accept(packOutput, id, existingFileHelper));
		}

		// Server

		if (itemModelSupplier != null && blockTagsSupplier != null) {
			BlockTagsProvider extra = blockTagsSupplier.accept(packOutput, provider, id, existingFileHelper);
			dataGenerator.addProvider(doServer, extra);
			dataGenerator.addProvider(doServer, itemTagsSupplier.accept(packOutput, provider, extra.contentsGetter(), id, existingFileHelper));
		}
		if (fluidTagsSupplier != null) {
			dataGenerator.addProvider(doServer, fluidTagsSupplier.accept(packOutput, provider, id, existingFileHelper));
		}
		if (biomeTagsSupplier != null) {
			dataGenerator.addProvider(doServer, biomeTagsSupplier.accept(packOutput, provider, id, existingFileHelper));
		}
		if (recipeSupplier != null) {
			dataGenerator.addProvider(doServer, recipeSupplier.accept(packOutput));
		}
		if (lootSupplier != null) {
			dataGenerator.addProvider(doServer, lootSupplier.accept(packOutput));
		}
		if (advancementSupplier != null) {
			dataGenerator.addProvider(doServer, advancementSupplier.accept(packOutput, provider, id, existingFileHelper));
		}
		if (genericSuppliers != null) {
			for (GenericSupplier genericSupplier : genericSuppliers) {
				dataGenerator.addProvider(doServer, genericSupplier.accept(packOutput, id));
			}
		}
	}

	public static void addTranslation(String id, GatherDataEvent event, String local, List<String> translations) {
		DataGenerator dataGenerator = event.getGenerator();
		PackOutput packOutput = dataGenerator.getPackOutput();
		dataGenerator.addProvider(event.includeClient(), new SimpleTranslationProvider(packOutput, id, local, translations));
	}

	// Client

	@FunctionalInterface
	public static interface SoundDefinitionsSupplier {
		SoundDefinitionsProvider accept(PackOutput packOutput, String id, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface LanguageSupplier {
		LanguageProvider accept(PackOutput packOutput, String id, String local);
	}

	@FunctionalInterface
	public static interface BlockStateSupplier {
		BlockStateProvider accept(PackOutput packOutput, String id, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface ItemModelSupplier {
		ItemModelProvider accept(PackOutput packOutput, String id, ExistingFileHelper existingFileHelper);
	}

	// Server

	@FunctionalInterface
	public static interface BlockTagsSupplier {
		BlockTagsProvider accept(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, String id, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface ItemTagsSupplier {
		ItemTagsProvider accept(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, String id, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface FluidTagsSupplier {
		FluidTagsProvider accept(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, String id, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface BiomeTagsSupplier {
		BiomeTagsProvider accept(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, String id, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface RecipeSupplier {
		RecipeProvider accept(PackOutput packOutput);
	}

	@FunctionalInterface
	public static interface LootSupplier {
		LootTableProvider accept(PackOutput packOutput);
	}

	@FunctionalInterface
	public static interface AdvancementSupplier {
		SimpleAdvancementProvider accept(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, String id, ExistingFileHelper existingFileHelper);
	}

	@FunctionalInterface
	public static interface GenericSupplier {
		DataProvider accept(PackOutput packOutput, String id);
	}
}
