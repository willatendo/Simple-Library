package ca.willatendo.simplelibrary.injects;

import ca.willatendo.simplelibrary.server.data_maps.DataMapType;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

public interface RegistryLookupExtension<T> {
    default void clearData() {
        throw new RuntimeException("This has not been registered correctly! " + this.getClass());
    }

    default <A> Map<ResourceKey<T>, A> getDataMap(DataMapType<T, A> dataMapType) {
        throw new RuntimeException("This has not been registered correctly! " + this.getClass());
    }

    default <A> void putData(DataMapType<T, A> type, Map<ResourceKey<T>, A> map) {
        throw new RuntimeException("This has not been registered correctly! " + this.getClass());
    }

    default <A> A getData(DataMapType<T, A> type, ResourceKey<T> key) {
        throw new RuntimeException("This has not been registered correctly! " + this.getClass());
    }
}
