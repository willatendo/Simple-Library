package willatendo.simplelibrary.data.model;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jetbrains.annotations.VisibleForTesting;

import com.google.common.base.Preconditions;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper.ResourceType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public abstract class ModelProvider<T extends ModelBuilder<T>> implements DataProvider {
	public static final String BLOCK_FOLDER = "block";
	public static final String ITEM_FOLDER = "item";

	public static final ResourceType TEXTURE = new ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
	public static final ResourceType MODEL = new ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");
	public static final ResourceType MODEL_WITH_EXTENSION = new ResourceType(PackType.CLIENT_RESOURCES, "", "models");

	protected final FabricDataOutput fabricDataOutput;
	protected final String modId;
	protected final String folder;
	protected final Function<ResourceLocation, T> factory;
	@VisibleForTesting
	public final Map<ResourceLocation, T> generatedModels = new HashMap<>();
	@VisibleForTesting
	public final ExistingFileHelper existingFileHelper;

	public ModelProvider(FabricDataOutput fabricDataOutput, String modId, String folder, Function<ResourceLocation, T> factory, ExistingFileHelper existingFileHelper) {
		Preconditions.checkNotNull(fabricDataOutput);
		this.fabricDataOutput = fabricDataOutput;
		Preconditions.checkNotNull(modId);
		this.modId = modId;
		Preconditions.checkNotNull(folder);
		this.folder = folder;
		Preconditions.checkNotNull(factory);
		this.factory = factory;
		Preconditions.checkNotNull(existingFileHelper);
		this.existingFileHelper = existingFileHelper;
	}

	public ModelProvider(FabricDataOutput fabricDataOutput, String modId, String folder, BiFunction<ResourceLocation, ExistingFileHelper, T> builderFromModId, ExistingFileHelper existingFileHelper) {
		this(fabricDataOutput, modId, folder, resourceLocation -> builderFromModId.apply(resourceLocation, existingFileHelper), existingFileHelper);
	}

	protected abstract void registerModels();

	public T getBuilder(String path) {
		Preconditions.checkNotNull(path, "Path must not be null");
		ResourceLocation outputLoc = extendWithFolder(path.contains(":") ? new ResourceLocation(path) : new ResourceLocation(this.modId, path));
		this.existingFileHelper.trackGenerated(outputLoc, MODEL);
		return generatedModels.computeIfAbsent(outputLoc, this.factory);
	}

	private ResourceLocation extendWithFolder(ResourceLocation resourceLocation) {
		if (resourceLocation.getPath().contains("/")) {
			return resourceLocation;
		}
		return new ResourceLocation(resourceLocation.getNamespace(), this.folder + "/" + resourceLocation.getPath());
	}

	public ResourceLocation modLoc(String name) {
		return new ResourceLocation(this.modId, name);
	}

	public ResourceLocation mcLoc(String name) {
		return new ResourceLocation(name);
	}

	public T withExistingParent(String name, String parent) {
		return this.withExistingParent(name, mcLoc(parent));
	}

	public T withExistingParent(String name, ResourceLocation parent) {
		return this.getBuilder(name).parent(getExistingFile(parent));
	}

	public T cube(String name, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west) {
		return this.withExistingParent(name, "cube").texture("down", down).texture("up", up).texture("north", north).texture("south", south).texture("east", east).texture("west", west);
	}

	private T singleTexture(String name, String parent, ResourceLocation texture) {
		return this.singleTexture(name, mcLoc(parent), texture);
	}

	public T singleTexture(String name, ResourceLocation parent, ResourceLocation texture) {
		return this.singleTexture(name, parent, "texture", texture);
	}

	private T singleTexture(String name, String parent, String textureKey, ResourceLocation texture) {
		return this.singleTexture(name, mcLoc(parent), textureKey, texture);
	}

	public T singleTexture(String name, ResourceLocation parent, String textureKey, ResourceLocation texture) {
		return this.withExistingParent(name, parent).texture(textureKey, texture);
	}

	public T cubeAll(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/cube_all", "all", texture);
	}

	public T cubeTop(String name, ResourceLocation side, ResourceLocation top) {
		return this.withExistingParent(name, BLOCK_FOLDER + "/cube_top").texture("side", side).texture("top", top);
	}

	private T sideBottomTop(String name, String parent, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
		return this.withExistingParent(name, parent).texture("side", side).texture("bottom", bottom).texture("top", top);
	}

	public T cubeBottomTop(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
		return this.sideBottomTop(name, BLOCK_FOLDER + "/cube_bottom_top", side, bottom, top);
	}

	public T cubeColumn(String name, ResourceLocation side, ResourceLocation end) {
		return this.withExistingParent(name, BLOCK_FOLDER + "/cube_column").texture("side", side).texture("end", end);
	}

	public T cubeColumnHorizontal(String name, ResourceLocation side, ResourceLocation end) {
		return this.withExistingParent(name, BLOCK_FOLDER + "/cube_column_horizontal").texture("side", side).texture("end", end);
	}

	public T orientableVertical(String name, ResourceLocation side, ResourceLocation front) {
		return this.withExistingParent(name, BLOCK_FOLDER + "/orientable_vertical").texture("side", side).texture("front", front);
	}

	public T orientableWithBottom(String name, ResourceLocation side, ResourceLocation front, ResourceLocation bottom, ResourceLocation top) {
		return this.withExistingParent(name, BLOCK_FOLDER + "/orientable_with_bottom").texture("side", side).texture("front", front).texture("bottom", bottom).texture("top", top);
	}

	public T orientable(String name, ResourceLocation side, ResourceLocation front, ResourceLocation top) {
		return this.withExistingParent(name, BLOCK_FOLDER + "/orientable").texture("side", side).texture("front", front).texture("top", top);
	}

	public T crop(String name, ResourceLocation crop) {
		return this.singleTexture(name, BLOCK_FOLDER + "/crop", "crop", crop);
	}

	public T cross(String name, ResourceLocation cross) {
		return this.singleTexture(name, BLOCK_FOLDER + "/cross", "cross", cross);
	}

	public T stairs(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
		return this.sideBottomTop(name, BLOCK_FOLDER + "/stairs", side, bottom, top);
	}

	public T stairsOuter(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
		return this.sideBottomTop(name, BLOCK_FOLDER + "/outer_stairs", side, bottom, top);
	}

	public T stairsInner(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
		return this.sideBottomTop(name, BLOCK_FOLDER + "/inner_stairs", side, bottom, top);
	}

	public T slab(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
		return this.sideBottomTop(name, BLOCK_FOLDER + "/slab", side, bottom, top);
	}

	public T slabTop(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
		return this.sideBottomTop(name, BLOCK_FOLDER + "/slab_top", side, bottom, top);
	}

	public T button(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/button", texture);
	}

	public T buttonPressed(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/button_pressed", texture);
	}

	public T buttonInventory(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/button_inventory", texture);
	}

	public T pressurePlate(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/pressure_plate_up", texture);
	}

	public T pressurePlateDown(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/pressure_plate_down", texture);
	}

	public T sign(String name, ResourceLocation texture) {
		return this.getBuilder(name).texture("particle", texture);
	}

	public T fencePost(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/fence_post", texture);
	}

	public T fenceSide(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/fence_side", texture);
	}

	public T fenceInventory(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/fence_inventory", texture);
	}

	public T fenceGate(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_fence_gate", texture);
	}

	public T fenceGateOpen(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_open", texture);
	}

	public T fenceGateWall(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_wall", texture);
	}

	public T fenceGateWallOpen(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_wall_open", texture);
	}

	public T wallPost(String name, ResourceLocation wall) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_wall_post", "wall", wall);
	}

	public T wallSide(String name, ResourceLocation wall) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_wall_side", "wall", wall);
	}

	public T wallSideTall(String name, ResourceLocation wall) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_wall_side_tall", "wall", wall);
	}

	public T wallInventory(String name, ResourceLocation wall) {
		return this.singleTexture(name, BLOCK_FOLDER + "/wall_inventory", "wall", wall);
	}

	private T pane(String name, String parent, ResourceLocation pane, ResourceLocation edge) {
		return this.withExistingParent(name, BLOCK_FOLDER + "/" + parent).texture("pane", pane).texture("edge", edge);
	}

	public T panePost(String name, ResourceLocation pane, ResourceLocation edge) {
		return this.pane(name, "template_glass_pane_post", pane, edge);
	}

	public T paneSide(String name, ResourceLocation pane, ResourceLocation edge) {
		return this.pane(name, "template_glass_pane_side", pane, edge);
	}

	public T paneSideAlt(String name, ResourceLocation pane, ResourceLocation edge) {
		return this.pane(name, "template_glass_pane_side_alt", pane, edge);
	}

	public T paneNoSide(String name, ResourceLocation pane) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_glass_pane_noside", "pane", pane);
	}

	public T paneNoSideAlt(String name, ResourceLocation pane) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_glass_pane_noside_alt", "pane", pane);
	}

	private T door(String name, String model, ResourceLocation bottom, ResourceLocation top) {
		return this.withExistingParent(name, BLOCK_FOLDER + "/" + model).texture("bottom", bottom).texture("top", top);
	}

	public T doorBottomLeft(String name, ResourceLocation bottom, ResourceLocation top) {
		return this.door(name, "door_bottom_left", bottom, top);
	}

	public T doorBottomLeftOpen(String name, ResourceLocation bottom, ResourceLocation top) {
		return this.door(name, "door_bottom_left_open", bottom, top);
	}

	public T doorBottomRight(String name, ResourceLocation bottom, ResourceLocation top) {
		return this.door(name, "door_bottom_right", bottom, top);
	}

	public T doorBottomRightOpen(String name, ResourceLocation bottom, ResourceLocation top) {
		return this.door(name, "door_bottom_right_open", bottom, top);
	}

	public T doorTopLeft(String name, ResourceLocation bottom, ResourceLocation top) {
		return this.door(name, "door_top_left", bottom, top);
	}

	public T doorTopLeftOpen(String name, ResourceLocation bottom, ResourceLocation top) {
		return this.door(name, "door_top_left_open", bottom, top);
	}

	public T doorTopRight(String name, ResourceLocation bottom, ResourceLocation top) {
		return this.door(name, "door_top_right", bottom, top);
	}

	public T doorTopRightOpen(String name, ResourceLocation bottom, ResourceLocation top) {
		return this.door(name, "door_top_right_open", bottom, top);
	}

	public T trapdoorBottom(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_bottom", texture);
	}

	public T trapdoorTop(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_top", texture);
	}

	public T trapdoorOpen(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_open", texture);
	}

	public T trapdoorOrientableBottom(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_bottom", texture);
	}

	public T trapdoorOrientableTop(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_top", texture);
	}

	public T trapdoorOrientableOpen(String name, ResourceLocation texture) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_open", texture);
	}

	public T torch(String name, ResourceLocation torch) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_torch", "torch", torch);
	}

	public T torchWall(String name, ResourceLocation torch) {
		return this.singleTexture(name, BLOCK_FOLDER + "/template_torch_wall", "torch", torch);
	}

	public T carpet(String name, ResourceLocation wool) {
		return this.singleTexture(name, BLOCK_FOLDER + "/carpet", "wool", wool);
	}

	public T nested() {
		return this.factory.apply(new ResourceLocation("dummy:dummy"));
	}

	public ModelFile.ExistingModelFile getExistingFile(ResourceLocation path) {
		ModelFile.ExistingModelFile existingModelFile = new ModelFile.ExistingModelFile(this.extendWithFolder(path), this.existingFileHelper);
		existingModelFile.assertExistence();
		return existingModelFile;
	}

	public void clear() {
		this.generatedModels.clear();
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		this.clear();
		this.registerModels();
		return this.generateAll(cachedOutput);
	}

	public CompletableFuture<?> generateAll(CachedOutput cachedOutput) {
		CompletableFuture<?>[] completableFutures = new CompletableFuture<?>[this.generatedModels.size()];
		int i = 0;

		for (T model : this.generatedModels.values()) {
			Path path = this.getPath(model);
			completableFutures[i++] = DataProvider.saveStable(cachedOutput, model.toJson(), path);
		}

		return CompletableFuture.allOf(completableFutures);
	}

	protected Path getPath(T model) {
		ResourceLocation resourceLocation = model.getResourceLocation();
		return this.fabricDataOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(resourceLocation.getNamespace()).resolve("models").resolve(resourceLocation.getPath() + ".json");
	}
}
