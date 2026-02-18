package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.data_maps.SimpleLibraryDataMaps;
import ca.willatendo.simplelibrary.server.data_maps.buillt_in.Compostable;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {
    @Shadow
    @Final
    public static Object2FloatMap<ItemLike> COMPOSTABLES;
    @Shadow
    @Final
    public static IntegerProperty LEVEL;

    @Inject(at = @At("HEAD"), method = "useItemOn", cancellable = true)
    private void useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        int currentLevel = blockState.getValue(LEVEL);
        if (currentLevel < 8 && ComposterBlockMixin.getValue(itemStack) > 0.0F) {
            if (currentLevel < 7 && !level.isClientSide()) {
                BlockState composterBlockState = ComposterBlock.addItem(player, blockState, level, blockPos, itemStack);
                level.levelEvent(1500, blockPos, blockState != composterBlockState ? 1 : 0);
                player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                itemStack.consume(1, player);
            }

            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Inject(at = @At("HEAD"), method = "insertItem", cancellable = true)
    private static void insertItem(Entity entity, BlockState blockStateIn, ServerLevel serverLevel, ItemStack itemStack, BlockPos blockPos, CallbackInfoReturnable<BlockState> cir) {
        int currentLevel = blockStateIn.getValue(LEVEL);
        if (currentLevel < 7 && ComposterBlockMixin.getValue(itemStack) > 0.0F) {
            BlockState composterBlockState = ComposterBlock.addItem(entity, blockStateIn, serverLevel, blockPos, itemStack);
            itemStack.shrink(1);
            cir.setReturnValue(composterBlockState);
        }
    }

    @Inject(at = @At("HEAD"), method = "addItem", cancellable = true)
    private static void addItem(Entity entity, BlockState blockStateIn, LevelAccessor levelAccessor, BlockPos blockPos, ItemStack itemStack, CallbackInfoReturnable<BlockState> cir) {
        int currentLevel = blockStateIn.getValue(LEVEL);
        float chance = ComposterBlockMixin.getValue(itemStack);
        if ((currentLevel == 0 || (chance > 0.0F)) && (levelAccessor.getRandom().nextDouble() < (double) chance)) {
            int newLevel = currentLevel + 1;
            BlockState blockState = blockStateIn.setValue(LEVEL, newLevel);
            levelAccessor.setBlock(blockPos, blockState, 3);
            levelAccessor.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(entity, blockState));
            if (newLevel == 7) {
                levelAccessor.scheduleTick(blockPos, blockStateIn.getBlock(), 20);
            }

            cir.setReturnValue(blockState);
        }
    }

    private static float getValue(ItemStack itemStack) {
        Compostable compostable = (Compostable) itemStack.getItemHolder().getData(SimpleLibraryDataMaps.COMPOSTABLES);
        if (compostable != null) {
            return compostable.chance();
        }
        return 0.0F;
    }
}
