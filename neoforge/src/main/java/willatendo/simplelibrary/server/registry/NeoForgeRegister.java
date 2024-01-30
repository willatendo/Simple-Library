package willatendo.simplelibrary.server.registry;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

public class NeoForgeRegister implements GenericRegister {
	private final RegisterEvent registerEvent;

	public NeoForgeRegister(RegisterEvent registerEvent) {
		this.registerEvent = registerEvent;
	}

	@Override
	public <T> void register(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation id, Supplier<T> supplier) {
		this.registerEvent.register(registryKey, registerHelper -> registerHelper.register(id, supplier.get()));
	}
}
