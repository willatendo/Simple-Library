package willatendo.simplelibrary.server.event;

import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;

public interface SpawnPlacementRegister {
    <T extends Mob> void addSpawnPlacement(EntityType<T> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate);
}
