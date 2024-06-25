package willatendo.simplelibrary.server.event;

import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;

public class FabricSpawnPlacementRegister implements SpawnPlacementRegister {
    @Override
    public <T extends Mob> void addSpawnPlacement(EntityType<T> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        SpawnPlacements.register(entityType, spawnPlacementType, types, spawnPredicate);
    }
}
