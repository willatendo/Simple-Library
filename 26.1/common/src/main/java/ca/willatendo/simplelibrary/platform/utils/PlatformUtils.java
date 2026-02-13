package ca.willatendo.simplelibrary.platform.utils;

import ca.willatendo.simplelibrary.platform.SimpleLibraryPlatformHelper;

import java.nio.file.Path;
import java.util.ServiceLoader;

public final class PlatformUtils {
    private PlatformUtils() {
    }

    public static <T> T ofPlatformHelper(Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }

    public static boolean isDevelopmentalEnvironment() {
        return SimpleLibraryPlatformHelper.INSTANCE.getPlatform().isDevelopmentalEnvironment();
    }

    public static boolean isModLoaded(String modId) {
        return SimpleLibraryPlatformHelper.INSTANCE.getPlatform().isModLoaded(modId);
    }

    public static Path getConfigPath() {
        return SimpleLibraryPlatformHelper.INSTANCE.getPlatform().getConfigPath();
    }
}
