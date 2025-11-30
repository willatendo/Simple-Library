package willatendo.simplelibrary.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import willatendo.simplelibrary.client.filter.CreativeModeTabFilter;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;removed()V"))
    private void frameworkOnScreenClosed(Screen screen, CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS.forEach(creativeModeTabFilter -> creativeModeTabFilter.closedEvent(minecraft.screen));
    }
}
