package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.injects.BiomeExtension;
import ca.willatendo.simplelibrary.server.biome_modifier.ModifiableBiomeInfo;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class BiomeMixin implements BiomeExtension {
    @Shadow
    @Final
    private Biome.ClimateSettings climateSettings;
    @Shadow
    @Final
    private BiomeGenerationSettings generationSettings;
    @Shadow
    @Final
    private MobSpawnSettings mobSettings;
    @Shadow
    @Final
    private BiomeSpecialEffects specialEffects;
    @Final
    private ModifiableBiomeInfo modifiableBiomeInfo;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void simpleLibrary_init(CallbackInfo ci) {
        this.modifiableBiomeInfo = new ModifiableBiomeInfo(new ModifiableBiomeInfo.BiomeInfo(this.climateSettings, this.specialEffects, this.generationSettings, this.mobSettings));
    }

    @Inject(at = @At("HEAD"), method = "getMobSettings", cancellable = true)
    private void simpleLibrary_getMobSettings(CallbackInfoReturnable<MobSpawnSettings> cir) {
        cir.setReturnValue(this.modifiableBiomeInfo().get().mobSpawnSettings());
    }

    @Inject(at = @At("HEAD"), method = "getGenerationSettings", cancellable = true)
    private void simpleLibrary_getGenerationSettings(CallbackInfoReturnable<BiomeGenerationSettings> cir) {
        cir.setReturnValue(this.modifiableBiomeInfo.get().generationSettings());
    }

    @Override
    public ModifiableBiomeInfo modifiableBiomeInfo() {
        return this.modifiableBiomeInfo;
    }
}
