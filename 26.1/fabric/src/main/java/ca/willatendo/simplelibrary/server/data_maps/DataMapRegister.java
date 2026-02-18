package ca.willatendo.simplelibrary.server.data_maps;

import ca.willatendo.simplelibrary.server.event.DataMapEvents;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public final class DataMapRegister {
    private static Map<ResourceKey<Registry<?>>, Map<Identifier, DataMapType<?, ?>>> dataMaps = Map.of();

    public static <R> DataMapType<R, ?> getDataMap(ResourceKey<? extends Registry<R>> registry, Identifier key) {
        final var map = dataMaps.get(registry);
        return map == null ? null : (DataMapType<R, ?>) map.get(key);
    }

    public static Map<ResourceKey<Registry<?>>, Map<Identifier, DataMapType<?, ?>>> getDataMaps() {
        return dataMaps;
    }

    public static void initDataMaps() {
        final Map<ResourceKey<Registry<?>>, Map<Identifier, DataMapType<?, ?>>> dataMapTypes = new HashMap<>();
        DataMapEvents.REGISTER_DATA_MAPS.invoker().register(dataMapTypes);
        dataMaps = new IdentityHashMap<>();
        dataMapTypes.forEach((key, values) -> dataMaps.put(key, Collections.unmodifiableMap(values)));
        dataMaps = Collections.unmodifiableMap(dataMapTypes);
    }
}
