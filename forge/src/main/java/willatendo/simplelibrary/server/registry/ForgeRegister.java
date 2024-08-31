package willatendo.simplelibrary.server.registry;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegisterEvent;
import willatendo.simplelibrary.server.event.ForgeRegisterRegister;

public final class ForgeRegister {
    public static void register(IEventBus iEventBus, SimpleRegistry<?> simpleRegistry) {
        iEventBus.<RegisterEvent>addListener(registryEvent -> {
            simpleRegistry.addEntries(new ForgeRegisterRegister(registryEvent));
        });
    }
}
