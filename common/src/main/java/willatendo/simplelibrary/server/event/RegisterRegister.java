package willatendo.simplelibrary.server.event;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface RegisterRegister {
    <T> void register(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation id, Supplier<T> value);

    <T extends Registry<?>> boolean sameRegistryKey(ResourceKey<T> registryKey);
}
