package ca.willatendo.simplelibrary.core;

import ca.willatendo.simplelibrary.platform.Platform;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public record NeoforgePlatform() implements Platform {
    @Override
    public boolean isDevelopmentalEnvironment() {
        return !FMLEnvironment.isProduction();
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }
}
