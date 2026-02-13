package ca.willatendo.simplelibrary.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.Screen;

public final class SimpleScreenEvents {
    public static final Event<ScreenClosing> CLOSING = EventFactory.createArrayBacked(ScreenClosing.class, callbacks -> screen -> {
        for (ScreenClosing callback : callbacks) {
            callback.onScreenClose(screen);
        }
    });

    private SimpleScreenEvents() {
    }

    @FunctionalInterface
    public interface ScreenClosing {
        void onScreenClose(Screen screen);
    }
}
