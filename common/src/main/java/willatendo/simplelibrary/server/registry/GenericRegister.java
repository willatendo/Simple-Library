package willatendo.simplelibrary.server.registry;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface GenericRegister {
	<T> void register(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation id, Supplier<T> supplier);
}
