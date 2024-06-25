package willatendo.simplelibrary.server.event;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EventsHolder {
    public final List<AttributeEntry> attributes = new ArrayList<AttributeEntry>();
    public final List<SpawnPlacementEntry> spawnPlacements = new ArrayList<SpawnPlacementEntry>();
    public final List<Registry<?>> registries = new ArrayList<Registry<?>>();
    public final List<ResourcePackEntry> resourcePackEntries = new ArrayList<ResourcePackEntry>();

    public <T extends Entity> void addAttribute(Supplier<EntityType<T>> entityTypeSupplier, Supplier<AttributeSupplier> attributeSupplier) {
        this.attributes.add(new AttributeEntry(entityTypeSupplier, attributeSupplier));
    }

    public <T extends Entity> void addSpawnPlacement(Supplier<EntityType<T>> entityTypeSupplier, SpawnPlacementType spawnPlacementType, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        this.spawnPlacements.add(new SpawnPlacementEntry(entityTypeSupplier, spawnPlacementType, types, spawnPredicate));
    }

    public <T> void addRegistry(Registry<T> registry) {
        this.registries.add(registry);
    }

    public <T> void addResourcePack(String modId, String resourcePackName) {
        this.resourcePackEntries.add(new ResourcePackEntry(modId, resourcePackName));
    }
}
