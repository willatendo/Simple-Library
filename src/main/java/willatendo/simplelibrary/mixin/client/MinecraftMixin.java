package willatendo.simplelibrary.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import willatendo.simplelibrary.config.impl.network.config.ConfigSync;

@Mixin(Minecraft.class)
abstract class MinecraftMixin {
	@Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;isLocalServer:Z", shift = At.Shift.AFTER))
	public void disconnect(Screen screen, CallbackInfo callback) {
		ConfigSync.unloadSyncedConfig();
	}
}
