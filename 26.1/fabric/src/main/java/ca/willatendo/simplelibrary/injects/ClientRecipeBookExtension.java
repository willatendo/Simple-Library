package ca.willatendo.simplelibrary.injects;

import ca.willatendo.simplelibrary.server.stats.CustomRecipeBookSettings;

public interface ClientRecipeBookExtension {
    default void setCustomBookSettings(CustomRecipeBookSettings customRecipeBookSettings) {
        throw new RuntimeException("This has not been registered correctly! " + this.getClass());
    }
}
