package willatendo.simplelibrary.server.event.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NewRegistryEvent;

public final class NeoforgeNewRegistryRegister implements NewRegistryRegister {
    private final NewRegistryEvent event;

    public NeoforgeNewRegistryRegister(NewRegistryEvent event) {
        this.event = event;
    }

    @Override
    @Deprecated
    public <T> void register(Registry<T> registry, ResourceKey<Registry<T>> name) {
        this.event.register(registry);
    }

    public <T> void register(Registry<T> registry) {
        this.register(registry, null);
    }
}
