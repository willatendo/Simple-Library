package ca.willatendo.simplelibrary.server;

import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.EntityDataSerializerSubRegistry;
import ca.willatendo.simplelibrary.network.PacketRegistryListener;

public interface ModInit {
    void register(SimpleRegistry<?>... simpleRegistry);

    void register(EntityDataSerializerSubRegistry entityDataSerializerSubRegistry);

    void eventListener(EventListener eventListener);

    void packetRegistryListener(PacketRegistryListener packetRegistryListener);
}
