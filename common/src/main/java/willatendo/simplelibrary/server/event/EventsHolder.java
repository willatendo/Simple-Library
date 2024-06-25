package willatendo.simplelibrary.server.event;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;

public class EventsHolder {
    public final List<AttributeEntry> attributes = new ArrayList<AttributeEntry>();
    public final List<SpawnPlacementEntry> spawnPlacements = new ArrayList<SpawnPlacementEntry>();
    public final List<Registry<?>> registries = new ArrayList<Registry<?>>();
    public final List<ResourcePackEntry> resourcePackEntries = new ArrayList<ResourcePackEntry>();

    public void addAttribute(EntityType<? extends LivingEntity> entityType, AttributeSupplier attributeSupplier) {
        this.attributes.add(new AttributeEntry(entityType, attributeSupplier));
    }

    public <T extends Entity> void addSpawnPlacement(EntityType<T> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        this.spawnPlacements.add(new SpawnPlacementEntry(entityType, spawnPlacementType, types, spawnPredicate));
    }

    public <T> void addRegistry(Registry<T> registry) {
        this.registries.add(registry);
    }

    public <T> void addResourcePack(String modId, String resourcePackName) {
        this.resourcePackEntries.add(new ResourcePackEntry(modId, resourcePackName));
    }
}
