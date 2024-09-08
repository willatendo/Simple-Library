package willatendo.simplelibrary.server.event.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface DynamicRegistryRegister {
    <T> void register(ResourceKey<? extends Registry<T>> key, Codec<T> codec);
}
