package ca.willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.model.AtlasManager;

import java.util.function.Consumer;

public interface RegisterTextureAtlasesEvent {
    Event<RegisterTextureAtlasesEvent> EVENT = EventFactory.createArrayBacked(RegisterTextureAtlasesEvent.class, callbacks -> consumer -> {
        for (RegisterTextureAtlasesEvent callback : callbacks) {
            callback.register(consumer);
        }
    });

    void register(Consumer<AtlasManager.AtlasConfig> consumer);
}
