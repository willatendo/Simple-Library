package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.platform.SimpleLibraryPlatformHelper;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.util.function.Supplier;

public abstract class EntityDataSerializerSubRegistry {
    private final String modId;

    public static EntityDataSerializerSubRegistry create(String modId) {
        return SimpleLibraryPlatformHelper.INSTANCE.createEntityDataSerializerSubRegistry(modId);
    }

    protected EntityDataSerializerSubRegistry(String modId) {
        this.modId = modId;
    }

    public String getModId() {
        return this.modId;
    }

    public abstract <T> Supplier<EntityDataSerializer<T>> register(String id, Supplier<EntityDataSerializer<T>> value);
}
