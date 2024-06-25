package willatendo.simplelibrary.server.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

public record AttributeEntry<T extends LivingEntity>(Supplier<EntityType<T>> entityTypeSupplier, Supplier<AttributeSupplier> attributeSupplier) {
    public EntityType<T> getEntityType() {
        return this.entityTypeSupplier.get();
    }

    public AttributeSupplier getAttributeSupplier() {
        return this.attributeSupplier.get();
    }
}
