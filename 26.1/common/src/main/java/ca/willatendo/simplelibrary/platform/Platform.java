package ca.willatendo.simplelibrary.platform;

import java.nio.file.Path;

public interface Platform {
    boolean isDevelopmentalEnvironment();

    boolean isModLoaded(String modId);

    Path getConfigPath();
}
