package ca.willatendo.simplelibrary.client;

import ca.willatendo.simplelibrary.client.event.RegisterRecipeBookSearchCategoriesEvent;
import com.google.common.collect.Maps;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;

import java.util.List;
import java.util.Map;

public final class RecipeBookManager {
    private static final Map<ExtendedRecipeBookCategory, List<RecipeBookCategory>> SEARCH_CATEGORIES = Maps.newHashMap();

    public static Map<ExtendedRecipeBookCategory, List<RecipeBookCategory>> getSearchCategories() {
        return RecipeBookManager.SEARCH_CATEGORIES;
    }

    public static void init() {
        SEARCH_CATEGORIES.clear();
        Map<ExtendedRecipeBookCategory, List<RecipeBookCategory>> searchCategories = Maps.newHashMap();
        RegisterRecipeBookSearchCategoriesEvent.EVENT.invoker().register(searchCategories);
        RecipeBookManager.SEARCH_CATEGORIES.putAll(searchCategories);
    }
}
