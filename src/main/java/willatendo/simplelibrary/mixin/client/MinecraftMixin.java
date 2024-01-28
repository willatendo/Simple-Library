package willatendo.simplelibrary.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.HitResult;
import willatendo.simplelibrary.client.SimpleMinecraft;

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

	@Override
	public GameConfig getGameConfig() {
		return this.gameConfig;
	}
}
