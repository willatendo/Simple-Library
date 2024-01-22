package willatendo.simplelibrary.mixin.world.level.block;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.GlazedTerracottaBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import willatendo.simplelibrary.server.block.PlantType;
import willatendo.simplelibrary.server.block.SimpleBlock;
import willatendo.simplelibrary.server.block.SimplePlantable;

@Mixin(Block.class)
public class BlockMixin implements SimpleBlock {
	@Override
	public boolean canSustainPlant(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction, SimplePlantable simplePlantable) {
		BlockState plantState = simplePlantable.getPlant(blockGetter, blockPos.relative(direction));
		PlantType plantType = simplePlantable.getPlantType(blockGetter, blockPos.relative(direction));

		if (plantState.getBlock() == Blocks.CACTUS) {
			return blockState.is(Blocks.CACTUS) || blockState.is(BlockTags.SAND);
		}

		if (plantState.getBlock() == Blocks.SUGAR_CANE && ((Block) (Object) this) == Blocks.SUGAR_CANE) {
			return true;
		}

		if (simplePlantable instanceof BushBlock && ((BushBlock) simplePlantable).mayPlaceOn(blockState, blockGetter, blockPos)) {
			return true;
		}

		if (PlantType.DESERT.equals(plantType)) {
			return blockState.is(BlockTags.SAND) || ((Block) (Object) this) == Blocks.TERRACOTTA || ((Block) (Object) this) instanceof GlazedTerracottaBlock;
		} else if (PlantType.NETHER.equals(plantType)) {
			return ((Block) (Object) this) == Blocks.SOUL_SAND;
		} else if (PlantType.CROP.equals(plantType)) {
			return blockState.is(Blocks.FARMLAND);
		} else if (PlantType.CAVE.equals(plantType)) {
			return blockState.isFaceSturdy(blockGetter, blockPos, Direction.UP);
		} else if (PlantType.PLAINS.equals(plantType)) {
			return blockState.is(BlockTags.DIRT) || ((Block) (Object) this) == Blocks.FARMLAND;
		} else if (PlantType.WATER.equals(plantType)) {
			return (blockState.is(Blocks.WATER) || blockState.getBlock() instanceof IceBlock) && blockGetter.getFluidState(blockPos.relative(direction)).isEmpty();
		} else if (PlantType.BEACH.equals(plantType)) {
			boolean isBeach = blockState.is(BlockTags.DIRT) || blockState.is(BlockTags.SAND);
			boolean hasWater = false;
			for (Direction directions : Direction.Plane.HORIZONTAL) {
				BlockState adjacentBlockState = blockGetter.getBlockState(blockPos.relative(directions));
				FluidState adjacentFluidState = blockGetter.getFluidState(blockPos.relative(directions));
				hasWater = hasWater || adjacentBlockState.is(Blocks.FROSTED_ICE) || adjacentFluidState.is(net.minecraft.tags.FluidTags.WATER);
				if (hasWater) {
					break;
				}
			}
			return isBeach && hasWater;
		}
		return false;
	}
}
