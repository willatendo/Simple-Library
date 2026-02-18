package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.MonsterRoomHooks;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MonsterRoomFeature.class)
public class MonsterRoomFeatureMixin {
    @Inject(at = @At("HEAD"), method = "randomEntityId", cancellable = true)
    private void randomEntityId(RandomSource randomSource, CallbackInfoReturnable<EntityType<?>> cir) {
        cir.setReturnValue(MonsterRoomHooks.getRandomMonsterRoomMob(randomSource));
    }
}
