package willatendo.simplelibrary.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface SimpleBlock {
//	private Block self() {
//		return (Block) this;
//	}

	boolean canSustainPlant(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction, SimplePlantable simplePlantable);

	default boolean isFertile(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		if (blockState.is(Blocks.FARMLAND)) {
			return blockState.getValue(FarmBlock.MOISTURE) > 0;
		}

		return false;
	}
}
