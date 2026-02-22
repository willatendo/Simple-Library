package ca.willatendo.simplelibrary.server.stats.utils;

import net.minecraft.world.inventory.RecipeBookType;

import java.util.Arrays;
import java.util.stream.Stream;

public final class RecipeBookUtils {
    private RecipeBookUtils() {
    }

    public static boolean isModded(RecipeBookType recipeBookType) {
        return recipeBookType != RecipeBookType.CRAFTING && recipeBookType != RecipeBookType.FURNACE && recipeBookType != RecipeBookType.BLAST_FURNACE && recipeBookType != RecipeBookType.SMOKER;
    }

    public static Stream<RecipeBookType> getModdedRecipeBookTypes() {
        return Arrays.stream(RecipeBookType.values()).filter(RecipeBookUtils::isModded);
    }
}
