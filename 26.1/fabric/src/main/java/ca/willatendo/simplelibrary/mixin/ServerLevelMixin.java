package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.event.EntityTickEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"), method = "tickNonPassenger")
    private void tickNonPassengerPre(Entity entity, CallbackInfo ci) {
        EntityTickEvent.PRE.invoker().register(entity);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = At.Shift.AFTER), method = "tickNonPassenger")
    private void tickNonPassengerPost(Entity entity, CallbackInfo ci) {
        EntityTickEvent.POST.invoker().register(entity);
    }
}
