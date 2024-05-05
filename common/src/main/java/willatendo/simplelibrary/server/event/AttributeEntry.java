package willatendo.simplelibrary.server.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public record AttributeEntry(EntityType<? extends LivingEntity> entityType, AttributeSupplier attributeSupplier) {
}
