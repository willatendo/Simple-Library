package willatendo.simplelibrary.config;

import willatendo.simplelibrary.config.api.ForgeConfigRegistry;

public final class ForgeConfigRegistryV3Impl implements ForgeConfigRegistry {
	@Override
	public ModConfig register(String modId, ModConfig.Type type, ConfigSpec<?> spec) {
		return new ModConfig(type, spec, modId);
	}

	@Override
	public ModConfig register(String modId, ModConfig.Type type, ConfigSpec<?> spec, String fileName) {
		return new ModConfig(type, spec, modId, fileName);
	}
}
