package willatendo.simplelibrary.server.event;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface NewRegistryRegister {
    <T> void register(Registry<T> registry, ResourceKey<Registry<T>> name);
}
