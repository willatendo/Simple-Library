package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.data_maps.DataMapHooks;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(HoneycombItem.class)
public class HoneycombItemMixin {
    @Inject(at = @At("HEAD"), method = "getWaxed", cancellable = true)
    private static void getWaxed(BlockState blockState, CallbackInfoReturnable<Optional<BlockState>> cir) {
        cir.setReturnValue(Optional.ofNullable(DataMapHooks.getBlockWaxed(blockState.getBlock())).map(block -> block.withPropertiesOf(blockState)));
    }
}
