package ca.willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.flag.FeatureFlagRegistry;

public interface FeatureFlagRegistryEvent {
    Event<FeatureFlagRegistryEvent> EVENT = EventFactory.createArrayBacked(FeatureFlagRegistryEvent.class, callbacks -> builder -> {
        for (FeatureFlagRegistryEvent callback : callbacks) {
            callback.register(builder);
        }
    });

    void register(FeatureFlagRegistry.Builder builder);
}
