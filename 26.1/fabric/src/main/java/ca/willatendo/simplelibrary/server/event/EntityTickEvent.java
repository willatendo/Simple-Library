package ca.willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;

public interface EntityTickEvent {
    Event<EntityTickEvent> PRE = EventFactory.createArrayBacked(EntityTickEvent.class, callbacks -> entity -> {
        for (EntityTickEvent callback : callbacks) {
            callback.register(entity);
        }
    });
    Event<EntityTickEvent> POST = EventFactory.createArrayBacked(EntityTickEvent.class, callbacks -> entity -> {
        for (EntityTickEvent callback : callbacks) {
            callback.register(entity);
        }
    });

    void register(Entity entity);
}
