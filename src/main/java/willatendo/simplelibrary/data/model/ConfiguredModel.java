package willatendo.simplelibrary.data.model;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ObjectArrays;
import com.google.gson.JsonObject;

import net.minecraft.client.resources.model.BlockModelRotation;
import willatendo.simplelibrary.data.model.MultiPartBlockStateBuilder.PartBuilder;
import willatendo.simplelibrary.data.model.VariantBlockStateBuilder.PartialBlockstate;

public final class ConfiguredModel {
	public static final int DEFAULT_WEIGHT = 1;

	public final ModelFile model;
	public final int rotationX;
	public final int rotationY;
	public final boolean uvLock;
	public final int weight;

	private static IntStream validRotations() {
		return IntStream.range(0, 4).map(i -> i * 90);
	}

	public static ConfiguredModel[] allYRotations(ModelFile modelFile, int x, boolean uvlock) {
		return allYRotations(modelFile, x, uvlock, DEFAULT_WEIGHT);
	}

	public static ConfiguredModel[] allYRotations(ModelFile modelFile, int x, boolean uvlock, int weight) {
		return validRotations().mapToObj(y -> new ConfiguredModel(modelFile, x, y, uvlock, weight)).toArray(ConfiguredModel[]::new);
	}

	public static ConfiguredModel[] allRotations(ModelFile modelmodelFile, boolean uvlock) {
		return allRotations(modelmodelFile, uvlock, DEFAULT_WEIGHT);
	}

	public static ConfiguredModel[] allRotations(ModelFile modelFile, boolean uvlock, int weight) {
		return validRotations().mapToObj(x -> allYRotations(modelFile, x, uvlock, weight)).flatMap(Arrays::stream).toArray(ConfiguredModel[]::new);
	}

	public ConfiguredModel(ModelFile modelFile, int rotationX, int rotationY, boolean uvLock, int weight) {
		Preconditions.checkNotNull(modelFile);
		this.model = modelFile;
		checkRotation(rotationX, rotationY);
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.uvLock = uvLock;
		checkWeight(weight);
		this.weight = weight;
	}

	public ConfiguredModel(ModelFile modelFile, int rotationX, int rotationY, boolean uvLock) {
		this(modelFile, rotationX, rotationY, uvLock, DEFAULT_WEIGHT);
	}

	public ConfiguredModel(ModelFile modelFile) {
		this(modelFile, 0, 0, false);
	}

	public static void checkRotation(int rotationX, int rotationY) {
		Preconditions.checkArgument(BlockModelRotation.by(rotationX, rotationY) != null, "Invalid model rotation x=%d, y=%d", rotationX, rotationY);
	}

	public static void checkWeight(int weight) {
		Preconditions.checkArgument(weight >= 1, "Model weight must be greater than or equal to 1. Found: %d", weight);
	}

	public JsonObject toJSON(boolean includeWeight) {
		JsonObject modelJson = new JsonObject();
		modelJson.addProperty("model", this.model.getResourceLocation().toString());
		if (this.rotationX != 0)
			modelJson.addProperty("x", this.rotationX);
		if (this.rotationY != 0)
			modelJson.addProperty("y", this.rotationY);
		if (this.uvLock)
			modelJson.addProperty("uvlock", this.uvLock);
		if (includeWeight && this.weight != DEFAULT_WEIGHT)
			modelJson.addProperty("weight", this.weight);
		return modelJson;
	}

	public static Builder<?> builder() {
		return new Builder<>();
	}

	static Builder<VariantBlockStateBuilder> builder(VariantBlockStateBuilder variantBlockStateBuilder, PartialBlockstate partialBlockstate) {
		return new Builder<>(models -> variantBlockStateBuilder.setModels(partialBlockstate, models), ImmutableList.of());
	}

	public static ConfiguredModel.Builder<PartBuilder> builder(MultiPartBlockStateBuilder multiPartBlockStateBuilder) {
		return new Builder<PartBuilder>(models -> {
			PartBuilder ret = multiPartBlockStateBuilder.new PartBuilder(new SimpleBlockStateProvider.ConfiguredModelList(models));
			multiPartBlockStateBuilder.addPart(ret);
			return ret;
		}, ImmutableList.of());
	}

	public static class Builder<T> {
		private ModelFile modelFile;
		@Nullable
		private final Function<ConfiguredModel[], T> callback;
		private final List<ConfiguredModel> otherModels;
		private int rotationX;
		private int rotationY;
		private boolean uvLock;
		private int weight = DEFAULT_WEIGHT;

		private Builder() {
			this(null, ImmutableList.of());
		}

		private Builder(@Nullable Function<ConfiguredModel[], T> callback, List<ConfiguredModel> otherModels) {
			this.callback = callback;
			this.otherModels = otherModels;
		}

		public Builder<T> modelFile(ModelFile modelFile) {
			Preconditions.checkNotNull(modelFile, "Model must not be null");
			this.modelFile = modelFile;
			return this;
		}

		public Builder<T> rotationX(int rotationX) {
			checkRotation(rotationX, this.rotationY);
			this.rotationX = rotationX;
			return this;
		}

		public Builder<T> rotationY(int rotationY) {
			checkRotation(this.rotationX, rotationY);
			this.rotationY = rotationY;
			return this;
		}

		public Builder<T> uvLock(boolean uvLock) {
			this.uvLock = uvLock;
			return this;
		}

		public Builder<T> weight(int weight) {
			checkWeight(weight);
			this.weight = weight;
			return this;
		}

		public ConfiguredModel buildLast() {
			return new ConfiguredModel(this.modelFile, this.rotationX, this.rotationY, this.uvLock, this.weight);
		}

		public ConfiguredModel[] build() {
			return ObjectArrays.concat(this.otherModels.toArray(new ConfiguredModel[0]), buildLast());
		}

		public T addModel() {
			Preconditions.checkNotNull(this.callback, "Cannot use addModel() without an owning builder present");
			return this.callback.apply(build());
		}

		public Builder<T> nextModel() {
			return new Builder<>(this.callback, Arrays.asList(build()));
		}
	}
}
