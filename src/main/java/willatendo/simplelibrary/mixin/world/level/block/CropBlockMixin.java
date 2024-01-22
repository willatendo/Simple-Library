package willatendo.simplelibrary.mixin.world.level.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import willatendo.simplelibrary.server.block.SimplePlantable;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin {
	@Shadow
	public abstract int getAge(BlockState blockState);

	@Shadow
	public abstract int getMaxAge();

	@Shadow
	public abstract BlockState getStateForAge(int age);

	@Inject(at = @At("HEAD"), method = "randomTick")
	private void plantableCode(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo callbackInfo) {
		int i;
		if (serverLevel.getRawBrightness(blockPos, 0) >= 9 && (i = this.getAge(blockState)) < this.getMaxAge() && randomSource.nextInt((int) (25.0f / (getRevampedGrowthSpeed((Block) (Object) this, serverLevel, blockPos))) + 1) == 0) {
			serverLevel.setBlock(blockPos, this.getStateForAge(i + 1), 2);
		}
		return;
	}

	private static float getRevampedGrowthSpeed(Block block, BlockGetter blockGetter, BlockPos blockPos) {
		float speed = 1.0F;
		BlockPos blockpos = blockPos.below();

		for (int xOffset = -1; xOffset <= 1; ++xOffset) {
			for (int zOffset = -1; zOffset <= 1; ++zOffset) {
				float speedChanger = 0.0F;
				BlockState blockState = blockGetter.getBlockState(blockpos.offset(xOffset, 0, zOffset));
				if (blockState.canSustainPlant(blockGetter, blockpos.offset(xOffset, 0, zOffset), net.minecraft.core.Direction.UP, (SimplePlantable) block)) {
					speedChanger = 1.0F;
					if (blockState.isFertile(blockGetter, blockPos.offset(xOffset, 0, zOffset))) {
						speedChanger = 3.0F;
					}
				}

				if (xOffset != 0 || zOffset != 0) {
					speedChanger /= 4.0F;
				}

				speed += speedChanger;
			}
		}

		BlockPos northPos = blockPos.north();
		BlockPos southPos = blockPos.south();
		BlockPos westPos = blockPos.west();
		BlockPos eastPos = blockPos.east();
		boolean westEastAxis = blockGetter.getBlockState(westPos).is(block) || blockGetter.getBlockState(eastPos).is(block);
		boolean northEastAxis = blockGetter.getBlockState(northPos).is(block) || blockGetter.getBlockState(southPos).is(block);
		if (westEastAxis && northEastAxis) {
			speed /= 2.0F;
		} else {
			boolean flag2 = blockGetter.getBlockState(westPos.north()).is(block) || blockGetter.getBlockState(eastPos.north()).is(block) || blockGetter.getBlockState(eastPos.south()).is(block) || blockGetter.getBlockState(westPos.south()).is(block);
			if (flag2) {
				speed /= 2.0F;
			}
		}

		return speed;
	}
}
