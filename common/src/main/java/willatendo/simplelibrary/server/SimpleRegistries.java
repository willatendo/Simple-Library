package willatendo.simplelibrary.server;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import willatendo.simplelibrary.server.entity.variant.BoatType;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class SimpleRegistries {
    public static final ResourceKey<Registry<BoatType>> BOAT_TYPES = createRegistryKey("boat_types");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String id) {
        return ResourceKey.createRegistryKey(SimpleUtils.simple(id));
    }
}
