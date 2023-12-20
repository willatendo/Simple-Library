package willatendo.simplelibrary.server.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class SimpleRegistry<T> {
	private final List<RegistryHolder<T>> objects = new ArrayList<>();
	private final Registry<T> registryType;
	private final String modId;

	private SimpleRegistry(Registry<T> registryType, String modId) {
		this.registryType = registryType;
		this.modId = modId;
	}

	public static <T> SimpleRegistry<T> create(Registry<T> registryType, String modId) {
		return new SimpleRegistry<>(registryType, modId);
	}

	public List<RegistryHolder<T>> getEntries() {
		return this.objects;
	}

	public RegistryHolder<T> register(String id, Supplier<T> object) {
		RegistryHolder<T> registryHolder = new RegistryHolder<T>(object.get(), new ResourceLocation(this.modId, id));

		Registry.register(this.registryType, registryHolder.getId(), registryHolder.get());
		this.objects.add(registryHolder);
		return registryHolder;
	}
}
