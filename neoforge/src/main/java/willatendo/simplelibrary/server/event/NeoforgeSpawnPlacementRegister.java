package willatendo.simplelibrary.server.event;

import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;

public class NeoforgeSpawnPlacementRegister implements SpawnPlacementRegister {
    private final SpawnPlacementRegisterEvent event;

    public NeoforgeSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {
        this.event = event;
    }

    @Override
    public <T extends Mob> void addSpawnPlacement(EntityType<T> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        this.event.register(entityType, spawnPlacementType, types, spawnPredicate, SpawnPlacementRegisterEvent.Operation.OR);
    }
}
