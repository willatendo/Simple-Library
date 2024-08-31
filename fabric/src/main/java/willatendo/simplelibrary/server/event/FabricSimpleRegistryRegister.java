package willatendo.simplelibrary.server.event;

import willatendo.simplelibrary.server.registry.SimpleRegistry;

public final class FabricSimpleRegistryRegister implements SimpleRegistryRegister {
    @Override
    public void register(SimpleRegistry<?> simpleRegistry) {
        simpleRegistry.addEntries(new FabricRegisterRegister());
    }
}
