package willatendo.simplelibrary.mixin.world.level.block;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import willatendo.simplelibrary.server.block.SimplePlantable;

@Mixin(BambooStalkBlock.class)
public class BambooStalkBlockMixin implements SimplePlantable {
	@Override
	public BlockState getPlant(BlockGetter blockGetter, BlockPos blockPos) {
		BlockState blockState = blockGetter.getBlockState(blockPos);
		if (blockState.getBlock() != ((Block) (Object) this)) {
			return ((Block) (Object) this).defaultBlockState();
		}
		return blockState;
	}
}
