package willatendo.simplelibrary.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.HitResult;
import willatendo.simplelibrary.config.impl.network.config.ConfigSync;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Shadow
	public LocalPlayer player;
	@Shadow
	public HitResult hitResult;

	@Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;isLocalServer:Z", shift = At.Shift.AFTER))
	private void disconnect(Screen screen, CallbackInfo callback) {
		ConfigSync.unloadSyncedConfig();
	}
}
