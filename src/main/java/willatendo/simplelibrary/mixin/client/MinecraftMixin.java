package willatendo.simplelibrary.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.HitResult;
import willatendo.simplelibrary.client.SimpleMinecraft;
import willatendo.simplelibrary.config.impl.network.config.ConfigSync;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements SimpleMinecraft {
	@Shadow
	public LocalPlayer player;
	@Shadow
	public HitResult hitResult;

	private GameConfig gameConfig;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getNanos()J"))
	private void gameConfigAccess(GameConfig gameConfig, CallbackInfo callback) {
		this.gameConfig = gameConfig;
	}

	@Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;isLocalServer:Z", shift = At.Shift.AFTER))
	private void disconnect(Screen screen, CallbackInfo callback) {
		ConfigSync.unloadSyncedConfig();
	}

	@Override
	public GameConfig getGameConfig() {
		return this.gameConfig;
	}
}
