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
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.common.data.ForgeAdvancementProvider.AdvancementGenerator;
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
	 */
	public static DataHelperBuilder collectAllData(String id, GatherDataEvent event, LanguageSupplier languageSupplier) {
		DataGenerator dataGenerator = event.getGenerator();
		PackOutput packOutput = dataGenerator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

		// Client
		SimpleLanguageProvider enUS = (SimpleLanguageProvider) languageSupplier.accept(packOutput, id, "en_us");
		dataGenerator.addProvider(event.includeClient(), enUS);

		return new DataHelperBuilder(id, event, dataGenerator, packOutput, existingFileHelper, provider);
	}

	public static final class DataHelperBuilder {
		private final String id;
		private final DataGenerator dataGenerator;
		private final PackOutput packOutput;
		private final ExistingFileHelper existingFileHelper;
		private final CompletableFuture<HolderLookup.Provider> provider;
		private final boolean doClient;
		private final boolean doServer;

		private DataHelperBuilder(String id, GatherDataEvent event, DataGenerator dataGenerator, PackOutput packOutput, ExistingFileHelper existingFileHelper, CompletableFuture<HolderLookup.Provider> provider) {
			this.id = id;
			this.dataGenerator = dataGenerator;
			this.packOutput = packOutput;
			this.existingFileHelper = existingFileHelper;
			this.provider = provider;
			this.doClient = event.includeClient();
			this.doServer = event.includeServer();
		}

		public DataHelperBuilder addBlockStateProvider(BlockStateSupplier blockStateSupplier) {
			this.dataGenerator.addProvider(this.doClient, blockStateSupplier.accept(this.packOutput, this.id, this.existingFileHelper));
			return this;
		}

		public DataHelperBuilder addItemModelProvider(ItemModelSupplier itemModelSupplier) {
			this.dataGenerator.addProvider(this.doClient, itemModelSupplier.accept(this.packOutput, this.id, this.existingFileHelper));
			return this;
		}

		public DataHelperBuilder addSoundDefinitionProvider(SoundDefinitionsSupplier soundDefinitionsSupplier) {
			this.dataGenerator.addProvider(this.doClient, soundDefinitionsSupplier.accept(this.packOutput, this.id, this.existingFileHelper));
			return this;
		}

		public DataHelperBuilder addBasicTagProviders(ItemTagsSupplier itemTagsSupplier, BlockTagsSupplier blockTagsSupplier) {
			BlockTagsProvider blockTagProvider = blockTagsSupplier.accept(this.packOutput, this.provider, this.id, this.existingFileHelper);
			this.dataGenerator.addProvider(this.doServer, blockTagProvider);
			this.dataGenerator.addProvider(this.doServer, itemTagsSupplier.accept(this.packOutput, this.provider, blockTagProvider.contentsGetter(), this.id, this.existingFileHelper));
			return this;
		}

		public DataHelperBuilder addFluidTagProvider(FluidTagsSupplier fluidTagsSupplier) {
			this.dataGenerator.addProvider(this.doServer, fluidTagsSupplier.accept(this.packOutput, this.provider, this.id, this.existingFileHelper));
			return this;
		}

		public DataHelperBuilder addBiomeTagProvider(BiomeTagsSupplier biomeTagsSupplier) {
			this.dataGenerator.addProvider(this.doServer, biomeTagsSupplier.accept(this.packOutput, this.provider, this.id, this.existingFileHelper));
			return this;
		}

		public DataHelperBuilder addRecipeProvider(RecipeSupplier recipeSupplier) {
			this.dataGenerator.addProvider(this.doServer, recipeSupplier.accept(this.packOutput));
			return this;
		}

		public DataHelperBuilder addLootProvider(LootSupplier lootSupplier) {
			this.dataGenerator.addProvider(this.doServer, lootSupplier.accept(this.packOutput));
			return this;
		}

		public DataHelperBuilder addAdvancementProvider(List<AdvancementGenerator> advancementGenerators) {
			this.dataGenerator.addProvider(this.doServer, new ForgeAdvancementProvider(this.packOutput, this.provider, this.existingFileHelper, advancementGenerators));
			return this;
		}

		public DataHelperBuilder addBiome(BiomeSupplier biomeSuppliers) {
			this.dataGenerator.addProvider(this.doServer, biomeSuppliers.accept(this.packOutput, this.id));
			return this;
		}

		public DataHelperBuilder addDimension(DimensionSupplier dimensionSupplier) {
			this.dataGenerator.addProvider(this.doServer, dimensionSupplier.accept(this.packOutput, this.id));
			return this;
		}

		public DataHelperBuilder addGeneric(GenericSupplier genericSuppliers) {
			this.dataGenerator.addProvider(this.doServer, genericSuppliers.accept(this.packOutput, this.id));
			return this;
		}

		public DataHelperBuilder addGeneric(GenericForgeSupplier genericForgeSuppliers) {
			this.dataGenerator.addProvider(this.doServer, genericForgeSuppliers.accept(this.packOutput, this.id, this.existingFileHelper));
			return this;
		}

		public void build() {
		}
	}

	public static enum Codes {
		AFRIKAANS("af_za"),
		ARABIC("ar_sa"),
		ASTURIAN("ast_es"),
		AZERBAIJANI("az_az"),
		BASHKIR("ba_ru"),
		BAVARIAN("bar"),
		BELARUSIAN("be_by"),
		BULGARIAN("bg_bg"),
		BRETON("br_fr"),
		BRABANTIAN("brb"),
		BOSNIAN("bs_ba"),
		CATALAN("ca_es"),
		CZECH("cs_cz"),
		WELSH("cy_gb"),
		DANISH("da_dk"),
		AUSTRIAN_GERMAN("de_at"),
		SWISS_GERMAN("de_ch"),
		GERMAN("de_de"),
		GREEK("el_gr"),
		AUSTRALIAN_ENGLISH("en_au"),
		CANADIAN_ENGLISH("en_ca"),
		BRITISH_ENGLISH("en_gb"),
		NEW_ZEALAND_ENGLISH("en_nz"),
		PIRATE_ENGLISH("en_pt"),
		UPSIDE_DOWN_BRITISH_ENGLISH("en_ud"),
		AMERICAN_ENGLISH("en_us"),
		ANGLISH("enp"),
		EARLY_MODERN_ENGLISH("enws"),
		ESPERANTO("eo_uy"),
		ARGENTINIAN_SPANISH("es_ar"),
		CHILEAN_SPANISH("es_cl"),
		ECUADORIAN_SPANISH("es_ec"),
		EUROPEAN_SPANISH("es_es"),
		MEXICAN_SPANISH("es_mx"),
		URUGUAYAN_SPANISH("es_uy"),
		VENEZUELAN_SPANISH("es_ve"),
		ANDALUSIAN("esan"),
		ESTONIAN("et_ee"),
		BASQUE("eu_es"),
		PERSIAN("fa_ir"),
		FINNISH("fi_fi"),
		FILIPINO("fil_ph"),
		FAROESE("fo_fo"),
		CANADIAN_FRENCH("fr_ca"),
		EUROPEAN_FRENCH("fr_fr"),
		EAST_FRANCONIAN("fra_de"),
		FRIULIAN("fur_it"),
		FRISIAN("fy_nl"),
		IRISH("ga_ie"),
		SCOTTISH_GAELIC("gd_gb"),
		GALICIAN("gl_es"),
		HAWAIIAN("haw_us"),
		HEBREW("he_il"),
		HINDI("hi_in"),
		CROATIAN("hr_hr"),
		HUNGARIAN("hu_hu"),
		ARMENIAN("hy_am"),
		INDONESIAN("id_id"),
		IGBO("ig_ng"),
		IDO("io_en"),
		ICELANDIC("is_is"),
		INTERSLAVIC("isv"),
		ITALIAN("it_it"),
		JAPANESE("ja_jp"),
		LOJBAN("jbo_en"),
		GEORGIAN("ka_ge"),
		KAZAKH("kk_kz"),
		KANNADA("kn_in"),
		KOREAN("ko_kr"),
		RIPUARIAN("ksh"),
		CORNISH("kw_gb"),
		LATIN("la_la"),
		LUXEMBOURGISH("lb_lu"),
		LIMBURGISH("li_li"),
		LOMBARD("lmo"),
		LOLCAT("lol_us"),
		LITHUANIAN("lt_lt"),
		LATVIAN("lv_lv"),
		CLASSICAL_CHINESE("lzh"),
		MACEDONIAN("mk_mk"),
		MONGOLIAN("mn_mn"),
		MALAY("ms_my"),
		MALTESE("mt_mt"),
		NAHUATL("nah"),
		LOW_GERMAN("nds_de"),
		FLEMISH("nl_be"),
		DUTCH("nl_nl"),
		NYNORSK("nn_no"),
		BOKMAL("no_no‌"),
		OCCITAN("oc_fr"),
		ELFDALIAN("ovd"),
		POLISH("pl_pl"),
		BRAZILIAN_PORTUGUESE("pt_br"),
		EUROPEAN_PORTUGUESE("pt_pt"),
		QUENYA("qya_aa"),
		ROMANIAN("ro_ro"),
		OLD_RUSSIAN("rpr"),
		RUSSIAN("ru_ru"),
		RUSYN("ry_ua"),
		NORTHERN_SAMI("se_no"),
		SLOVAK("sk_sk"),
		SLOVENIAN("sl_si"),
		SOMALI("so_so"),
		ALBANIAN("sq_al"),
		SERBIAN_LATIN("sr_cs"),
		SERBIAN_CYRILLIC("sr_sp"),
		SWEDISH("sv_se"),
		UPPER_SAXON_GERMAN("sxu"),
		SILESIAN("szl"),
		TAMIL("ta_in"),
		THAI("th_th"),
		TAGALOG("tl_ph"),
		KLINGON("tlh_aa"),
		TOKI_PONA("tok"),
		TURKISH("tr_tr"),
		TATAR("tt_ru"),
		UKRAINIAN("uk_ua"),
		VALENCIAN("val_es"),
		VENETIAN("vec_it"),
		VIETNAMESE("vi_vn"),
		YIDDISH("yi_de"),
		YORUBA("yo_ng"),
		SIMPLIFIED_CHINESE("zh_cn"),
		TRADITIONAL_CHINESE_HONG_KONG("zh_hk"),
		TRADITIONAL_CHINESE_TAIWAN("zh_tw"),
		JAWI_MALAY("zlm_arab");

		private final String code;

		private Codes(String code) {
			this.code = code;
		}

		public String getCode() {
			return this.code;
		}
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
	public static interface BiomeSupplier {
		SimpleBiomeProvider accept(PackOutput packOutput, String id);
	}

	@FunctionalInterface
	public static interface DimensionSupplier {
		SimpleDimensionProvider accept(PackOutput packOutput, String id);
	}

	@FunctionalInterface
	public static interface GenericSupplier {
		DataProvider accept(PackOutput packOutput, String id);
	}

	@FunctionalInterface
	public static interface GenericForgeSupplier {
		DataProvider accept(PackOutput packOutput, String id, ExistingFileHelper existingFileHelper);
	}
}
