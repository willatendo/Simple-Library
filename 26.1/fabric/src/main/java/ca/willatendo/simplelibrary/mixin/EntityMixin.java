package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.event.EntityTickEvent;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"), method = "rideTick")
    private void rideTickPre(CallbackInfo ci) {
        EntityTickEvent.PRE.invoker().register((Entity) (Object) this);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = At.Shift.AFTER), method = "rideTick")
    private void rideTickPost(CallbackInfo ci) {
        EntityTickEvent.POST.invoker().register((Entity) (Object) this);
    }
}
