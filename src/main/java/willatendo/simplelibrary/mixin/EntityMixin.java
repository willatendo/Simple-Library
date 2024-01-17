package willatendo.simplelibrary.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import willatendo.simplelibrary.server.event.ForgeEvents;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(at = @At("HEAD"), method = "thunderHit")
	private void event(ServerLevel serverLevel, LightningBolt lightningBolt, CallbackInfo callbackInfo) {
		if (!ForgeEvents.ENTITY_STRUCK_BY_LIGHTNING.invoker().lightning((Entity) (Object) this, lightningBolt)) {
			callbackInfo.cancel();
		} else {
			return;
		}
	}
}
