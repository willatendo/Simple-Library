package ca.willatendo.simplelibrary.injects;

import ca.willatendo.simplelibrary.server.stats.CustomRecipeBookSettings;

public interface RecipeBookExtension {
    default CustomRecipeBookSettings getCustomRecipeBookSettings() {
        throw new RuntimeException("This has not been registered correctly! " + this.getClass());
    }
}
