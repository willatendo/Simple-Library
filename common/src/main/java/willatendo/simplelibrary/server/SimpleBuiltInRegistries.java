package willatendo.simplelibrary.server;

import net.minecraft.core.Registry;
import willatendo.simplelibrary.server.entity.variant.BoatType;
import willatendo.simplelibrary.server.util.SimpleRegistryBuilder;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class SimpleBuiltInRegistries {
    public static final Registry<BoatType> BOAT_TYPES = SimpleUtils.createRegistry(SimpleRegistries.BOAT_TYPES, SimpleRegistryBuilder.of().sync());

    public static void init() {
    }
}
