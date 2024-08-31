package willatendo.simplelibrary.server.event;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public final class NeoforgeDynamicRegistryRegister implements DynamicRegistryRegister {
    private final DataPackRegistryEvent.NewRegistry event;

    public NeoforgeDynamicRegistryRegister(DataPackRegistryEvent.NewRegistry event) {
        this.event = event;
    }

    @Override
    public <T> void register(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
        this.event.<T>dataPackRegistry((ResourceKey<Registry<T>>) key, codec, codec);
    }
}
