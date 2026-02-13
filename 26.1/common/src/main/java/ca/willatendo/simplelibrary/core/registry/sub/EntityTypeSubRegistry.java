package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import ca.willatendo.simplelibrary.server.utils.ServerUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class EntityTypeSubRegistry extends SimpleRegistry<EntityType<?>> {
    public EntityTypeSubRegistry(String modId) {
        super(Registries.ENTITY_TYPE, modId);
    }

    public SimpleHolder<EntityType<Boat>> registerBoat(String name, Supplier<Item> boatItem) {
        SimpleHolder<EntityType<Boat>> boat = this.register(name, ServerUtils.<Boat>simpleEntityType((entityType, level) -> new Boat(entityType, level, boatItem), MobCategory.MISC, 1.375F, 0.5625F).noLootTable().eyeHeight(0.5625F).clientTrackingRange(10));
        return boat;
    }

    public SimpleHolder<EntityType<ChestBoat>> registerChestBoat(String name, Supplier<Item> boatItem) {
        return this.register(name, ServerUtils.<ChestBoat>simpleEntityType((entityType, level) -> new ChestBoat(entityType, level, boatItem), MobCategory.MISC, 1.375F, 0.5625F).noLootTable().eyeHeight(0.5625F).clientTrackingRange(10));
    }


    public <T extends Entity> SimpleHolder<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return this.register(name, () -> builder.build(ResourceKey.create(this.registryKey, CoreUtils.resource(this.modId, name))));
    }
}
