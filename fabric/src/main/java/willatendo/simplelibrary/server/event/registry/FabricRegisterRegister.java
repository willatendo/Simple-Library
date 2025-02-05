package willatendo.simplelibrary.server.event.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public final class FabricRegisterRegister implements RegisterRegister {
    @Override
    public <T> void register(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation id, Supplier<T> value) {
        Registry.register((Registry<T>) BuiltInRegistries.REGISTRY.getValue(registryKey.location()), id, value.get());
    }

    @Override
    public <T extends Registry<?>> boolean sameRegistryKey(ResourceKey<T> registryKey) {
        return true;
    }
}
