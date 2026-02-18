package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.data_maps.SimpleLibraryDataMaps;
import ca.willatendo.simplelibrary.server.data_maps.buillt_in.VibrationFrequency;
import net.minecraft.core.Holder;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VibrationSystem.class)
public interface VibrationSystemMixin {
    @Inject(at = @At("HEAD"), method = "getGameEventFrequency", cancellable = true)
    private static void getGameEventFrequency(Holder<GameEvent> gameEvent, CallbackInfoReturnable<Integer> cir) {
        VibrationFrequency vibrationFrequency = (VibrationFrequency) gameEvent.getData(SimpleLibraryDataMaps.VIBRATION_FREQUENCIES);
        if (vibrationFrequency != null) {
            cir.setReturnValue(vibrationFrequency.frequency());
        }
    }
}
