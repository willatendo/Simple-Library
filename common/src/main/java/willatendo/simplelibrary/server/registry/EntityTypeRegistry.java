package willatendo.simplelibrary.server.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public final class EntityTypeRegistry extends SimpleRegistry<EntityType<?>> {
    EntityTypeRegistry(String modId) {
        super(Registries.ENTITY_TYPE, modId);
    }

    public <T extends Entity> SimpleHolder<EntityType<T>> register(String id, EntityType.Builder<T> builder) {
        return this.register(id, () -> builder.build(ResourceKey.create(this.registryKey, ResourceLocation.fromNamespaceAndPath(this.modId, id))));
    }
}
