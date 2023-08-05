package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class SimpleBiomeModifierProvider implements DataProvider {
	private final PackOutput packOutput;
	private final String modid;
	protected static final Map<String, JsonObject> BIOME_MODIFERS = new HashMap<>();

	public SimpleBiomeModifierProvider(PackOutput packOutput, String modid) {
		this.packOutput = packOutput;
		this.modid = modid;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		Path path = this.packOutput.getOutputFolder();
		ArrayList<CompletableFuture> completableFutures = Lists.newArrayList();
		this.allBiomeModifiers();
		for (int i = 0; i < BIOME_MODIFERS.size(); i++) {
			completableFutures.add(DataProvider.saveStable(cachedOutput, BIOME_MODIFERS.values().stream().toList().get(i), path.resolve("data/" + this.modid + "/forge/biome_modifier/" + BIOME_MODIFERS.keySet().stream().toList().get(i) + ".json")));
		}
		return CompletableFuture.allOf(completableFutures.stream().toArray(CompletableFuture[]::new));
	}

	public abstract void allBiomeModifiers();

	public void addPlacedFeature(ResourceKey<PlacedFeature> placedFeature, TagKey<Biome> biomes, Decoration decoration) {
		this.addPlacedFeature("add_" + placedFeature.location().getPath() + "_to_" + biomes.location().getPath(), placedFeature, biomes, decoration);
	}

	public void removePlacedFeature(ResourceKey<PlacedFeature> placedFeature, TagKey<Biome> biomes, Decoration... decorations) {
		this.removePlacedFeature("add_" + placedFeature.location().getPath() + "_to_" + biomes.location().getPath(), placedFeature, biomes, decorations);
	}

	public void addPlacedFeature(String id, ResourceKey<PlacedFeature> placedFeature, TagKey<Biome> biomes, Decoration decoration) {
		JsonObject addPlacedFeature = new JsonObject();
		addPlacedFeature.addProperty("type", "forge:add_features");
		addPlacedFeature.addProperty("biomes", "#" + biomes.location().toString());
		addPlacedFeature.addProperty("features", placedFeature.location().toString());
		addPlacedFeature.addProperty("step", decoration.getName());
		BIOME_MODIFERS.put(id, addPlacedFeature);
	}

	public void removePlacedFeature(String id, ResourceKey<PlacedFeature> placedFeature, TagKey<Biome> biomes, Decoration... decorations) {
		JsonObject addPlacedFeature = new JsonObject();
		addPlacedFeature.addProperty("type", "forge:remove_features");
		addPlacedFeature.addProperty("biomes", "#" + biomes.location().toString());
		addPlacedFeature.addProperty("features", placedFeature.location().toString());
		if (decorations.length == 1) {
			addPlacedFeature.addProperty("step", decorations[0].getName());
		} else {
			JsonArray jsonArray = new JsonArray();
			for (Decoration decoration : decorations) {
				jsonArray.add(decoration.getName());
			}
			addPlacedFeature.add("steps", jsonArray);
		}
		BIOME_MODIFERS.put(id, addPlacedFeature);
	}

	public void addSpawns(String id, TagKey<Biome> biomes, Supplier<EntityType<?>> entityType, int weight, int minCount, int maxCount) {
		JsonObject addPlacedFeature = new JsonObject();
		addPlacedFeature.addProperty("type", "forge:add_spawns");
		addPlacedFeature.addProperty("biomes", "#" + biomes.location().toString());
		JsonObject spawner = new JsonObject();
		spawner.addProperty("type", ForgeRegistries.ENTITY_TYPES.getKey(entityType.get()).toString());
		spawner.addProperty("weight", weight);
		spawner.addProperty("minCount", minCount);
		spawner.addProperty("maxCount", maxCount);
		addPlacedFeature.add("spawners", spawner);
		BIOME_MODIFERS.put(id, addPlacedFeature);
	}

	public void addSpawns(String id, TagKey<Biome> biomes, AddSpawn... addSpawns) {
		JsonObject addPlacedFeature = new JsonObject();
		addPlacedFeature.addProperty("type", "forge:add_spawns");
		addPlacedFeature.addProperty("biomes", "#" + biomes.location().toString());
		JsonArray spawner = new JsonArray();
		for (AddSpawn addSpawn : addSpawns) {
			JsonObject spawn = new JsonObject();
			spawn.addProperty("type", ForgeRegistries.ENTITY_TYPES.getKey(addSpawn.entityType().get()).toString());
			spawn.addProperty("weight", addSpawn.weight());
			spawn.addProperty("minCount", addSpawn.minCount());
			spawn.addProperty("maxCount", addSpawn.maxCount());
			spawner.add(spawn);
		}
		addPlacedFeature.add("spawners", spawner);
		BIOME_MODIFERS.put(id, addPlacedFeature);
	}

	public void removeSpawns(String id, TagKey<Biome> biomes, TagKey<EntityType<?>> entityType) {
		JsonObject addPlacedFeature = new JsonObject();
		addPlacedFeature.addProperty("type", "forge:add_spawns");
		addPlacedFeature.addProperty("biomes", "#" + biomes.location().toString());
		addPlacedFeature.addProperty("entity_types", "#" + entityType.location().toString());
		BIOME_MODIFERS.put(id, addPlacedFeature);
	}

	public void removeSpawns(String id, TagKey<Biome> biomes, Supplier<EntityType<?>>... entityTypes) {
		JsonObject addPlacedFeature = new JsonObject();
		addPlacedFeature.addProperty("type", "forge:add_spawns");
		addPlacedFeature.addProperty("biomes", "#" + biomes.location().toString());
		if (entityTypes.length == 1) {
			addPlacedFeature.addProperty("entity_types", ForgeRegistries.ENTITY_TYPES.getKey(entityTypes[0].get()).toString());
		} else {
			JsonArray jsonArray = new JsonArray();
			for (Supplier<EntityType<?>> entityType : entityTypes) {
				jsonArray.add(ForgeRegistries.ENTITY_TYPES.getKey(entityType.get()).toString());
			}
			addPlacedFeature.add("entity_types", jsonArray);
		}
		BIOME_MODIFERS.put(id, addPlacedFeature);
	}

	@Override
	public String getName() {
		return this.modid + ": Configured Features";
	}

	public static record AddSpawn(Supplier<EntityType<?>> entityType, int weight, int minCount, int maxCount) {
	}
}
