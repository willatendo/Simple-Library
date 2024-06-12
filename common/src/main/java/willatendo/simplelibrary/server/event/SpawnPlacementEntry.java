package willatendo.simplelibrary.server.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public record SpawnPlacementEntry<T extends Entity>(EntityType<T> entityType, SpawnPlacementTypes spawnPlacementTypes, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
}
