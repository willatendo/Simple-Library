package willatendo.simplelibrary.server.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;

public class EventsHolder {
	private final List<AttributeEntry> attributes = new ArrayList<AttributeEntry>();
	private final List<SpawnPlacementEntry> spawnPlacements = new ArrayList<SpawnPlacementEntry>();

	public void addAttribute(EntityType<? extends LivingEntity> entityType, AttributeSupplier attributeSupplier) {
		this.attributes.add(new AttributeEntry(entityType, attributeSupplier));
	}

	public <T extends Entity> void addSpawnPlacement(EntityType<T> entityType, SpawnPlacements.Type type, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
		this.spawnPlacements.add(new SpawnPlacementEntry(entityType, type, types, spawnPredicate));
	}

	public void registerAllAttributes(Consumer<? super AttributeEntry> action) {
		this.attributes.forEach(action);
	}

	public void registerAllSpawnPlacements(Consumer<? super SpawnPlacementEntry> action) {
		this.spawnPlacements.forEach(action);
	}
}
