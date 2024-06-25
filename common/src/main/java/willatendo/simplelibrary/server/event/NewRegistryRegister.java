package willatendo.simplelibrary.server.event;

import net.minecraft.core.Registry;

public interface NewRegistryRegister {
    <T> void register(Registry<T> registry);
}
