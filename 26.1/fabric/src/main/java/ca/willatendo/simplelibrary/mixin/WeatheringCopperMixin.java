package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.data_maps.DataMapHooks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(WeatheringCopper.class)
public interface WeatheringCopperMixin {
    @Inject(at = @At("HEAD"), method = "getPrevious", cancellable = true)
    private static void getPrevious(Block block, CallbackInfoReturnable<Optional<Block>> cir) {
        cir.setReturnValue(Optional.ofNullable(DataMapHooks.getPreviousOxidizedStage(block)));
    }

    @Inject(at = @At("HEAD"), method = "getFirst", cancellable = true)
    private static void getFirst(Block blockIn, CallbackInfoReturnable<Block> cir) {
        Block blockOut = blockIn;

        for (Block block = DataMapHooks.getPreviousOxidizedStage(blockIn); block != null; block = DataMapHooks.getPreviousOxidizedStage(block)) {
            blockOut = block;
        }

        cir.setReturnValue(blockOut);
    }

    @Inject(at = @At("HEAD"), method = "getNext", cancellable = true)
    private static void getNext(Block block, CallbackInfoReturnable<Optional<Block>> cir) {
        cir.setReturnValue(Optional.ofNullable(DataMapHooks.getNextOxidizedStage(block)));
    }
}
