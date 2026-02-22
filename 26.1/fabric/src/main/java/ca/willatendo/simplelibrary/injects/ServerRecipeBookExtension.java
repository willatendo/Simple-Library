package ca.willatendo.simplelibrary.injects;

import ca.willatendo.simplelibrary.server.stats.CustomRecipeBookSettings;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.function.Predicate;

public interface ServerRecipeBookExtension {
    default void loadCustomUntrusted(CustomRecipeBookSettings customRecipeBookSettings, Predicate<ResourceKey<Recipe<?>>> predicate) {
        throw new RuntimeException("This has not been registered correctly! " + this.getClass());
    }
}
