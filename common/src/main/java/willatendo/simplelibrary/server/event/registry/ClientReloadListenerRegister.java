package willatendo.simplelibrary.server.event.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface ClientReloadListenerRegister {
    void register(ResourceLocation id, PreparableReloadListener preparableReloadListener);
}
