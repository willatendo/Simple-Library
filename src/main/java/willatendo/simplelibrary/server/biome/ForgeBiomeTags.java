package willatendo.simplelibrary.server.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import willatendo.simplelibrary.server.util.SimpleUtils;
import willatendo.simplelibrary.server.util.TagRegister;

public class ForgeBiomeTags {
	public static final TagRegister<Biome> BIOMES = SimpleUtils.create(Registries.BIOME, SimpleUtils.FORGE);

	public static final TagKey<Biome> IS_HOT = BIOMES.register("is_hot");
	public static final TagKey<Biome> IS_HOT_OVERWORLD = BIOMES.register("is_hot/overworld");
	public static final TagKey<Biome> IS_HOT_NETHER = BIOMES.register("is_hot/nether");
	public static final TagKey<Biome> IS_HOT_END = BIOMES.register("is_hot/end");
	public static final TagKey<Biome> IS_COLD = BIOMES.register("is_cold");
	public static final TagKey<Biome> IS_COLD_OVERWORLD = BIOMES.register("is_cold/overworld");
	public static final TagKey<Biome> IS_COLD_NETHER = BIOMES.register("is_cold/nether");
	public static final TagKey<Biome> IS_COLD_END = BIOMES.register("is_cold/end");
	public static final TagKey<Biome> IS_SPARSE = BIOMES.register("is_sparse");
	public static final TagKey<Biome> IS_SPARSE_OVERWORLD = BIOMES.register("is_sparse/overworld");
	public static final TagKey<Biome> IS_SPARSE_NETHER = BIOMES.register("is_sparse/nether");
	public static final TagKey<Biome> IS_SPARSE_END = BIOMES.register("is_sparse/end");
	public static final TagKey<Biome> IS_DENSE = BIOMES.register("is_dense");
	public static final TagKey<Biome> IS_DENSE_OVERWORLD = BIOMES.register("is_dense/overworld");
	public static final TagKey<Biome> IS_DENSE_NETHER = BIOMES.register("is_dense/nether");
	public static final TagKey<Biome> IS_DENSE_END = BIOMES.register("is_dense/end");
	public static final TagKey<Biome> IS_WET = BIOMES.register("is_wet");
	public static final TagKey<Biome> IS_WET_OVERWORLD = BIOMES.register("is_wet/overworld");
	public static final TagKey<Biome> IS_WET_NETHER = BIOMES.register("is_wet/nether");
	public static final TagKey<Biome> IS_WET_END = BIOMES.register("is_wet/end");
	public static final TagKey<Biome> IS_DRY = BIOMES.register("is_dry");
	public static final TagKey<Biome> IS_DRY_OVERWORLD = BIOMES.register("is_dry/overworld");
	public static final TagKey<Biome> IS_DRY_NETHER = BIOMES.register("is_dry/nether");
	public static final TagKey<Biome> IS_DRY_END = BIOMES.register("is_dry/end");
	public static final TagKey<Biome> IS_CONIFEROUS = BIOMES.register("is_coniferous");
	public static final TagKey<Biome> IS_SPOOKY = BIOMES.register("is_spooky");
	public static final TagKey<Biome> IS_DEAD = BIOMES.register("is_dead");
	public static final TagKey<Biome> IS_LUSH = BIOMES.register("is_lush");
	public static final TagKey<Biome> IS_MUSHROOM = BIOMES.register("is_mushroom");
	public static final TagKey<Biome> IS_MAGICAL = BIOMES.register("is_magical");
	public static final TagKey<Biome> IS_RARE = BIOMES.register("is_rare");
	public static final TagKey<Biome> IS_PLATEAU = BIOMES.register("is_plateau");
	public static final TagKey<Biome> IS_MODIFIED = BIOMES.register("is_modified");
	public static final TagKey<Biome> IS_WATER = BIOMES.register("is_water");
	public static final TagKey<Biome> IS_DESERT = BIOMES.register("is_desert");
	public static final TagKey<Biome> IS_PLAINS = BIOMES.register("is_plains");
	public static final TagKey<Biome> IS_SWAMP = BIOMES.register("is_swamp");
	public static final TagKey<Biome> IS_SANDY = BIOMES.register("is_sandy");
	public static final TagKey<Biome> IS_SNOWY = BIOMES.register("is_snowy");
	public static final TagKey<Biome> IS_WASTELAND = BIOMES.register("is_wasteland");
	public static final TagKey<Biome> IS_VOID = BIOMES.register("is_void");
	public static final TagKey<Biome> IS_UNDERGROUND = BIOMES.register("is_underground");
	public static final TagKey<Biome> IS_CAVE = BIOMES.register("is_cave");
	public static final TagKey<Biome> IS_PEAK = BIOMES.register("is_peak");
	public static final TagKey<Biome> IS_SLOPE = BIOMES.register("is_slope");
	public static final TagKey<Biome> IS_MOUNTAIN = BIOMES.register("is_mountain");
}
