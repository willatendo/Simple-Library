package willatendo.simplelibrary.server.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import willatendo.simplelibrary.server.registry.SimpleHolder;
import willatendo.simplelibrary.server.registry.SimpleRegistry;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class SimpleEntityTypes {
    public static final SimpleRegistry<EntityType<?>> ENTITY_TYPES = SimpleRegistry.create(Registries.ENTITY_TYPE, SimpleUtils.SIMPLE_ID);

    public static final SimpleHolder<EntityType<SimpleBoat>> SIMPLE_BOAT = ENTITY_TYPES.register("simple_boat", () -> SimpleUtils.entityTypeBuilder("simple_boat", SimpleBoat::new, MobCategory.MISC, 1.375F, 0.5625F));
    public static final SimpleHolder<EntityType<SimpleChestBoat>> SIMPLE_CHEST_BOAT = ENTITY_TYPES.register("simple_chest_boat", () -> SimpleUtils.entityTypeBuilder("simple_chest_boat", SimpleChestBoat::new, MobCategory.MISC, 1.375F, 0.5625F));
}
