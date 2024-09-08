package willatendo.simplelibrary.server.event.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

public final class ForgeAttributeRegister implements AttributeRegister {
    private final EntityAttributeCreationEvent event;

    public ForgeAttributeRegister(EntityAttributeCreationEvent event) {
        this.event = event;
    }

    @Override
    public <T extends LivingEntity> void register(EntityType<T> entityType, AttributeSupplier attributeSupplier) {
        this.event.put(entityType, attributeSupplier);
    }
}
