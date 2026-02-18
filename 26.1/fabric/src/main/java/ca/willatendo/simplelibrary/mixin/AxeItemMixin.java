package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.data_maps.SimpleLibraryDataMaps;
import ca.willatendo.simplelibrary.server.data_maps.buillt_in.Strippable;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Inject(at = @At("HEAD"), method = "getStripped", cancellable = true)
    private void simpleLibrary$getStripped(BlockState unstrippedState, CallbackInfoReturnable<Optional<BlockState>> cir) {
        Strippable strippable = (Strippable) unstrippedState.getBlock().builtInRegistryHolder().getData(SimpleLibraryDataMaps.STRIPPABLES);
        if (strippable != null) {
            cir.setReturnValue(Optional.of(strippable.strippedBlock().withPropertiesOf(unstrippedState)));
        }
    }
}
