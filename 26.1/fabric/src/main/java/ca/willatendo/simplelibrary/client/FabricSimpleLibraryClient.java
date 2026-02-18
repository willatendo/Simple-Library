package ca.willatendo.simplelibrary.client;

import ca.willatendo.simplelibrary.network.FabricSimpleLibraryPacketRegistryListener;
import net.fabricmc.api.ClientModInitializer;

public final class FabricSimpleLibraryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricClientModInit fabricClientModInit = new FabricClientModInit();
        fabricClientModInit.packetRegistryListener(new FabricSimpleLibraryPacketRegistryListener());
        fabricClientModInit.clientEventListener(new SimpleLibraryClientEventListener());
    }
}
