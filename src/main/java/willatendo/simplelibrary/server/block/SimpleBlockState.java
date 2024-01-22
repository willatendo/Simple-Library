package willatendo.simplelibrary.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface SimpleBlockState {
	private BlockState self() {
		return (BlockState) this;
	}

	default boolean canSustainPlant(BlockGetter blockGetter, BlockPos blockPos, Direction direction, SimplePlantable simplePlantable) {
		return this.self().getBlock().canSustainPlant(this.self(), blockGetter, blockPos, direction, simplePlantable);
	}

	default boolean isFertile(BlockGetter blockGetter, BlockPos blockPos) {
		return this.self().getBlock().isFertile(this.self(), blockGetter, blockPos);
	}
}
