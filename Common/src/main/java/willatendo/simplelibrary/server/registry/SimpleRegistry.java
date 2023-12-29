package willatendo.simplelibrary.server.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import willatendo.simplelibrary.helper.ModloaderHelper;

public class SimpleRegistry<T> {
	private final List<RegistryHolder<? extends T>> objects = new ArrayList<>();
	private final Registry<T> registryType;
	private final String modId;

	private SimpleRegistry(Registry<T> registryType, String modId) {
		this.registryType = registryType;
		this.modId = modId;
	}

	public static <T> SimpleRegistry<T> create(Registry<T> registryType, String modId) {
		return new SimpleRegistry<>(registryType, modId);
	}

	public List<RegistryHolder<? extends T>> getEntries() {
		return this.objects;
	}

	public <I extends T> RegistryHolder<I> register(String id, Supplier<I> object) {
		RegistryHolder<I> registryHolder = new RegistryHolder<I>(object.get(), new ResourceLocation(this.modId, id));

		ModloaderHelper.getInstance().register(this.registryType, registryHolder.getId(), () -> registryHolder.get());
		this.objects.add(registryHolder);
		return registryHolder;
	}
}
