package willatendo.simplelibrary.config.api;

import java.nio.file.Path;

import willatendo.simplelibrary.config.ForgeConfigPathsV3Impl;

public interface ForgeConfigPaths {
	ForgeConfigPaths INSTANCE = new ForgeConfigPathsV3Impl();

	Path getConfigDirectory();

	Path getDefaultConfigsDirectory();
}
