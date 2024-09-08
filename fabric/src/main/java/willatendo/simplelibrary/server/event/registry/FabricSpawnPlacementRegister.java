package willatendo.simplelibrary.server.event.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public final class FabricSpawnPlacementRegister implements SpawnPlacementRegister {
    @Override
    public <T extends Mob> void addSpawnPlacement(EntityType<T> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        SpawnPlacements.register(entityType, spawnPlacementType, types, spawnPredicate);
    }
}
