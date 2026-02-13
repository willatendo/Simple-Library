package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.client.filter.CreativeModeTabFilter;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {
    @Inject(method = "mouseScrolled", at = @At(value = "HEAD"), cancellable = true)
    private void simpleLibrary_ScrollModification(double mouseX, double mouseY, double scrollX, double scrollY, CallbackInfoReturnable<Boolean> cir) {
        for (CreativeModeTabFilter creativeModeTabFilter : CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS) {
            if (creativeModeTabFilter.onMouseScroll(mouseX, mouseY, scrollY)) {
                cir.setReturnValue(true);
                break;
            }
        }
    }
}
