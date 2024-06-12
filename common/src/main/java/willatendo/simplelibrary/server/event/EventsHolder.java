package willatendo.simplelibrary.server.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;

public class EventsHolder {
	private final List<AttributeEntry> attributes = new ArrayList<AttributeEntry>();
	private final List<SpawnPlacementEntry> spawnPlacements = new ArrayList<SpawnPlacementEntry>();

	public void addAttribute(EntityType<? extends LivingEntity> entityType, AttributeSupplier attributeSupplier) {
		this.attributes.add(new AttributeEntry(entityType, attributeSupplier));
	}

	public <T extends Entity> void addSpawnPlacement(EntityType<T> entityType, SpawnPlacementTypes spawnPlacementTypes, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
		this.spawnPlacements.add(new SpawnPlacementEntry(entityType, spawnPlacementTypes, types, spawnPredicate));
	}

	public void registerAllAttributes(Consumer<? super AttributeEntry> action) {
		this.attributes.forEach(action);
	}

	public void registerAllSpawnPlacements(Consumer<? super SpawnPlacementEntry> action) {
		this.spawnPlacements.forEach(action);
	}
}
