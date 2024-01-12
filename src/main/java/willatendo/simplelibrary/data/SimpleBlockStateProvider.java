package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.WallSide;
import willatendo.simplelibrary.data.model.BlockModelProvider;
import willatendo.simplelibrary.data.model.ConfiguredModel;
import willatendo.simplelibrary.data.model.GeneratedBlockState;
import willatendo.simplelibrary.data.model.ModelFile;
import willatendo.simplelibrary.data.model.ModelProvider;
import willatendo.simplelibrary.data.model.MultiPartBlockStateBuilder;
import willatendo.simplelibrary.data.model.VariantBlockStateBuilder;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimpleBlockStateProvider implements DataProvider {
	@VisibleForTesting
	protected final Map<Block, GeneratedBlockState> registeredBlocks = new LinkedHashMap<>();

	private final FabricDataOutput fabricDataOutput;
	private final String modId;
	private final BlockModelProvider blockModels;
	private final SimpleItemModelProvider itemModels;

	public SimpleBlockStateProvider(FabricDataOutput fabricDataOutput, String modid, ExistingFileHelper existingFileHelper) {
		this.fabricDataOutput = fabricDataOutput;
		this.modId = modid;
		this.blockModels = new BlockModelProvider(fabricDataOutput, modid, existingFileHelper) {
			@Override
			public CompletableFuture<?> run(CachedOutput cache) {
				return CompletableFuture.allOf();
			}

			@Override
			protected void registerModels() {
			}
		};
		this.itemModels = new SimpleItemModelProvider(fabricDataOutput, modid, this.blockModels.existingFileHelper) {
			@Override
			protected void registerModels() {
			}

			@Override
			public CompletableFuture<?> run(CachedOutput cache) {
				return CompletableFuture.allOf();
			}
		};
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		this.models().clear();
		this.itemModels().clear();
		this.registeredBlocks.clear();
		this.registerStatesAndModels();
		CompletableFuture<?>[] completableFutures = new CompletableFuture<?>[2 + this.registeredBlocks.size()];
		int i = 0;
		completableFutures[i++] = this.models().generateAll(cachedOutput);
		completableFutures[i++] = this.itemModels().generateAll(cachedOutput);
		for (Map.Entry<Block, GeneratedBlockState> entry : this.registeredBlocks.entrySet()) {
			completableFutures[i++] = this.saveBlockState(cachedOutput, entry.getValue().toJson(), entry.getKey());
		}
		return CompletableFuture.allOf(completableFutures);
	}

	protected abstract void registerStatesAndModels();

	public VariantBlockStateBuilder getVariantBuilder(Block block) {
		if (registeredBlocks.containsKey(block)) {
			GeneratedBlockState generatedBlockState = registeredBlocks.get(block);
			Preconditions.checkState(generatedBlockState instanceof VariantBlockStateBuilder);
			return (VariantBlockStateBuilder) generatedBlockState;
		} else {
			VariantBlockStateBuilder variantBlockStateBuilder = new VariantBlockStateBuilder(block);
			this.registeredBlocks.put(block, variantBlockStateBuilder);
			return variantBlockStateBuilder;
		}
	}

	public MultiPartBlockStateBuilder getMultipartBuilder(Block b) {
		if (registeredBlocks.containsKey(b)) {
			GeneratedBlockState generatedBlockState = registeredBlocks.get(b);
			Preconditions.checkState(generatedBlockState instanceof MultiPartBlockStateBuilder);
			return (MultiPartBlockStateBuilder) generatedBlockState;
		} else {
			MultiPartBlockStateBuilder ret = new MultiPartBlockStateBuilder(b);
			this.registeredBlocks.put(b, ret);
			return ret;
		}
	}

	public BlockModelProvider models() {
		return this.blockModels;
	}

	public SimpleItemModelProvider itemModels() {
		return this.itemModels;
	}

	public ResourceLocation modLoc(String name) {
		return new ResourceLocation(this.modId, name);
	}

	public ResourceLocation mcLoc(String name) {
		return new ResourceLocation(name);
	}

	private ResourceLocation key(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	private String name(Block block) {
		return this.key(block).getPath();
	}

	public ResourceLocation blockTexture(Block block) {
		ResourceLocation name = this.key(block);
		return new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
	}

	private ResourceLocation extend(ResourceLocation rl, String suffix) {
		return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
	}

	public ModelFile cubeAll(Block block) {
		return this.models().cubeAll(this.name(block), this.blockTexture(block));
	}

	public void simpleBlock(Block block) {
		this.simpleBlock(block, cubeAll(block));
	}

	public void simpleBlock(Block block, Function<ModelFile, ConfiguredModel[]> expander) {
		this.simpleBlock(block, expander.apply(this.cubeAll(block)));
	}

	public void simpleBlock(Block block, ModelFile model) {
		this.simpleBlock(block, new ConfiguredModel(model));
	}

	public void simpleBlockItem(Block block, ModelFile model) {
		this.itemModels().getBuilder(this.key(block).getPath()).parent(model);
	}

	public void simpleBlockWithItem(Block block, ModelFile model) {
		this.simpleBlock(block, model);
		this.simpleBlockItem(block, model);
	}

	public void simpleBlock(Block block, ConfiguredModel... models) {
		this.getVariantBuilder(block).partialState().setModels(models);
	}

	public void axisBlock(RotatedPillarBlock rotatedPillarBlock) {
		this.axisBlock(rotatedPillarBlock, this.blockTexture(rotatedPillarBlock));
	}

	public void logBlock(RotatedPillarBlock rotatedPillarBlock) {
		this.axisBlock(rotatedPillarBlock, this.blockTexture(rotatedPillarBlock), this.extend(this.blockTexture(rotatedPillarBlock), "_top"));
	}

	public void axisBlock(RotatedPillarBlock rotatedPillarBlock, ResourceLocation baseName) {
		this.axisBlock(rotatedPillarBlock, this.extend(baseName, "_side"), this.extend(baseName, "_end"));
	}

	public void axisBlock(RotatedPillarBlock rotatedPillarBlock, ResourceLocation sideTexture, ResourceLocation endTexture) {
		this.axisBlock(rotatedPillarBlock, this.models().cubeColumn(this.name(rotatedPillarBlock), sideTexture, endTexture), this.models().cubeColumnHorizontal(this.name(rotatedPillarBlock) + "_horizontal", sideTexture, endTexture));
	}

	public void axisBlock(RotatedPillarBlock rotatedPillarBlock, ModelFile verticalModel, ModelFile horizontalModel) {
		this.getVariantBuilder(rotatedPillarBlock).partialState().with(RotatedPillarBlock.AXIS, Axis.Y).modelForState().modelFile(verticalModel).addModel().partialState().with(RotatedPillarBlock.AXIS, Axis.Z).modelForState().modelFile(horizontalModel).rotationX(90).addModel().partialState().with(RotatedPillarBlock.AXIS, Axis.X).modelForState().modelFile(horizontalModel).rotationX(90).rotationY(90).addModel();
	}

	private static final int DEFAULT_ANGLE_OFFSET = 180;

	public void horizontalBlock(Block block, ResourceLocation sideTexture, ResourceLocation frontTexture, ResourceLocation topTexture) {
		this.horizontalBlock(block, this.models().orientable(this.name(block), sideTexture, frontTexture, topTexture));
	}

	public void horizontalBlock(Block block, ModelFile model) {
		this.horizontalBlock(block, model, DEFAULT_ANGLE_OFFSET);
	}

	public void horizontalBlock(Block block, ModelFile model, int angleOffset) {
		this.horizontalBlock(block, blockState -> model, angleOffset);
	}

	public void horizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
		this.horizontalBlock(block, modelFunc, DEFAULT_ANGLE_OFFSET);
	}

	public void horizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
		this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(modelFunc.apply(state)).rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) % 360).build());
	}

	public void horizontalFaceBlock(Block block, ModelFile model) {
		this.horizontalFaceBlock(block, model, DEFAULT_ANGLE_OFFSET);
	}

	public void horizontalFaceBlock(Block block, ModelFile model, int angleOffset) {
		this.horizontalFaceBlock(block, blockState -> model, angleOffset);
	}

	public void horizontalFaceBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
		this.horizontalFaceBlock(block, modelFunc, DEFAULT_ANGLE_OFFSET);
	}

	public void horizontalFaceBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
		this.getVariantBuilder(block).forAllStates(blockState -> ConfiguredModel.builder().modelFile(modelFunc.apply(blockState)).rotationX(blockState.getValue(BlockStateProperties.ATTACH_FACE).ordinal() * 90).rotationY((((int) blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) + (blockState.getValue(BlockStateProperties.ATTACH_FACE) == AttachFace.CEILING ? 180 : 0)) % 360).build());
	}

	public void directionalBlock(Block block, ModelFile model) {
		this.directionalBlock(block, model, DEFAULT_ANGLE_OFFSET);
	}

	public void directionalBlock(Block block, ModelFile model, int angleOffset) {
		this.directionalBlock(block, blockState -> model, angleOffset);
	}

	public void directionalBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
		this.directionalBlock(block, modelFunc, DEFAULT_ANGLE_OFFSET);
	}

	public void directionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
		this.getVariantBuilder(block).forAllStates(blockState -> {
			Direction direction = blockState.getValue(BlockStateProperties.FACING);
			return ConfiguredModel.builder().modelFile(modelFunc.apply(blockState)).rotationX(direction == Direction.DOWN ? 180 : direction.getAxis().isHorizontal() ? 90 : 0).rotationY(direction.getAxis().isVertical() ? 0 : (((int) direction.toYRot()) + angleOffset) % 360).build();
		});
	}

	public void stairsBlock(StairBlock stairBlock, ResourceLocation texture) {
		this.stairsBlock(stairBlock, texture, texture, texture);
	}

	public void stairsBlock(StairBlock stairBlock, String name, ResourceLocation texture) {
		this.stairsBlock(stairBlock, name, texture, texture, texture);
	}

	public void stairsBlock(StairBlock stairBlock, ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
		this.stairsBlockInternal(stairBlock, this.key(stairBlock).toString(), sideTexture, bottomTexture, topTexture);
	}

	public void stairsBlock(StairBlock stairBlock, String name, ResourceLocation sidev, ResourceLocation bottomTexture, ResourceLocation topTexture) {
		this.stairsBlockInternal(stairBlock, name + "_stairs", sidev, bottomTexture, topTexture);
	}

	private void stairsBlockInternal(StairBlock stairBlock, String baseName, ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
		ModelFile stairs = models().stairs(baseName, sideTexture, bottomTexture, topTexture);
		ModelFile stairsInner = models().stairsInner(baseName + "_inner", sideTexture, bottomTexture, topTexture);
		ModelFile stairsOuter = models().stairsOuter(baseName + "_outer", sideTexture, bottomTexture, topTexture);
		this.stairsBlock(stairBlock, stairs, stairsInner, stairsOuter);
	}

	public void stairsBlock(StairBlock stairBlock, ModelFile stairsModel, ModelFile stairsInnerModel, ModelFile stairsOuterModel) {
		this.getVariantBuilder(stairBlock).forAllStatesExcept(blockState -> {
			Direction directiond = blockState.getValue(StairBlock.FACING);
			Half half = blockState.getValue(StairBlock.HALF);
			StairsShape stairsShape = blockState.getValue(StairBlock.SHAPE);
			int yRot = (int) directiond.getClockWise().toYRot();
			if (stairsShape == StairsShape.INNER_LEFT || stairsShape == StairsShape.OUTER_LEFT) {
				yRot += 270;
			}
			if (stairsShape != StairsShape.STRAIGHT && half == Half.TOP) {
				yRot += 90;
			}
			yRot %= 360;
			boolean uvlock = yRot != 0 || half == Half.TOP;
			return ConfiguredModel.builder().modelFile(stairsShape == StairsShape.STRAIGHT ? stairsModel : stairsShape == StairsShape.INNER_LEFT || stairsShape == StairsShape.INNER_RIGHT ? stairsInnerModel : stairsOuterModel).rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock).build();
		}, StairBlock.WATERLOGGED);
	}

	public void slabBlock(SlabBlock slabBlock, ResourceLocation doubleSlabTexture, ResourceLocation texture) {
		this.slabBlock(slabBlock, doubleSlabTexture, texture, texture, texture);
	}

	public void slabBlock(SlabBlock slabBlock, ResourceLocation doubleslabTexture, ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
		this.slabBlock(slabBlock, this.models().slab(this.name(slabBlock), sideTexture, bottomTexture, topTexture), this.models().slabTop(this.name(slabBlock) + "_top", sideTexture, bottomTexture, topTexture), models().getExistingFile(doubleslabTexture));
	}

	public void slabBlock(SlabBlock slabBlock, ModelFile bottomModel, ModelFile topModel, ModelFile doubleSlabModel) {
		this.getVariantBuilder(slabBlock).partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(bottomModel)).partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(new ConfiguredModel(topModel)).partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleSlabModel));
	}

	public void buttonBlock(ButtonBlock buttonBlock, ResourceLocation texture) {
		ModelFile button = models().button(name(buttonBlock), texture);
		ModelFile buttonPressed = models().buttonPressed(name(buttonBlock) + "_pressed", texture);
		this.buttonBlock(buttonBlock, button, buttonPressed);
	}

	public void buttonBlock(ButtonBlock buttonBlock, ModelFile buttonModel, ModelFile pressedButtonModel) {
		this.getVariantBuilder(buttonBlock).forAllStates(blockState -> {
			Direction facing = blockState.getValue(ButtonBlock.FACING);
			AttachFace face = blockState.getValue(ButtonBlock.FACE);
			boolean powered = blockState.getValue(ButtonBlock.POWERED);

			return ConfiguredModel.builder().modelFile(powered ? pressedButtonModel : buttonModel).rotationX(face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180)).rotationY((int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot()).uvLock(face == AttachFace.WALL).build();
		});
	}

	public void pressurePlateBlock(PressurePlateBlock pressurePlateBlock, ResourceLocation texture) {
		ModelFile pressurePlate = models().pressurePlate(name(pressurePlateBlock), texture);
		ModelFile pressurePlateDown = models().pressurePlateDown(name(pressurePlateBlock) + "_down", texture);
		this.pressurePlateBlock(pressurePlateBlock, pressurePlate, pressurePlateDown);
	}

	public void pressurePlateBlock(PressurePlateBlock pressurePlateBlock, ModelFile pressurePlateModel, ModelFile pressurePlateDownModel) {
		this.getVariantBuilder(pressurePlateBlock).partialState().with(PressurePlateBlock.POWERED, true).addModels(new ConfiguredModel(pressurePlateDownModel)).partialState().with(PressurePlateBlock.POWERED, false).addModels(new ConfiguredModel(pressurePlateModel));
	}

	public void signBlock(StandingSignBlock standingSignBlock, WallSignBlock wallSignBlock, ResourceLocation texture) {
		ModelFile signModel = models().sign(name(standingSignBlock), texture);
		this.signBlock(standingSignBlock, wallSignBlock, signModel);
	}

	public void signBlock(StandingSignBlock standingSignBlock, WallSignBlock wallSignBlock, ModelFile signModel) {
		this.simpleBlock(standingSignBlock, signModel);
		this.simpleBlock(wallSignBlock, signModel);
	}

	public void fourWayBlock(CrossCollisionBlock crossCollisionBlock, ModelFile postModel, ModelFile sideModel) {
		MultiPartBlockStateBuilder multiPartBlockStateBuilder = this.getMultipartBuilder(crossCollisionBlock).part().modelFile(postModel).addModel().end();
		this.fourWayMultipart(multiPartBlockStateBuilder, sideModel);
	}

	public void fourWayMultipart(MultiPartBlockStateBuilder multiPartBlockStateBuilder, ModelFile sideModel) {
		PipeBlock.PROPERTY_BY_DIRECTION.entrySet().forEach(entry -> {
			Direction direction = entry.getKey();
			if (direction.getAxis().isHorizontal()) {
				multiPartBlockStateBuilder.part().modelFile(sideModel).rotationY((((int) direction.toYRot()) + 180) % 360).uvLock(true).addModel().condition(entry.getValue(), true);
			}
		});
	}

	public void fenceBlock(FenceBlock fenceBlock, ResourceLocation texture) {
		String baseName = this.key(fenceBlock).toString();
		this.fourWayBlock(fenceBlock, models().fencePost(baseName + "_post", texture), this.models().fenceSide(baseName + "_side", texture));
	}

	public void fenceBlock(FenceBlock block, String name, ResourceLocation texture) {
		this.fourWayBlock(block, this.models().fencePost(name + "_fence_post", texture), this.models().fenceSide(name + "_fence_side", texture));
	}

	public void fenceGateBlock(FenceGateBlock block, ResourceLocation texture) {
		this.fenceGateBlockInternal(block, this.key(block).toString(), texture);
	}

	public void fenceGateBlock(FenceGateBlock block, String name, ResourceLocation texture) {
		this.fenceGateBlockInternal(block, name + "_fence_gate", texture);
	}

	private void fenceGateBlockInternal(FenceGateBlock block, String baseName, ResourceLocation texture) {
		ModelFile gate = this.models().fenceGate(baseName, texture);
		ModelFile gateOpen = this.models().fenceGateOpen(baseName + "_open", texture);
		ModelFile gateWall = this.models().fenceGateWall(baseName + "_wall", texture);
		ModelFile gateWallOpen = this.models().fenceGateWallOpen(baseName + "_wall_open", texture);
		this.fenceGateBlock(block, gate, gateOpen, gateWall, gateWallOpen);
	}

	public void fenceGateBlock(FenceGateBlock fenceGateBlock, ModelFile gateModel, ModelFile openGateModel, ModelFile wallGateModel, ModelFile openWallGateModel) {
		this.getVariantBuilder(fenceGateBlock).forAllStatesExcept(blockState -> {
			ModelFile modelFile = gateModel;
			if (blockState.getValue(FenceGateBlock.IN_WALL)) {
				modelFile = wallGateModel;
			}
			if (blockState.getValue(FenceGateBlock.OPEN)) {
				modelFile = modelFile == wallGateModel ? openWallGateModel : openGateModel;
			}
			return ConfiguredModel.builder().modelFile(modelFile).rotationY((int) blockState.getValue(FenceGateBlock.FACING).toYRot()).uvLock(true).build();
		}, FenceGateBlock.POWERED);
	}

	public void wallBlock(WallBlock block, ResourceLocation texture) {
		this.wallBlockInternal(block, key(block).toString(), texture);
	}

	public void wallBlock(WallBlock block, String name, ResourceLocation texture) {
		this.wallBlockInternal(block, name + "_wall", texture);
	}

	private void wallBlockInternal(WallBlock block, String baseName, ResourceLocation texture) {
		this.wallBlock(block, models().wallPost(baseName + "_post", texture), models().wallSide(baseName + "_side", texture), models().wallSideTall(baseName + "_side_tall", texture));
	}

	public static final ImmutableMap<Direction, Property<WallSide>> WALL_PROPS = ImmutableMap.<Direction, Property<WallSide>>builder().put(Direction.EAST, BlockStateProperties.EAST_WALL).put(Direction.NORTH, BlockStateProperties.NORTH_WALL).put(Direction.SOUTH, BlockStateProperties.SOUTH_WALL).put(Direction.WEST, BlockStateProperties.WEST_WALL).build();

	public void wallBlock(WallBlock wallBlock, ModelFile postModel, ModelFile sideModel, ModelFile tallSideModel) {
		MultiPartBlockStateBuilder multiPartBlockStateBuilder = getMultipartBuilder(wallBlock).part().modelFile(postModel).addModel().condition(WallBlock.UP, true).end();
		WALL_PROPS.entrySet().stream().filter(e -> e.getKey().getAxis().isHorizontal()).forEach(e -> {
			this.wallSidePart(multiPartBlockStateBuilder, sideModel, e, WallSide.LOW);
			this.wallSidePart(multiPartBlockStateBuilder, tallSideModel, e, WallSide.TALL);
		});
	}

	private void wallSidePart(MultiPartBlockStateBuilder multiPartBlockStateBuilder, ModelFile model, Map.Entry<Direction, Property<WallSide>> entry, WallSide wallSide) {
		multiPartBlockStateBuilder.part().modelFile(model).rotationY((((int) entry.getKey().toYRot()) + 180) % 360).uvLock(true).addModel().condition(entry.getValue(), wallSide);
	}

	public void paneBlock(IronBarsBlock block, ResourceLocation paneTexture, ResourceLocation edgeTexture) {
		this.paneBlockInternal(block, this.key(block).toString(), paneTexture, edgeTexture);
	}

	public void paneBlock(IronBarsBlock block, String name, ResourceLocation paneTexture, ResourceLocation edgeTexture) {
		this.paneBlockInternal(block, name + "_pane", paneTexture, edgeTexture);
	}

	private void paneBlockInternal(IronBarsBlock block, String baseName, ResourceLocation pane, ResourceLocation edge) {
		ModelFile post = this.models().panePost(baseName + "_post", pane, edge);
		ModelFile side = this.models().paneSide(baseName + "_side", pane, edge);
		ModelFile sideAlt = this.models().paneSideAlt(baseName + "_side_alt", pane, edge);
		ModelFile noSide = this.models().paneNoSide(baseName + "_noside", pane);
		ModelFile noSideAlt = this.models().paneNoSideAlt(baseName + "_noside_alt", pane);
		this.paneBlock(block, post, side, sideAlt, noSide, noSideAlt);
	}

	public void paneBlock(IronBarsBlock block, ModelFile post, ModelFile side, ModelFile sideAlt, ModelFile noSide, ModelFile noSideAlt) {
		MultiPartBlockStateBuilder builder = this.getMultipartBuilder(block).part().modelFile(post).addModel().end();
		PipeBlock.PROPERTY_BY_DIRECTION.entrySet().forEach(entry -> {
			Direction direction = entry.getKey();
			if (direction.getAxis().isHorizontal()) {
				boolean alt = direction == Direction.SOUTH;
				builder.part().modelFile(alt || direction == Direction.WEST ? sideAlt : side).rotationY(direction.getAxis() == Axis.X ? 90 : 0).addModel().condition(entry.getValue(), true).end().part().modelFile(alt || direction == Direction.EAST ? noSideAlt : noSide).rotationY(direction == Direction.WEST ? 270 : direction == Direction.SOUTH ? 90 : 0).addModel().condition(entry.getValue(), false);
			}
		});
	}

	public void doorBlock(DoorBlock doorBlock, ResourceLocation bottomTexture, ResourceLocation topTexture) {
		this.doorBlockInternal(doorBlock, this.key(doorBlock).toString(), bottomTexture, topTexture);
	}

	public void doorBlock(DoorBlock doorBlock, String name, ResourceLocation bottomTexture, ResourceLocation topTexture) {
		this.doorBlockInternal(doorBlock, name + "_door", bottomTexture, topTexture);
	}

	private void doorBlockInternal(DoorBlock doorBlock, String baseName, ResourceLocation bottomTexture, ResourceLocation topTexture) {
		ModelFile bottomLeft = models().doorBottomLeft(baseName + "_bottom_left", bottomTexture, topTexture);
		ModelFile bottomLeftOpen = models().doorBottomLeftOpen(baseName + "_bottom_left_open", bottomTexture, topTexture);
		ModelFile bottomRight = models().doorBottomRight(baseName + "_bottom_right", bottomTexture, topTexture);
		ModelFile bottomRightOpen = models().doorBottomRightOpen(baseName + "_bottom_right_open", bottomTexture, topTexture);
		ModelFile topLeft = models().doorTopLeft(baseName + "_top_left", bottomTexture, topTexture);
		ModelFile topLeftOpen = models().doorTopLeftOpen(baseName + "_top_left_open", bottomTexture, topTexture);
		ModelFile topRight = models().doorTopRight(baseName + "_top_right", bottomTexture, topTexture);
		ModelFile topRightOpen = models().doorTopRightOpen(baseName + "_top_right_open", bottomTexture, topTexture);
		doorBlock(doorBlock, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen);
	}

	public void doorBlock(DoorBlock doorBlock, ModelFile bottomLeftModel, ModelFile openBottomLeftModel, ModelFile bottomRightModel, ModelFile openBottomRightModel, ModelFile topLeftModel, ModelFile openTopLeftModel, ModelFile topRightModel, ModelFile openTopRightModel) {
		this.getVariantBuilder(doorBlock).forAllStatesExcept(blockState -> {
			int yRot = ((int) blockState.getValue(DoorBlock.FACING).toYRot()) + 90;
			boolean right = blockState.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
			boolean open = blockState.getValue(DoorBlock.OPEN);
			boolean lower = blockState.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER;
			if (open) {
				yRot += 90;
			}
			if (right && open) {
				yRot += 180;
			}
			yRot %= 360;

			ModelFile model = null;
			if (lower && right && open) {
				model = openBottomRightModel;
			} else if (lower && !right && open) {
				model = openBottomLeftModel;
			}
			if (lower && right && !open) {
				model = bottomRightModel;
			} else if (lower && !right && !open) {
				model = bottomLeftModel;
			}
			if (!lower && right && open) {
				model = openTopRightModel;
			} else if (!lower && !right && open) {
				model = openTopLeftModel;
			}
			if (!lower && right && !open) {
				model = topRightModel;
			} else if (!lower && !right && !open) {
				model = topLeftModel;
			}

			return ConfiguredModel.builder().modelFile(model).rotationY(yRot).build();
		}, DoorBlock.POWERED);
	}

	public void trapdoorBlock(TrapDoorBlock trapDoorBlock, ResourceLocation texture, boolean orientable) {
		this.trapdoorBlockInternal(trapDoorBlock, this.key(trapDoorBlock).toString(), texture, orientable);
	}

	public void trapdoorBlock(TrapDoorBlock trapDoorBlock, String name, ResourceLocation texture, boolean orientable) {
		this.trapdoorBlockInternal(trapDoorBlock, name + "_trapdoor", texture, orientable);
	}

	private void trapdoorBlockInternal(TrapDoorBlock trapDoorBlock, String baseName, ResourceLocation texture, boolean orientable) {
		ModelFile bottom = orientable ? this.models().trapdoorOrientableBottom(baseName + "_bottom", texture) : models().trapdoorBottom(baseName + "_bottom", texture);
		ModelFile top = orientable ? this.models().trapdoorOrientableTop(baseName + "_top", texture) : models().trapdoorTop(baseName + "_top", texture);
		ModelFile open = orientable ? this.models().trapdoorOrientableOpen(baseName + "_open", texture) : models().trapdoorOpen(baseName + "_open", texture);
		this.trapdoorBlock(trapDoorBlock, bottom, top, open, orientable);
	}

	public void trapdoorBlock(TrapDoorBlock trapDoorBlock, ModelFile bottomModel, ModelFile topModel, ModelFile openModel, boolean orientable) {
		this.getVariantBuilder(trapDoorBlock).forAllStatesExcept(blockState -> {
			int xRot = 0;
			int yRot = ((int) blockState.getValue(TrapDoorBlock.FACING).toYRot()) + 180;
			boolean isOpen = blockState.getValue(TrapDoorBlock.OPEN);
			if (orientable && isOpen && blockState.getValue(TrapDoorBlock.HALF) == Half.TOP) {
				xRot += 180;
				yRot += 180;
			}
			if (!orientable && !isOpen) {
				yRot = 0;
			}
			yRot %= 360;
			return ConfiguredModel.builder().modelFile(isOpen ? openModel : blockState.getValue(TrapDoorBlock.HALF) == Half.TOP ? topModel : bottomModel).rotationX(xRot).rotationY(yRot).build();
		}, TrapDoorBlock.POWERED, TrapDoorBlock.WATERLOGGED);
	}

	private CompletableFuture<?> saveBlockState(CachedOutput cachedOutput, JsonObject jsonObject, Block block) {
		ResourceLocation blockName = Preconditions.checkNotNull(this.key(block));
		Path outputPath = this.fabricDataOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(blockName.getNamespace()).resolve("blockstates").resolve(blockName.getPath() + ".json");
		return DataProvider.saveStable(cachedOutput, jsonObject, outputPath);
	}

	@NotNull
	@Override
	public String getName() {
		return this.modId + ": Block States";
	}

	public static class ConfiguredModelList {
		private final List<ConfiguredModel> models;

		private ConfiguredModelList(List<ConfiguredModel> models) {
			Preconditions.checkArgument(!models.isEmpty());
			this.models = models;
		}

		public ConfiguredModelList(ConfiguredModel model) {
			this(ImmutableList.of(model));
		}

		public ConfiguredModelList(ConfiguredModel... models) {
			this(Arrays.asList(models));
		}

		public JsonElement toJSON() {
			if (this.models.size() == 1) {
				return this.models.get(0).toJSON(false);
			} else {
				JsonArray ret = new JsonArray();
				for (ConfiguredModel m : models) {
					ret.add(m.toJSON(true));
				}
				return ret;
			}
		}

		public ConfiguredModelList append(ConfiguredModel... models) {
			return new ConfiguredModelList(ImmutableList.<ConfiguredModel>builder().addAll(this.models).add(models).build());
		}
	}
}
