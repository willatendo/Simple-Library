package ca.willatendo.simplelibrary.injects;

import ca.willatendo.simplelibrary.server.data_maps.DataMapType;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

public interface RegistryExtension<T> {
    default Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> getDataMaps() {
        return Map.of();
    }

    default <A> A getData(DataMapType<T, A> type, ResourceKey<T> key) {
        final var innerMap = getDataMaps().get(type);
        return innerMap == null ? null : (A) innerMap.get(key);
    }

    default <A> Map<ResourceKey<T>, A> getDataMap(DataMapType<T, A> type) {
        return (Map<ResourceKey<T>, A>) getDataMaps().getOrDefault(type, Map.of());
    }
}
