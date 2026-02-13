package ca.willatendo.simplelibrary.server.biome_modifier;

import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.Collections;
import java.util.Set;

// Modified from Neoforge
public final class MobSpawnSettingsBuilder extends MobSpawnSettings.Builder {
    private final Set<MobCategory> typesView = Collections.unmodifiableSet(this.spawners.keySet());
    private final Set<EntityType<?>> costView = Collections.unmodifiableSet(this.mobSpawnCosts.keySet());

    public MobSpawnSettingsBuilder(MobSpawnSettings mobSpawnSettings) {
        mobSpawnSettings.getSpawnerTypes().forEach(mobCategory -> this.spawners.get(mobCategory).addAll(mobSpawnSettings.getMobs(mobCategory)));
        mobSpawnSettings.getEntityTypes().forEach(entityType -> this.mobSpawnCosts.put(entityType, mobSpawnSettings.getMobSpawnCost(entityType)));
        creatureGenerationProbability = mobSpawnSettings.getCreatureProbability();
    }

    public Set<MobCategory> getSpawnerTypes() {
        return this.typesView;
    }

    public WeightedList.Builder<MobSpawnSettings.SpawnerData> getSpawner(MobCategory type) {
        return this.spawners.get(type);
    }

    public Set<EntityType<?>> getEntityTypes() {
        return this.costView;
    }

    public MobSpawnSettings.MobSpawnCost getCost(EntityType<?> type) {
        return this.mobSpawnCosts.get(type);
    }

    public float getProbability() {
        return this.creatureGenerationProbability;
    }

    public MobSpawnSettingsBuilder disablePlayerSpawn() {
        return this;
    }

    public MobSpawnSettingsBuilder removeSpawnCost(EntityType<?>... entityTypes) {
        for (EntityType<?> entityType : entityTypes) {
            this.mobSpawnCosts.remove(entityType);
        }
        return this;
    }
}
