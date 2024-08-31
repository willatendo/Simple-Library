package willatendo.simplelibrary.server.event;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class FabricDynamicRegistryRegister implements DynamicRegistryRegister {
    @Override
    public <T> void register(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
        DynamicRegistries.registerSynced(key, codec, codec);
    }
}
