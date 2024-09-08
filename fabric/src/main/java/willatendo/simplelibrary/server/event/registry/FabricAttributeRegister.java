package willatendo.simplelibrary.server.event.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public final class FabricAttributeRegister implements AttributeRegister {
    @Override
    public <T extends LivingEntity> void register(EntityType<T> entityType, AttributeSupplier attributeSupplier) {
        FabricDefaultAttributeRegistry.register(entityType, attributeSupplier);
    }
}
