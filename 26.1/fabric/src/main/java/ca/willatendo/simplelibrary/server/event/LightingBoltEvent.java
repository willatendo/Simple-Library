package ca.willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;

public final class LightingBoltEvent {
    public static final Event<EntityStruckByLighting> ENTITY_STRUCK_BY_LIGHTING = EventFactory.createArrayBacked(EntityStruckByLighting.class, callbacks -> (entity, lightningBolt) -> {
        for (EntityStruckByLighting callback : callbacks) {
            if (callback.onEntityStruckByLightning(entity, lightningBolt)) {
                return true;
            }
        }
        return false;
    });

    private LightingBoltEvent() {
    }

    @FunctionalInterface
    public interface EntityStruckByLighting {
        boolean onEntityStruckByLightning(Entity entity, LightningBolt lightningBolt);
    }
}
