package willatendo.simplelibrary.server.event.registry;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import willatendo.simplelibrary.server.registry.ForgeRegister;
import willatendo.simplelibrary.server.registry.SimpleRegistry;

public final class ForgeSimpleRegistryRegister implements SimpleRegistryRegister {
    private final IEventBus iEventBus;

    public ForgeSimpleRegistryRegister() {
        this.iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }

    @Override
    public void register(SimpleRegistry<?> simpleRegistry) {
        ForgeRegister.register(this.iEventBus, simpleRegistry);
    }
}
