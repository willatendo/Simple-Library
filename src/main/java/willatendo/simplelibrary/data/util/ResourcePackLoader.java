package willatendo.simplelibrary.data.util;

import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class ResourcePackLoader {
	public static PathPackResources createPackForMod(ModContainer modContainer) {
		return new PathPackResources(modContainer.getMetadata().getName(), false, modContainer.getRootPath()) {
			@NotNull
			@Override
			protected Path resolve(@NotNull String... paths) {
				if (paths.length < 1) {
					throw new IllegalArgumentException("Missing path");
				}
				String path = String.join("/", paths);
				for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
					if (modContainer.findPath(path).isPresent()) {
						return modContainer.findPath(path).get();
					}
				}

				return modContainer.findPath(path).orElseThrow();
			}
		};
	}
}
