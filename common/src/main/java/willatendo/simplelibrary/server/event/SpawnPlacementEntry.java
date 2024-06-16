package willatendo.simplelibrary.server.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public record SpawnPlacementEntry<T extends Entity>(EntityType<T> entityType, SpawnPlacementType spawnPlacementType,
                                                    Heightmap.Types types,
                                                    SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
}
