package ca.willatendo.simplelibrary.server.recipe;

import net.minecraft.world.item.crafting.RecipeMap;

public final class SyncedRecipeData {
    private static RecipeMap RECIPES = RecipeMap.EMPTY;

    private SyncedRecipeData() {
    }

    public static void addRecipes(RecipeMap recipes) {
        SyncedRecipeData.RECIPES = recipes;
    }

    public static RecipeMap getRecipes() {
        return RECIPES;
    }
}
