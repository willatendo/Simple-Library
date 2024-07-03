package willatendo.simplelibrary.server.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RecipeBookRegistry {
    public static final Map<RecipeBookCategories, List<RecipeBookCategories>> AGGREGATE_CATEGORIES = new HashMap<>();
    private static final Map<RecipeBookType, List<RecipeBookCategories>> TYPE_CATEGORIES = new HashMap<>();
    private static final Map<RecipeType<?>, Function<RecipeHolder<?>, RecipeBookCategories>> RECIPE_CATEGORY_LOOKUPS = new HashMap<>();

    public static <T extends Recipe<?>> RecipeBookCategories findCategories(RecipeType<T> type, RecipeHolder<T> recipeHolder) {
        Function<RecipeHolder<?>, RecipeBookCategories> lookup = RECIPE_CATEGORY_LOOKUPS.get(type);
        return lookup != null ? lookup.apply(recipeHolder) : null;
    }

    public static List<RecipeBookCategories> getCustomCategoriesOrNull(RecipeBookType recipeBookType) {
        return TYPE_CATEGORIES.getOrDefault(recipeBookType, null);
    }

    public static void registerAggregateCategory(RecipeBookCategories recipeBookCategories, List<RecipeBookCategories> recipeBookCategoriesList) {
        AGGREGATE_CATEGORIES.put(recipeBookCategories, ImmutableList.copyOf(recipeBookCategoriesList));
    }

    public static void registerBookCategories(RecipeBookType recipeBookType, List<RecipeBookCategories> recipeBookCategories) {
        TYPE_CATEGORIES.put(recipeBookType, ImmutableList.copyOf(recipeBookCategories));
    }

    public static void registerRecipeCategoryFinder(RecipeType<?> recipeType, Function<RecipeHolder<?>, RecipeBookCategories> lookup) {
        RECIPE_CATEGORY_LOOKUPS.put(recipeType, lookup);
    }
}
