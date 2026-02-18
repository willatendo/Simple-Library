package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.data_maps.SimpleLibraryDataMaps;
import ca.willatendo.simplelibrary.server.data_maps.buillt_in.ParrotImitation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.parrot.Parrot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Parrot.class)
public class ParrotMixin {
    @Inject(at = @At("HEAD"), method = "getImitatedSound", cancellable = true)
    private static void getImitatedSound(EntityType<?> entityType, CallbackInfoReturnable<SoundEvent> cir) {
        ParrotImitation parrotImitation = (ParrotImitation) entityType.builtInRegistryHolder().getData(SimpleLibraryDataMaps.PARROT_IMITATIONS);
        if (parrotImitation != null) {
            cir.setReturnValue(parrotImitation.sound());
        }
    }
}
