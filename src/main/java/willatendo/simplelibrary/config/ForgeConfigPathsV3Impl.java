package willatendo.simplelibrary.config;

import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;
import willatendo.simplelibrary.config.api.ForgeConfigPaths;

public final class ForgeConfigPathsV3Impl implements ForgeConfigPaths {
	@Override
	public Path getConfigDirectory() {
		return FabricLoader.getInstance().getConfigDir();
	}

	@Override
	public Path getDefaultConfigsDirectory() {
		return FabricLoader.getInstance().getGameDir().resolve(ForgeConfigApiPortConfig.INSTANCE.<String>getValue("defaultConfigsPath"));
	}
}
