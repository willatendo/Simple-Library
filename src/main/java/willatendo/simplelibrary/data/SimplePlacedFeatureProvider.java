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

public abstract class SimplePlacedFeatureProvider implements DataProvider {
	private final PackOutput packOutput;
	private final String id;
	public static final Map<String, JsonObject> PLACED_FEATURES = new HashMap<>();

	public SimplePlacedFeatureProvider(PackOutput packOutput, String id) {
		this.packOutput = packOutput;
		this.id = id;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		Path path = this.packOutput.getOutputFolder();
		ArrayList<CompletableFuture> completableFutures = Lists.newArrayList();
		this.allConfiguredFeatures();
		for (int i = 0; i < PLACED_FEATURES.size(); i++) {
			completableFutures.add(DataProvider.saveStable(cachedOutput, null, path.resolve("data/" + this.id + "/placed_feature/" + PLACED_FEATURES.keySet().stream().toList().get(i) + ".json")));
		}
		return CompletableFuture.allOf(completableFutures.stream().toArray(CompletableFuture[]::new));
	}

	public abstract void allConfiguredFeatures();

	public void simpleConfiguredFeature(String configuredFeature, String id, JsonObject placement) {
		JsonObject placedFeature = new JsonObject();
		placedFeature.addProperty("type", configuredFeature);
		placedFeature.add("placement", placement);
		PLACED_FEATURES.put(id, placedFeature);
	}

	@Override
	public String getName() {
		return this.id + ": Placed Features";
	}
}
