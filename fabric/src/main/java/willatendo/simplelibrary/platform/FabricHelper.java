package willatendo.simplelibrary.platform;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class FabricHelper implements ModloaderHelper {
	@Override
	public <T> void register(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation id, Supplier<T> simpleHolder) {
		Registry.register((Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location()), id, simpleHolder.get());
	}
}
