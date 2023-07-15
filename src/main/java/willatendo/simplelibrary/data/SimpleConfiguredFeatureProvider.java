package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class SimpleConfiguredFeatureProvider implements DataProvider {
	private final PackOutput packOutput;
	private final String id;
	public static final Map<String, JsonObject> CONFIGURED_FEATURES = new HashMap<>();

	public SimpleConfiguredFeatureProvider(PackOutput packOutput, String id) {
		this.packOutput = packOutput;
		this.id = id;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		Path path = this.packOutput.getOutputFolder();
		ArrayList<CompletableFuture> completableFutures = Lists.newArrayList();
		this.allConfiguredFeatures();
		for (int i = 0; i < CONFIGURED_FEATURES.size(); i++) {
			completableFutures.add(DataProvider.saveStable(cachedOutput, null, path.resolve("data/" + this.id + "/configured_feature/" + CONFIGURED_FEATURES.keySet().stream().toList().get(i) + ".json")));
		}
		return CompletableFuture.allOf(completableFutures.stream().toArray(CompletableFuture[]::new));
	}

	public abstract void allConfiguredFeatures();

	public void simpleConfiguredFeature(Feature<?> feature, String id, JsonObject config) {
		JsonObject configuredFeature = new JsonObject();
		configuredFeature.addProperty("type", ForgeRegistries.FEATURES.getKey(feature).toString());
		configuredFeature.add("config", config);
		CONFIGURED_FEATURES.put(id, configuredFeature);
	}

	@Override
	public String getName() {
		return this.id + ": Configured Features";
	}
}
