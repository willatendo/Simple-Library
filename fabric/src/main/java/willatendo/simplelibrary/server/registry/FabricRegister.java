package willatendo.simplelibrary.server.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Map.Entry;
import java.util.function.Supplier;

public class FabricRegister {
	public static <T> void register(SimpleRegistry<T>... simpleRegistries) {
		for (SimpleRegistry<T> simpleRegistry : simpleRegistries) {
			for (Entry<SimpleHolder<? extends T>, Supplier<? extends T>> entry : simpleRegistry.getEntries().entrySet()) {
				Registry.register((Registry<T>) BuiltInRegistries.REGISTRY.get(simpleRegistry.getRegistryKey().location()), entry.getKey().getId(), entry.getValue().get());
				entry.getKey().bind(false);
			}
		}
	}
}
