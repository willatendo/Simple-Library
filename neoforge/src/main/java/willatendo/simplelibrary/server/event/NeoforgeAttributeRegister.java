package willatendo.simplelibrary.server.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class NeoforgeAttributeRegister implements AttributeRegister {
    private final EntityAttributeCreationEvent event;

    public NeoforgeAttributeRegister(EntityAttributeCreationEvent event) {
        this.event = event;
    }

    @Override
    public <T extends LivingEntity> void register(EntityType<T> entityType, AttributeSupplier attributeSupplier) {
        this.event.put(entityType, attributeSupplier);
    }
}
