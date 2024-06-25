package willatendo.simplelibrary.server.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public interface AttributeRegister {
    <T extends LivingEntity> void register(EntityType<T> entityType, AttributeSupplier attributeSupplier);
}
