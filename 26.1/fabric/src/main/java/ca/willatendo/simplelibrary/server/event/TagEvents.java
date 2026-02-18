package ca.willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.HolderLookup;

public final class TagEvents {
    public static final Event<TagEvents.TagsUpdate> UPDATE_TAGS = EventFactory.createArrayBacked(TagEvents.TagsUpdate.class, callbacks -> (provider, fromClientPacket, isIntegratedServerConnection) -> {
        for (TagEvents.TagsUpdate callback : callbacks) {
            callback.update(provider, fromClientPacket, isIntegratedServerConnection);
        }
    });

    @FunctionalInterface
    public interface TagsUpdate {
        void update(HolderLookup.Provider provider, boolean fromClientPacket, boolean isIntegratedServerConnection);
    }

    public enum UpdateCause {
        SERVER_DATA_LOAD,
        CLIENT_PACKET_RECEIVED
    }
}
