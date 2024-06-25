package willatendo.simplelibrary.server.event;

import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Supplier;

public record SpawnPlacementEntry<T extends Entity>(Supplier<EntityType<T>> entityTypeSupplier, SpawnPlacementType spawnPlacementType, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
    public EntityType<T> getEntityType() {
        return this.entityTypeSupplier.get();
    }
}
