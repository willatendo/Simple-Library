package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import willatendo.simplelibrary.server.util.SimpleUtils;

public abstract class SimpleDimensionProvider implements DataProvider {
	private final PackOutput packOutput;
	private final String modid;
	protected static final Map<String, JsonObject> DIMENSIONS = new HashMap<>();
	protected static final Map<String, JsonObject> DIMENSIONS_TYPES = new HashMap<>();

	public SimpleDimensionProvider(PackOutput packOutput, String modid) {
		this.packOutput = packOutput;
		this.modid = modid;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		Path path = this.packOutput.getOutputFolder();
		ArrayList<CompletableFuture> completableFutures = Lists.newArrayList();
		this.allDimensions();
		for (int i = 0; i < DIMENSIONS.size(); i++) {
			completableFutures.add(DataProvider.saveStable(cachedOutput, DIMENSIONS.values().stream().toList().get(i), path.resolve("data/" + this.modid + "/dimension/" + DIMENSIONS.keySet().stream().toList().get(i) + ".json")));
		}
		for (int i = 0; i < DIMENSIONS_TYPES.size(); i++) {
			completableFutures.add(DataProvider.saveStable(cachedOutput, DIMENSIONS_TYPES.values().stream().toList().get(i), path.resolve("data/" + this.modid + "/dimension_type/" + DIMENSIONS.keySet().stream().toList().get(i) + ".json")));
		}
		return CompletableFuture.allOf(completableFutures.stream().toArray(CompletableFuture[]::new));
	}

	public abstract void allDimensions();

	public void dimension(String id, String dimensionType, String type, String settings, JsonObject biomeSource) {
		JsonObject dimension = new JsonObject();
		dimension.addProperty("type", dimensionType);
		JsonObject generator = new JsonObject();
		generator.addProperty("type", type);
		generator.addProperty("settings", settings);
		generator.add("biome_source", biomeSource);
		dimension.add("generator", generator);
		DIMENSIONS.put(id, dimension);
	}

	public String dimensionType(String id, boolean ultrawarm, boolean natural, int coordinateScale, boolean hasSkylight, boolean hasCeiling, float ambientLight, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int logicalHeight, int height, int minY, TagKey<Block> infiniburnTag, ResourceLocation effects, int monsterLightMin, int monsterLightMax, int monsterSpawnerLight) {
		JsonObject dimensionType = new JsonObject();
		dimensionType.addProperty("ultrawarm", ultrawarm);
		dimensionType.addProperty("natural", natural);
		dimensionType.addProperty("coordinate_scale", coordinateScale);
		dimensionType.addProperty("has_skylight", hasSkylight);
		dimensionType.addProperty("has_ceiling", hasCeiling);
		dimensionType.addProperty("ambient_light", ambientLight);
		dimensionType.addProperty("piglin_safe", piglinSafe);
		dimensionType.addProperty("bed_works", bedWorks);
		dimensionType.addProperty("respawn_anchor_works", respawnAnchorWorks);
		dimensionType.addProperty("has_raids", hasRaids);
		dimensionType.addProperty("logical_height", logicalHeight);
		dimensionType.addProperty("height", height);
		dimensionType.addProperty("min_y", minY);
		dimensionType.addProperty("infiniburn", "#" + infiniburnTag.location().toString());
		dimensionType.addProperty("effects", effects.toString());
		JsonObject monsterSpawnLightLevel = new JsonObject();
		monsterSpawnLightLevel.addProperty("type", "minecraft:uniform");
		JsonObject value = new JsonObject();
		value.addProperty("min_inclusive", monsterLightMin);
		value.addProperty("max_inclusive", monsterLightMax);
		monsterSpawnLightLevel.add("value", value);
		dimensionType.add("monster_spawn_light_level", monsterSpawnLightLevel);
		dimensionType.addProperty("monster_spawn_block_light_limit", monsterSpawnerLight);
		DIMENSIONS_TYPES.put(id, dimensionType);
		return id;
	}

	public JsonObject biomeSource(BiomeSupplier... biomeSuppliers) {
		JsonObject biomeSource = new JsonObject();
		biomeSource.addProperty("type", "minecraft:multi_noise");
		JsonArray biomes = new JsonArray();
		for (BiomeSupplier biomeSupplier : biomeSuppliers) {
			JsonObject biome = new JsonObject();
			biome.addProperty("biome", biomeSupplier.biome().location().toString());
			for (int i = 0; i < biomeSupplier.parameters().size(); i++) {
				if (biomeSupplier.parameters().values().stream().toList().get(i).size() == 1) {
					biome.addProperty(biomeSupplier.parameters().keySet().stream().toList().get(i), biomeSupplier.parameters().values().stream().toList().get(i).get(0));
				} else {
					JsonArray parameter = new JsonArray();
					List<Float> parameters = biomeSupplier.parameters().values().stream().toList().get(i);
					for (float f : parameters) {
						parameter.add(f);
					}
					biome.add(biomeSupplier.parameters().keySet().stream().toList().get(i), parameter);
				}
			}
			biomes.add(biome);
		}
		biomeSource.add("biomes", biomes);
		return biomeSource;
	}

	@Override
	public String getName() {
		return "Dimensions and Dimension Types: " + this.modid;
	}

	public static final record AddLikeNether(ResourceKey<Biome> biome, float temperature, float humidity, float continentalness, float erosion, float weirdness, float depth, float offset) implements BiomeSupplier {
		@Override
		public ResourceKey<Biome> biome() {
			return this.biome;
		}

		@Override
		public Map<String, List<Float>> parameters() {
			HashMap<String, List<Float>> parameters = new HashMap<>();
			parameters.put("temperature", List.of(this.temperature));
			parameters.put("humidity", List.of(this.humidity));
			parameters.put("continentalness", List.of(this.continentalness));
			parameters.put("erosion", List.of(this.erosion));
			parameters.put("weirdness", List.of(this.weirdness));
			parameters.put("depth", List.of(this.depth));
			parameters.put("offset", List.of(this.offset));
			return parameters;
		}
	}

	public static final record AddLikeOverworld(ResourceKey<Biome> biome, float[] temperature, float[] humidity, float[] continentalness, float[] erosion, float[] weirdness, float depth, float offset) implements BiomeSupplier {
		@Override
		public ResourceKey<Biome> biome() {
			return this.biome;
		}

		@Override
		public Map<String, List<Float>> parameters() {
			HashMap<String, List<Float>> parameters = new HashMap<>();
			parameters.put("temperature", SimpleUtils.toList(this.temperature));
			parameters.put("humidity", SimpleUtils.toList(this.humidity));
			parameters.put("continentalness", SimpleUtils.toList(this.continentalness));
			parameters.put("erosion", SimpleUtils.toList(this.erosion));
			parameters.put("weirdness", SimpleUtils.toList(this.weirdness));
			parameters.put("depth", List.of(this.depth));
			parameters.put("offset", List.of(this.offset));
			return parameters;
		}
	}

	public static interface BiomeSupplier {
		ResourceKey<Biome> biome();

		Map<String, List<Float>> parameters();
	}
}
