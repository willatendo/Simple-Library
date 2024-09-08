package willatendo.simplelibrary.server.event.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;

public final class ForgeSpawnPlacementRegister implements SpawnPlacementRegister {
    private final SpawnPlacementRegisterEvent event;

    public ForgeSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {
        this.event = event;
    }

    @Override
    public <T extends Mob> void addSpawnPlacement(EntityType<T> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        this.event.register(entityType, spawnPlacementType, types, spawnPredicate, SpawnPlacementRegisterEvent.Operation.OR);
    }
}
