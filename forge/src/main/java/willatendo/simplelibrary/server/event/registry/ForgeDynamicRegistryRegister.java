package willatendo.simplelibrary.server.event.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DataPackRegistryEvent;

public final class ForgeDynamicRegistryRegister implements DynamicRegistryRegister {
    private final DataPackRegistryEvent.NewRegistry event;

    public ForgeDynamicRegistryRegister(DataPackRegistryEvent.NewRegistry event) {
        this.event = event;
    }

    @Override
    public <T> void register(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
        this.event.<T>dataPackRegistry((ResourceKey<Registry<T>>) key, codec, codec);
    }
}
