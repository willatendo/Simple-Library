package willatendo.simplelibrary.server;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class ModUtils {
	private static String id;

	public ModUtils(String id) {
		this.id = id;
	}

	public static ResourceLocation resource(String path) {
		return new ResourceLocation(id, path);
	}

	public static MutableComponent translation(String type, String name) {
		return Component.translatable(type + "." + id + "." + name);
	}

	public static MutableComponent translation(String type, String name, Object... args) {
		return Component.translatable(type + "." + id + "." + name, args);
	}
}
