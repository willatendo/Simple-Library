package ca.willatendo.simplelibrary.server.event;

import ca.willatendo.simplelibrary.server.conditions.ICondition;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.List;

public final class AddReloadListenersEvent {
    public static final Event<AddReloadListenersCallback> EVENT = EventFactory.createArrayBacked(AddReloadListenersCallback.class, callbacks -> (preparableReloadListeners, context, reloadableServerResources) -> {
        for (AddReloadListenersCallback callback : callbacks) {
            callback.onAddReloadListeners(preparableReloadListeners, context, reloadableServerResources);
        }
    });

    public interface AddReloadListenersCallback {
        void onAddReloadListeners(List<PreparableReloadListener> preparableReloadListeners, ICondition.IContext context, ReloadableServerResources reloadableServerResources);
    }
}
