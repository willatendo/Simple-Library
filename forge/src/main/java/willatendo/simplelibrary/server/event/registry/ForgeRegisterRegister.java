package willatendo.simplelibrary.server.event.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;

import java.util.function.Supplier;

public final class ForgeRegisterRegister implements RegisterRegister {
    private final RegisterEvent registerEvent;

    public ForgeRegisterRegister(RegisterEvent registerEvent) {
        this.registerEvent = registerEvent;
    }

    @Override
    public <T> void register(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation id, Supplier<T> value) {
        this.registerEvent.register(registryKey, id, value);
    }

    @Override
    public <T extends Registry<?>> boolean sameRegistryKey(ResourceKey<T> registryKey) {
        return this.registerEvent.getRegistryKey().equals(registryKey);
    }
}
