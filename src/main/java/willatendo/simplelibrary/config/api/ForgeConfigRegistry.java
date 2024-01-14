package willatendo.simplelibrary.config.api;

import willatendo.simplelibrary.config.ConfigSpec;
import willatendo.simplelibrary.config.ForgeConfigRegistryV3Impl;
import willatendo.simplelibrary.config.ModConfig;

public interface ForgeConfigRegistry {
	ForgeConfigRegistry INSTANCE = new ForgeConfigRegistryV3Impl();

	ModConfig register(String modId, ModConfig.Type type, ConfigSpec<?> spec);

	ModConfig register(String modId, ModConfig.Type type, ConfigSpec<?> spec, String fileName);
}
