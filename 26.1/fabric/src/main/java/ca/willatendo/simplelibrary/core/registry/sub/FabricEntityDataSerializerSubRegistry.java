package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.util.function.Supplier;

public final class FabricEntityDataSerializerSubRegistry extends EntityDataSerializerSubRegistry {
    public FabricEntityDataSerializerSubRegistry(String modId) {
        super(modId);
    }

    @Override
    public <T> Supplier<EntityDataSerializer<T>> register(String id, Supplier<EntityDataSerializer<T>> value) {
        EntityDataSerializer<T> entityDataSerializer = value.get();
        FabricTrackedDataRegistry.register(CoreUtils.resource(this.getModId(), id), entityDataSerializer);
        return () -> entityDataSerializer;
    }
}
