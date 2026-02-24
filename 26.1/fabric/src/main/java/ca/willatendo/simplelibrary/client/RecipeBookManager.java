package ca.willatendo.simplelibrary.client;

import ca.willatendo.simplelibrary.server.event.RegisterRecipeBookSearchCategoriesEvent;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class RecipeBookManager {
    private static Map<ExtendedRecipeBookCategory, List<RecipeBookCategory>> searchCategories = Map.of();

    public static boolean hasSearchCategories(ExtendedRecipeBookCategory extendedRecipeBookCategory) {
        return RecipeBookManager.searchCategories.containsKey(extendedRecipeBookCategory);
    }

    public static Map<ExtendedRecipeBookCategory, List<RecipeBookCategory>> getSearchCategories() {
        return RecipeBookManager.searchCategories;
    }

    public static void init() {
        IdentityHashMap<ExtendedRecipeBookCategory, List<RecipeBookCategory>> searchCategories = new IdentityHashMap<>();
        RegisterRecipeBookSearchCategoriesEvent.EVENT.invoker().register(searchCategories::put);
        RecipeBookManager.searchCategories = Collections.unmodifiableMap(searchCategories);
    }
}
