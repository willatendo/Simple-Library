package ca.willatendo.simplelibrary.client;

import ca.willatendo.simplelibrary.network.PacketRegistryListener;

public interface ClientModInit {
    void clientEventListener(ClientEventListener clientEventListener);

    void packetRegistryListener(PacketRegistryListener packetRegistryListener);
}
