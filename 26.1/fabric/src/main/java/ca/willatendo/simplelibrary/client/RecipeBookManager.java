package ca.willatendo.simplelibrary.client;

import ca.willatendo.simplelibrary.server.event.RegisterRecipeBookSearchCategoriesEvent;
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
        RegisterRecipeBookSearchCategoriesEvent.EVENT.invoker().register(SEARCH_CATEGORIES::put);
    }
}
