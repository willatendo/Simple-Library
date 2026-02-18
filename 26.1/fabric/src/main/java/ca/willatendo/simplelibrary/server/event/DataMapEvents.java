package ca.willatendo.simplelibrary.server.event;

import ca.willatendo.simplelibrary.server.data_maps.DataMapType;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.registry.sync.DynamicRegistriesImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.Map;

public final class DataMapEvents {
    public static final Event<DataMapEvents.RegisterDataMap> REGISTER_DATA_MAPS = EventFactory.createArrayBacked(DataMapEvents.RegisterDataMap.class, callbacks -> dataMapTypes -> {
        for (DataMapEvents.RegisterDataMap callback : callbacks) {
            callback.register(dataMapTypes);
        }
    });
    public static final Event<DataMapEvents.DataMapsUpdate> UPDATE_DATA_MAPS = EventFactory.createArrayBacked(DataMapEvents.DataMapsUpdate.class, callbacks -> (registryAccess, registry, updateCause) -> {
        for (DataMapEvents.DataMapsUpdate callback : callbacks) {
            callback.update(registryAccess, registry, updateCause);
        }
    });

    private DataMapEvents() {
    }

    public static <T, R> void register(Map<ResourceKey<Registry<?>>, Map<Identifier, DataMapType<?, ?>>> attachments, DataMapType<R, T> type) {
        ResourceKey<Registry<R>> registry = type.registryKey();
        if (!registry.registry().getNamespace().equals("minecraft") && DynamicRegistriesImpl.DYNAMIC_REGISTRY_KEYS.stream().anyMatch(data -> data.equals(registry)) && type.networkCodec() != null && RegistryDataLoader.SYNCHRONIZED_REGISTRIES.stream().map(RegistryDataLoader.RegistryData::key).anyMatch(resourceKey -> resourceKey.equals(registry))) {
            throw new UnsupportedOperationException("Cannot register synced data map " + type.id() + " for datapack registry " + registry.identifier() + " that is not synced!");
        } else {
            Map<Identifier, DataMapType<?, ?>> map = attachments.computeIfAbsent((ResourceKey) registry, resourceKey -> new HashMap<>());
            if (map.containsKey(type.id())) {
                throw new IllegalArgumentException("Tried to register data map type with ID " + type.id() + " to registry " + registry.identifier() + " twice");
            } else {
                map.put(type.id(), type);
            }
        }
    }

    @FunctionalInterface
    public interface RegisterDataMap {
        void register(Map<ResourceKey<Registry<?>>, Map<Identifier, DataMapType<?, ?>>> dataMapTypes);
    }

    @FunctionalInterface
    public interface DataMapsUpdate {
        void update(RegistryAccess registryAccess, Registry<?> registry, DataMapEvents.UpdateCause updateCause);
    }

    public enum UpdateCause {
        CLIENT_SYNC,
        SERVER_RELOAD
    }
}
