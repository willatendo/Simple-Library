package willatendo.simplelibrary.server.event.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

public final class NeoforgeClientReloadListenerRegister implements ClientReloadListenerRegister {
    private final AddClientReloadListenersEvent event;

    public NeoforgeClientReloadListenerRegister(AddClientReloadListenersEvent event) {
        this.event = event;
    }

    @Override
    public void register(ResourceLocation id, PreparableReloadListener preparableReloadListener) {
        this.event.addListener(id, preparableReloadListener);
    }
}
