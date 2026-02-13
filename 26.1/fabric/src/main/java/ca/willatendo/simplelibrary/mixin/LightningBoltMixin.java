package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.event.LightingBoltEvent;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {
    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V"))
    private boolean simpleLibrary_lightningStrike(Entity entity, ServerLevel serverLevel, LightningBolt lightningBolt) {
        return LightingBoltEvent.ENTITY_STRUCK_BY_LIGHTING.invoker().onEntityStruckByLightning(entity, lightningBolt);
    }

}
