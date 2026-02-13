package ca.willatendo.simplelibrary.core;

import ca.willatendo.simplelibrary.platform.Platform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public record FabricPlatform() implements Platform {
    @Override
    public boolean isDevelopmentalEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
