package willatendo.simplelibrary.server.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public final class NeoforgeSpawnPlacementRegister implements SpawnPlacementRegister {
    private final RegisterSpawnPlacementsEvent event;

    public NeoforgeSpawnPlacementRegister(RegisterSpawnPlacementsEvent event) {
        this.event = event;
    }

    @Override
    public <T extends Mob> void addSpawnPlacement(EntityType<T> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        this.event.register(entityType, spawnPlacementType, types, spawnPredicate, RegisterSpawnPlacementsEvent.Operation.OR);
    }
}
