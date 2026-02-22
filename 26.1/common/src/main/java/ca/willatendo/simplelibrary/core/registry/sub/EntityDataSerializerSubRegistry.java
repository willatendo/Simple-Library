package ca.willatendo.simplelibrary.core.registry.sub;

import com.google.common.collect.Maps;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.util.Map;
import java.util.function.Supplier;

public final class EntityDataSerializerSubRegistry {
    private final Map<String, Supplier<EntityDataSerializer<?>>> entityDataSerializers = Maps.newHashMap();
    private final String modId;

    public EntityDataSerializerSubRegistry(String modId) {
        this.modId = modId;
    }

    public Map<String, Supplier<EntityDataSerializer<?>>> getEntityDataSerializers() {
        return this.entityDataSerializers;
    }

    public String getModId() {
        return this.modId;
    }

    public <T> Supplier<EntityDataSerializer<T>> register(String id, Supplier<EntityDataSerializer<T>> value) {
        this.entityDataSerializers.put(id, (Supplier) value);
        return value;
    }
}
