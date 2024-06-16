package willatendo.simplelibrary.server.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Map.Entry;
import java.util.function.Supplier;

public final class NeoForgeRegister {
	public static <T> void register(RegisterEvent registerEvent, SimpleRegistry<? extends T>... simpleRegistries) {
		for (SimpleRegistry<? extends T> simpleRegistry : simpleRegistries) {
			NeoForgeRegister.register(registerEvent, simpleRegistry);
		}
	}

	public static <T> void register(RegisterEvent registerEvent, SimpleRegistry<T> simpleRegistry) {
		ResourceKey<? extends Registry<T>> resourceKey = simpleRegistry.getRegistryKey();
		if (!registerEvent.getRegistryKey().equals(resourceKey)) {
			return;
		}
		for (Entry<SimpleHolder<? extends T>, Supplier<? extends T>> entry : simpleRegistry.getEntries().entrySet()) {
			registerEvent.register(resourceKey, entry.getKey().getId(), () -> entry.getValue().get());
			entry.getKey().bind(false);
		}
	}
}
