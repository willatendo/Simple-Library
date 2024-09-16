package willatendo.simplelibrary.server.event.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;

public final class ForgeClientReloadListenerRegister implements ClientReloadListenerRegister {
    private final RegisterClientReloadListenersEvent event;

    public ForgeClientReloadListenerRegister(RegisterClientReloadListenersEvent event) {
        this.event = event;
    }

    @Override
    public void register(ResourceLocation id, PreparableReloadListener preparableReloadListener) {
        this.event.registerReloadListener(preparableReloadListener);
    }
}
