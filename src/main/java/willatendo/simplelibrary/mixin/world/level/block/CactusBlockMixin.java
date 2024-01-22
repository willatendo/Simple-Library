package willatendo.simplelibrary.mixin.world.level.block;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import willatendo.simplelibrary.server.block.PlantType;
import willatendo.simplelibrary.server.block.SimplePlantable;

@Mixin(BushBlock.class)
public class CactusBlockMixin implements SimplePlantable {
	@Override
	public PlantType getPlantType(BlockGetter blockGetter, BlockPos blockPos) {
		return PlantType.DESERT;
	}

	@Override
	public BlockState getPlant(BlockGetter blockGetter, BlockPos blockPos) {
		return ((Block) (Object) this).defaultBlockState();
	}
}
