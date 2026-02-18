package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.injects.RegistryLookupExtension;
import ca.willatendo.simplelibrary.server.data_maps.DataMapType;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements RegistryLookupExtension<T> {
    private final Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> dataMaps = new IdentityHashMap<>();

    @Override
    public void clearData() {
        this.dataMaps.clear();
    }

    @Override
    public <A> A getData(DataMapType<T, A> type, ResourceKey<T> key) {
        Map<ResourceKey<T>, ?> innerMap = this.dataMaps.get(type);
        return innerMap == null ? null : (A) innerMap.get(key);
    }

    @Override
    public <A> Map<ResourceKey<T>, A> getDataMap(DataMapType<T, A> dataMapType) {
        return (Map) this.dataMaps.getOrDefault(dataMapType, Map.of());
    }

    @Override
    public <A> void putData(DataMapType<T, A> type, Map<ResourceKey<T>, A> map) {
        this.dataMaps.put(type, map);
    }

    public Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> getDataMaps() {
        return this.dataMaps;
    }
}
