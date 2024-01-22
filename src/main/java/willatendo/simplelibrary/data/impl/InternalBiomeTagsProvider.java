package willatendo.simplelibrary.data.impl;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import willatendo.simplelibrary.data.tags.SimpleBiomeTagsProvider;
import willatendo.simplelibrary.data.util.ExistingFileHelper;
import willatendo.simplelibrary.server.biome.ForgeBiomeTags;

public class InternalBiomeTagsProvider extends SimpleBiomeTagsProvider {
	public InternalBiomeTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, provider, modId, existingFileHelper);
	}

	@Override
	protected void addTags(Provider provider) {
		this.tag(Biomes.PLAINS, ForgeBiomeTags.IS_PLAINS);
		this.tag(Biomes.DESERT, ForgeBiomeTags.IS_HOT_OVERWORLD, ForgeBiomeTags.IS_DRY_OVERWORLD, ForgeBiomeTags.IS_SANDY, ForgeBiomeTags.IS_DESERT);
		this.tag(Biomes.TAIGA, ForgeBiomeTags.IS_COLD_OVERWORLD, ForgeBiomeTags.IS_CONIFEROUS);
		this.tag(Biomes.SWAMP, ForgeBiomeTags.IS_WET_OVERWORLD, ForgeBiomeTags.IS_SWAMP);
		this.tag(Biomes.NETHER_WASTES, ForgeBiomeTags.IS_HOT_NETHER, ForgeBiomeTags.IS_DRY_NETHER);
		this.tag(Biomes.THE_END, ForgeBiomeTags.IS_COLD_END, ForgeBiomeTags.IS_DRY_END);
		this.tag(Biomes.FROZEN_OCEAN, ForgeBiomeTags.IS_COLD_OVERWORLD, ForgeBiomeTags.IS_SNOWY);
		this.tag(Biomes.FROZEN_RIVER, ForgeBiomeTags.IS_COLD_OVERWORLD, ForgeBiomeTags.IS_SNOWY);
		this.tag(Biomes.SNOWY_PLAINS, ForgeBiomeTags.IS_COLD_OVERWORLD, ForgeBiomeTags.IS_SNOWY, ForgeBiomeTags.IS_WASTELAND, ForgeBiomeTags.IS_PLAINS);
		this.tag(Biomes.MUSHROOM_FIELDS, ForgeBiomeTags.IS_MUSHROOM, ForgeBiomeTags.IS_RARE);
		this.tag(Biomes.JUNGLE, ForgeBiomeTags.IS_HOT_OVERWORLD, ForgeBiomeTags.IS_WET_OVERWORLD, ForgeBiomeTags.IS_DENSE_OVERWORLD);
		this.tag(Biomes.SPARSE_JUNGLE, ForgeBiomeTags.IS_HOT_OVERWORLD, ForgeBiomeTags.IS_WET_OVERWORLD, ForgeBiomeTags.IS_RARE);
		this.tag(Biomes.BEACH, ForgeBiomeTags.IS_WET_OVERWORLD, ForgeBiomeTags.IS_SANDY);
		this.tag(Biomes.SNOWY_BEACH, ForgeBiomeTags.IS_COLD_OVERWORLD, ForgeBiomeTags.IS_SNOWY);
		this.tag(Biomes.DARK_FOREST, ForgeBiomeTags.IS_SPOOKY, ForgeBiomeTags.IS_DENSE_OVERWORLD);
		this.tag(Biomes.SNOWY_TAIGA, ForgeBiomeTags.IS_COLD_OVERWORLD, ForgeBiomeTags.IS_CONIFEROUS, ForgeBiomeTags.IS_SNOWY);
		this.tag(Biomes.OLD_GROWTH_PINE_TAIGA, ForgeBiomeTags.IS_COLD_OVERWORLD, ForgeBiomeTags.IS_CONIFEROUS);
		this.tag(Biomes.WINDSWEPT_FOREST, ForgeBiomeTags.IS_SPARSE_OVERWORLD);
		this.tag(Biomes.SAVANNA, ForgeBiomeTags.IS_HOT_OVERWORLD, ForgeBiomeTags.IS_SPARSE_OVERWORLD);
		this.tag(Biomes.SAVANNA_PLATEAU, ForgeBiomeTags.IS_HOT_OVERWORLD, ForgeBiomeTags.IS_SPARSE_OVERWORLD, ForgeBiomeTags.IS_RARE, ForgeBiomeTags.IS_SLOPE, ForgeBiomeTags.IS_PLATEAU);
		this.tag(Biomes.BADLANDS, ForgeBiomeTags.IS_SANDY, ForgeBiomeTags.IS_DRY_OVERWORLD);
		this.tag(Biomes.WOODED_BADLANDS, ForgeBiomeTags.IS_SANDY, ForgeBiomeTags.IS_DRY_OVERWORLD, ForgeBiomeTags.IS_SPARSE_OVERWORLD, ForgeBiomeTags.IS_SLOPE, ForgeBiomeTags.IS_PLATEAU);
		this.tag(Biomes.MEADOW, ForgeBiomeTags.IS_PLAINS, ForgeBiomeTags.IS_PLATEAU, ForgeBiomeTags.IS_SLOPE);
		this.tag(Biomes.GROVE, ForgeBiomeTags.IS_COLD_OVERWORLD, ForgeBiomeTags.IS_CONIFEROUS, ForgeBiomeTags.IS_SNOWY, ForgeBiomeTags.IS_SLOPE);
		this.tag(Biomes.SNOWY_SLOPES, ForgeBiomeTags.IS_COLD_OVERWORLD, ForgeBiomeTags.IS_SPARSE_OVERWORLD, ForgeBiomeTags.IS_SNOWY, ForgeBiomeTags.IS_SLOPE);
		this.tag(Biomes.JAGGED_PEAKS, ForgeBiomeTags.IS_COLD_OVERWORLD, ForgeBiomeTags.IS_SPARSE_OVERWORLD, ForgeBiomeTags.IS_SNOWY, ForgeBiomeTags.IS_PEAK);
		this.tag(Biomes.FROZEN_PEAKS, ForgeBiomeTags.IS_COLD_OVERWORLD, ForgeBiomeTags.IS_SPARSE_OVERWORLD, ForgeBiomeTags.IS_SNOWY, ForgeBiomeTags.IS_PEAK);
		this.tag(Biomes.STONY_PEAKS, ForgeBiomeTags.IS_HOT_OVERWORLD, ForgeBiomeTags.IS_PEAK);
		this.tag(Biomes.SMALL_END_ISLANDS, ForgeBiomeTags.IS_COLD_END, ForgeBiomeTags.IS_DRY_END);
		this.tag(Biomes.END_MIDLANDS, ForgeBiomeTags.IS_COLD_END, ForgeBiomeTags.IS_DRY_END);
		this.tag(Biomes.END_HIGHLANDS, ForgeBiomeTags.IS_COLD_END, ForgeBiomeTags.IS_DRY_END);
		this.tag(Biomes.END_BARRENS, ForgeBiomeTags.IS_COLD_END, ForgeBiomeTags.IS_DRY_END);
		this.tag(Biomes.WARM_OCEAN, ForgeBiomeTags.IS_HOT_OVERWORLD);
		this.tag(Biomes.COLD_OCEAN, ForgeBiomeTags.IS_COLD_OVERWORLD);
		this.tag(Biomes.DEEP_COLD_OCEAN, ForgeBiomeTags.IS_COLD_OVERWORLD);
		this.tag(Biomes.DEEP_FROZEN_OCEAN, ForgeBiomeTags.IS_COLD_OVERWORLD);
		this.tag(Biomes.THE_VOID, ForgeBiomeTags.IS_VOID);
		this.tag(Biomes.SUNFLOWER_PLAINS, ForgeBiomeTags.IS_PLAINS, ForgeBiomeTags.IS_RARE);
		this.tag(Biomes.WINDSWEPT_GRAVELLY_HILLS, ForgeBiomeTags.IS_SPARSE_OVERWORLD, ForgeBiomeTags.IS_RARE);
		this.tag(Biomes.FLOWER_FOREST, ForgeBiomeTags.IS_RARE);
		this.tag(Biomes.ICE_SPIKES, ForgeBiomeTags.IS_COLD_OVERWORLD, ForgeBiomeTags.IS_SNOWY, ForgeBiomeTags.IS_RARE);
		this.tag(Biomes.OLD_GROWTH_BIRCH_FOREST, ForgeBiomeTags.IS_DENSE_OVERWORLD, ForgeBiomeTags.IS_RARE);
		this.tag(Biomes.OLD_GROWTH_SPRUCE_TAIGA, ForgeBiomeTags.IS_DENSE_OVERWORLD, ForgeBiomeTags.IS_RARE);
		this.tag(Biomes.WINDSWEPT_SAVANNA, ForgeBiomeTags.IS_HOT_OVERWORLD, ForgeBiomeTags.IS_DRY_OVERWORLD, ForgeBiomeTags.IS_SPARSE_OVERWORLD, ForgeBiomeTags.IS_RARE);
		this.tag(Biomes.ERODED_BADLANDS, ForgeBiomeTags.IS_HOT_OVERWORLD, ForgeBiomeTags.IS_DRY_OVERWORLD, ForgeBiomeTags.IS_SPARSE_OVERWORLD, ForgeBiomeTags.IS_RARE);
		this.tag(Biomes.BAMBOO_JUNGLE, ForgeBiomeTags.IS_HOT_OVERWORLD, ForgeBiomeTags.IS_WET_OVERWORLD, ForgeBiomeTags.IS_RARE);
		this.tag(Biomes.LUSH_CAVES, ForgeBiomeTags.IS_CAVE, ForgeBiomeTags.IS_LUSH, ForgeBiomeTags.IS_WET_OVERWORLD);
		this.tag(Biomes.DRIPSTONE_CAVES, ForgeBiomeTags.IS_CAVE, ForgeBiomeTags.IS_SPARSE_OVERWORLD);
		this.tag(Biomes.SOUL_SAND_VALLEY, ForgeBiomeTags.IS_HOT_NETHER, ForgeBiomeTags.IS_DRY_NETHER);
		this.tag(Biomes.CRIMSON_FOREST, ForgeBiomeTags.IS_HOT_NETHER, ForgeBiomeTags.IS_DRY_NETHER);
		this.tag(Biomes.WARPED_FOREST, ForgeBiomeTags.IS_HOT_NETHER, ForgeBiomeTags.IS_DRY_NETHER);
		this.tag(Biomes.BASALT_DELTAS, ForgeBiomeTags.IS_HOT_NETHER, ForgeBiomeTags.IS_DRY_NETHER);
		this.tag(Biomes.MANGROVE_SWAMP, ForgeBiomeTags.IS_WET_OVERWORLD, ForgeBiomeTags.IS_HOT_OVERWORLD, ForgeBiomeTags.IS_SWAMP);
		this.tag(Biomes.DEEP_DARK, ForgeBiomeTags.IS_CAVE, ForgeBiomeTags.IS_RARE, ForgeBiomeTags.IS_SPOOKY);

		this.tag(ForgeBiomeTags.IS_HOT).addTag(ForgeBiomeTags.IS_HOT_OVERWORLD).addTag(ForgeBiomeTags.IS_HOT_NETHER).addOptionalTag(ForgeBiomeTags.IS_HOT_END.location());
		this.tag(ForgeBiomeTags.IS_COLD).addTag(ForgeBiomeTags.IS_COLD_OVERWORLD).addOptionalTag(ForgeBiomeTags.IS_COLD_NETHER.location()).addTag(ForgeBiomeTags.IS_COLD_END);
		this.tag(ForgeBiomeTags.IS_SPARSE).addTag(ForgeBiomeTags.IS_SPARSE_OVERWORLD).addOptionalTag(ForgeBiomeTags.IS_SPARSE_NETHER.location()).addOptionalTag(ForgeBiomeTags.IS_SPARSE_END.location());
		this.tag(ForgeBiomeTags.IS_DENSE).addTag(ForgeBiomeTags.IS_DENSE_OVERWORLD).addOptionalTag(ForgeBiomeTags.IS_DENSE_NETHER.location()).addOptionalTag(ForgeBiomeTags.IS_DENSE_END.location());
		this.tag(ForgeBiomeTags.IS_WET).addTag(ForgeBiomeTags.IS_WET_OVERWORLD).addOptionalTag(ForgeBiomeTags.IS_WET_NETHER.location()).addOptionalTag(ForgeBiomeTags.IS_WET_END.location());
		this.tag(ForgeBiomeTags.IS_DRY).addTag(ForgeBiomeTags.IS_DRY_OVERWORLD).addTag(ForgeBiomeTags.IS_DRY_NETHER).addTag(ForgeBiomeTags.IS_DRY_END);

		this.tag(ForgeBiomeTags.IS_WATER).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_RIVER);
		this.tag(ForgeBiomeTags.IS_MOUNTAIN).addTag(ForgeBiomeTags.IS_PEAK).addTag(ForgeBiomeTags.IS_SLOPE);
		this.tag(ForgeBiomeTags.IS_UNDERGROUND).addTag(ForgeBiomeTags.IS_CAVE);
	}

	private void tag(ResourceKey<Biome> biome, TagKey<Biome>... tags) {
		for (TagKey<Biome> key : tags) {
			this.tag(key).add(biome);
		}
	}
}
