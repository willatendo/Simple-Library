package willatendo.simplelibrary.server.entity;

import net.minecraft.world.level.block.Blocks;
import willatendo.simplelibrary.server.SimpleRegistries;
import willatendo.simplelibrary.server.entity.variant.BoatType;
import willatendo.simplelibrary.server.registry.SimpleHolder;
import willatendo.simplelibrary.server.registry.SimpleRegistry;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class SimpleBoatTypes {
    public static final SimpleRegistry<BoatType> BOAT_TYPES = SimpleRegistry.create(SimpleRegistries.BOAT_TYPES, SimpleUtils.SIMPLE_ID);

    public static final SimpleHolder<BoatType> EXAMPLE = BOAT_TYPES.register("example", () -> new BoatType("example", false, null, null, Blocks.ACACIA_PLANKS.builtInRegistryHolder()));
}
