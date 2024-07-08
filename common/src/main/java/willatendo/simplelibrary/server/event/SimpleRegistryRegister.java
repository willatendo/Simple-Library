package willatendo.simplelibrary.server.event;

import willatendo.simplelibrary.server.registry.SimpleRegistry;

public interface SimpleRegistryRegister {
    void register(SimpleRegistry<?> simpleRegistry);
}
