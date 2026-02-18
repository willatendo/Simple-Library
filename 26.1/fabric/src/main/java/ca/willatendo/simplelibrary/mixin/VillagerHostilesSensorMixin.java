package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.data_maps.SimpleLibraryDataMaps;
import ca.willatendo.simplelibrary.server.data_maps.buillt_in.AcceptableVillagerDistance;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public class VillagerHostilesSensorMixin {
    @Shadow
    @Final
    private static ImmutableMap<EntityType<?>, Float> ACCEPTABLE_DISTANCE_FROM_HOSTILES;

    @Inject(at = @At("HEAD"), method = "isClose", cancellable = true)
    private void isClose(LivingEntity attacker, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        AcceptableVillagerDistance acceptableDistanceFromHostile = (AcceptableVillagerDistance) target.getType().builtInRegistryHolder().getData(SimpleLibraryDataMaps.ACCEPTABLE_VILLAGER_DISTANCES);
        float f = acceptableDistanceFromHostile != null ? acceptableDistanceFromHostile.distance() : ACCEPTABLE_DISTANCE_FROM_HOSTILES.get(target.getType());
        cir.setReturnValue(target.distanceToSqr(attacker) <= f * f);
    }

    @Inject(at = @At("HEAD"), method = "isHostile", cancellable = true)
    private void isHostile(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        var acceptableDistanceFromHostile = (AcceptableVillagerDistance) entity.getType().builtInRegistryHolder().getData(SimpleLibraryDataMaps.ACCEPTABLE_VILLAGER_DISTANCES);
        cir.setReturnValue(acceptableDistanceFromHostile != null || ACCEPTABLE_DISTANCE_FROM_HOSTILES.containsKey(entity.getType()));
    }
}
