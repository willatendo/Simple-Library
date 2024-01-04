package willatendo.simplelibrary.server.registry;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;

public class RegistryHolder<T> implements Supplier<T> {
	private final T object;
	private final ResourceLocation id;

	public RegistryHolder(T object, ResourceLocation id) {
		this.object = object;
		this.id = id;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public T get() {
		return this.object;
	}
}
