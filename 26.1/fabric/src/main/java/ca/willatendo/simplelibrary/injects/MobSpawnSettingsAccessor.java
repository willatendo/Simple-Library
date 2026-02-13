package ca.willatendo.simplelibrary.injects;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.Set;

public interface MobSpawnSettingsAccessor {
    default Set<MobCategory> getSpawnerTypes() {
        return Set.of();
    }

    default Set<EntityType<?>> getEntityTypes() {
        return Set.of();
    }
}
