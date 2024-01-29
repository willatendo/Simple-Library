package willatendo.simplelibrary.platform;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import willatendo.simplelibrary.server.util.SimpleUtils;

public interface ModloaderHelper {
	public static final ModloaderHelper INSTANCE = SimpleUtils.loadModloaderHelper(ModloaderHelper.class);

	// Internal Use

	<T> void register(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation id, Supplier<T> simpleHolder);
}
