package willatendo.simplelibrary.data.util;

import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;

import net.fabricmc.loader.api.ModContainer;

public class ResourcePackLoader {
	public static PathPackResources createPackForMod(ModContainer modContainer) {
		return new PathPackResources(modContainer.getRoot().toString(), true, modContainer.getRootPath()) {
			@NotNull
			@Override
			protected Path resolve(@NotNull String... paths) {
//				return this.modFile.findResource(paths);

				return modContainer.getPath(paths[0]);
			}
		};
	}
}
