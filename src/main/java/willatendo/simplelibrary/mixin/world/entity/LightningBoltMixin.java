package willatendo.simplelibrary.mixin.world.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.WrapWithCondition;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import willatendo.simplelibrary.server.event.EntityStruckByLightningCallback;
import willatendo.simplelibrary.server.event.StruckByLightningCallback;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {
	@WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V"), method = "tick")
	private boolean event(Entity entity, ServerLevel serverLevel, LightningBolt lightningBolt) {
		EntityStruckByLightningCallback.EVENT.invoker().callback(entity, lightningBolt);
		return StruckByLightningCallback.EVENT.invoker().callback(entity, lightningBolt);
	}
}
