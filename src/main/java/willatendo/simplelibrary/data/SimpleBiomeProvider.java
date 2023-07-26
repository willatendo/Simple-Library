package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class SimpleBiomeProvider implements DataProvider {
	private final PackOutput packOutput;
	private final String modid;
	private static final Map<String, JsonObject> BIOMES = new HashMap<>();

	public SimpleBiomeProvider(PackOutput packOutput, String modid) {
		this.packOutput = packOutput;
		this.modid = modid;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		Path path = this.packOutput.getOutputFolder();
		ArrayList<CompletableFuture> completableFutures = Lists.newArrayList();
		this.allBiomes();
		for (int i = 0; i < BIOMES.size(); i++) {
			completableFutures.add(DataProvider.saveStable(cachedOutput, BIOMES.values().stream().toList().get(i), path.resolve("data/" + this.modid + "/worldgen/biome/" + BIOMES.keySet().stream().toList().get(i) + ".json")));
		}
		return CompletableFuture.allOf(completableFutures.stream().toArray(CompletableFuture[]::new));
	}

	public abstract void allBiomes();

	public BiomeBuilder biome(ResourceKey<Biome> biome, boolean hasPreciptation, float temperature, float downfall) {
		return new BiomeBuilder(biome, hasPreciptation, temperature, downfall);
	}

	@Override
	public String getName() {
		return this.modid + ": Biomes";
	}

	public static int calculateSkyColor(float temperature) {
		float colour = temperature / 3.0F;
		colour = Mth.clamp(colour, -1.0F, 1.0F);
		return Mth.hsvToRgb(0.62222224F - colour * 0.05F, 0.5F + colour * 0.1F, 1.0F);
	}

	public static final class BiomeBuilder {
		private final String id;
		private boolean hasPrecipitation;
		private float temperature;
		private float downfall;
		private int skyColour;
		private int waterColour = 4159204;
		private int waterFogColour = 329011;
		private int fogColour = 12638463;
		private Optional<Integer> grassColour;
		private Optional<Integer> foliageColour;
		private JsonObject carvers;
		private JsonArray ambient;
		private JsonArray axolotls;
		private JsonArray creature;
		private JsonArray misc;
		private JsonArray monster;
		private JsonArray undergroundWaterCreature;
		private JsonArray waterAmbient;
		private JsonArray waterCreature;
		private Optional<Music> music;
		private final List<ResourceKey<PlacedFeature>>[] placedFeatures = new List[GenerationStep.Decoration.values().length];

		private BiomeBuilder(ResourceKey<Biome> biome, boolean hasPrecipitation, float temperature, float downfall) {
			this.id = biome.location().getPath();
			this.hasPrecipitation = hasPrecipitation;
			this.temperature = temperature;
			this.downfall = downfall;
			this.skyColour = SimpleBiomeProvider.calculateSkyColor(temperature);
		}

		public BiomeBuilder setSkyColour(int skyColour) {
			this.skyColour = skyColour;
			return this;
		}

		public BiomeBuilder setWaterColour(int waterColour) {
			this.waterColour = waterColour;
			return this;
		}

		public BiomeBuilder setWaterFogColour(int waterFogColour) {
			this.waterFogColour = waterFogColour;
			return this;
		}

		public BiomeBuilder setFogColour(int fogColour) {
			this.fogColour = fogColour;
			return this;
		}

		public BiomeBuilder setGrassColour(int grassColour) {
			this.grassColour = Optional.of(grassColour);
			return this;
		}

		public BiomeBuilder setFoliageColour(int foliageColour) {
			this.foliageColour = Optional.of(foliageColour);
			return this;
		}

		public BiomeBuilder setMusic(Music music) {
			this.music = Optional.of(music);
			return this;
		}

		public BiomeBuilder addSpawn(MobCategory mobCategory, Supplier<EntityType<?>> entityType, int weight, int minCount, int maxCount) {
			JsonObject spawn = new JsonObject();
			spawn.addProperty("type", ForgeRegistries.ENTITY_TYPES.getKey(entityType.get()).toString());
			spawn.addProperty("weight", weight);
			spawn.addProperty("minCount", minCount);
			spawn.addProperty("maxCount", maxCount);
			if (mobCategory == MobCategory.AMBIENT) {
				this.ambient.add(spawn);
			}
			if (mobCategory == MobCategory.AXOLOTLS) {
				this.axolotls.add(spawn);
			}
			if (mobCategory == MobCategory.CREATURE) {
				this.creature.add(spawn);
			}
			if (mobCategory == MobCategory.MISC) {
				this.misc.add(spawn);
			}
			if (mobCategory == MobCategory.MONSTER) {
				this.monster.add(spawn);
			}
			if (mobCategory == MobCategory.UNDERGROUND_WATER_CREATURE) {
				this.undergroundWaterCreature.add(spawn);
			}
			if (mobCategory == MobCategory.WATER_AMBIENT) {
				this.waterAmbient.add(spawn);
			}
			if (mobCategory == MobCategory.WATER_CREATURE) {
				this.waterCreature.add(spawn);
			}
			return this;
		}

		public BiomeBuilder addFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> placedFeature) {
			this.placedFeatures[decoration.ordinal()].add(placedFeature);
			return this;
		}

		public BiomeBuilder addBasicCarvers() {
			JsonObject carvers = this.carvers;
			JsonArray air = new JsonArray();
			air.add("minecraft:cave");
			air.add("minecraft:cave_extra_underground");
			air.add("minecraft:canyon");
			carvers.add("air", air);
			return this;
		}

		public void build() {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("temperature", this.temperature);
			jsonObject.addProperty("downfall", this.downfall);
			jsonObject.addProperty("has_precipitation", this.hasPrecipitation);
			JsonObject effects = new JsonObject();
			effects.addProperty("sky_color", this.skyColour);
			effects.addProperty("fog_color", this.fogColour);
			effects.addProperty("water_color", this.waterColour);
			effects.addProperty("water_fog_color", this.waterFogColour);
			if (this.grassColour.isPresent()) {
				effects.addProperty("grass_color", this.grassColour.get());
			}
			if (this.foliageColour.isPresent()) {
				effects.addProperty("foliage_color", this.foliageColour.get());
			}
			if (this.music.isPresent()) {
				JsonObject music = new JsonObject();
				Music theMusic = this.music.get();
				music.addProperty("sound", ForgeRegistries.SOUND_EVENTS.getKey(theMusic.getEvent().get()).toString());
				music.addProperty("min_delay", theMusic.getMinDelay());
				music.addProperty("max_delay", theMusic.getMaxDelay());
				music.addProperty("replace_current_music", theMusic.replaceCurrentMusic());
				effects.add("music", music);
			}
			JsonObject spawners = new JsonObject();
			spawners.add("ambient", this.ambient);
			spawners.add("axolotls", this.axolotls);
			spawners.add("creature", this.creature);
			spawners.add("misc", this.misc);
			spawners.add("monster", this.monster);
			spawners.add("underground_water_creature", this.undergroundWaterCreature);
			spawners.add("water_ambient", this.waterAmbient);
			spawners.add("water_creature", this.waterCreature);
			jsonObject.add("spawners", spawners);
			jsonObject.add("carvers", this.carvers);
			JsonArray features = new JsonArray();
			for (int i = 0; i < this.placedFeatures.length; i++) {
				JsonArray placedFeatures = new JsonArray();
				JsonArray array = new JsonArray();
				for (ResourceKey<PlacedFeature> placedFeature : this.placedFeatures[i]) {
					array.add(placedFeature.location().toString());
				}
				placedFeatures.addAll(array);
				features.add(placedFeatures);
			}
			jsonObject.add("features", features);
			jsonObject.add("effects", effects);
			SimpleBiomeProvider.BIOMES.put(this.id, jsonObject);
		}
	}
}
