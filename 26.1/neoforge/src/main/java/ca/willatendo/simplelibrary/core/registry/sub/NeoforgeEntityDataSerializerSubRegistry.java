package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class NeoforgeEntityDataSerializerSubRegistry extends EntityDataSerializerSubRegistry {
    private final SimpleRegistry<EntityDataSerializer<?>> entityDataSerializers;

    public NeoforgeEntityDataSerializerSubRegistry(String modId) {
        super(modId);
        this.entityDataSerializers = new SimpleRegistry<>(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, modId);
    }

    public SimpleRegistry<EntityDataSerializer<?>> getSimpleRegistry() {
        return this.entityDataSerializers;
    }

    @Override
    public <T> Supplier<EntityDataSerializer<T>> register(String id, Supplier<EntityDataSerializer<T>> value) {
        return this.entityDataSerializers.register(id, value);
    }
}
