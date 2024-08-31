package willatendo.simplelibrary.server.event;

import net.neoforged.bus.api.IEventBus;
import willatendo.simplelibrary.server.registry.NeoForgeRegister;
import willatendo.simplelibrary.server.registry.SimpleRegistry;

public final class NeoforgeSimpleRegistryRegister implements SimpleRegistryRegister {
    private final IEventBus iEventBus;

    public NeoforgeSimpleRegistryRegister(IEventBus iEventBus) {
        this.iEventBus = iEventBus;
    }

    @Override
    public void register(SimpleRegistry<?> simpleRegistry) {
        NeoForgeRegister.register(this.iEventBus, simpleRegistry);
    }
}
