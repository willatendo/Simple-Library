package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.injects.MobSpawnSettingsExtension;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Mixin(MobSpawnSettings.class)
public class MobSpawnSettingsMixin implements MobSpawnSettingsExtension {
    @Shadow
    @Final
    private Map<MobCategory, WeightedList<MobSpawnSettings.SpawnerData>> spawners;
    @Shadow
    @Final
    private Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> mobSpawnCosts;
    private Set<MobCategory> typesView;
    private Set<EntityType<?>> costView;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void simpleLibrary_init(float creatureGenerationProbability, Map<MobCategory, WeightedList<MobSpawnSettings.SpawnerData>> spawners, Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> mobSpawnCosts, CallbackInfo ci) {
        this.typesView = Collections.unmodifiableSet(this.spawners.keySet());
        this.costView = Collections.unmodifiableSet(this.mobSpawnCosts.keySet());
    }

    @Override
    public Set<MobCategory> getSpawnerTypes() {
        return this.typesView;
    }

    @Override
    public Set<EntityType<?>> getEntityTypes() {
        return this.costView;
    }
}
