package willatendo.simplelibrary.server.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;
import willatendo.simplelibrary.server.event.registry.NeoforgeRegisterRegister;

public final class NeoForgeRegister {
    public static void register(IEventBus iEventBus, SimpleRegistry<?> simpleRegistry) {
        iEventBus.<RegisterEvent>addListener(registryEvent -> {
            simpleRegistry.addEntries(new NeoforgeRegisterRegister(registryEvent));
        });
    }
}
