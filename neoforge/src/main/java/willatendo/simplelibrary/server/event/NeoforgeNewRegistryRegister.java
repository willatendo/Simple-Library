package willatendo.simplelibrary.server.event;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.NewRegistryEvent;

public class NeoforgeNewRegistryRegister implements NewRegistryRegister {
    private final NewRegistryEvent event;

    public NeoforgeNewRegistryRegister(NewRegistryEvent event) {
        this.event = event;
    }

    @Override
    public <T> void register(Registry<T> registry) {
        this.event.register(registry);
    }
}
