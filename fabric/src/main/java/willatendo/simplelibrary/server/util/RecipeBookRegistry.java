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

    public static <T extends Recipe<?>> RecipeBookCategories findCategories(RecipeType<T> type, RecipeHolder<T> recipe) {
        Function<RecipeHolder<?>, RecipeBookCategories> lookup = RECIPE_CATEGORY_LOOKUPS.get(type);
        return lookup != null ? lookup.apply(recipe) : null;
    }

    public static List<RecipeBookCategories> getCustomCategoriesOrNull(RecipeBookType recipeBookType) {
        return TYPE_CATEGORIES.getOrDefault(recipeBookType, null);
    }

    public static void registerAggregateCategory(RecipeBookCategories category, List<RecipeBookCategories> others) {
        AGGREGATE_CATEGORIES.put(category, ImmutableList.copyOf(others));
    }

    public static void registerBookCategories(RecipeBookType type, List<RecipeBookCategories> categories) {
        TYPE_CATEGORIES.put(type, ImmutableList.copyOf(categories));
    }

    public static void registerRecipeCategoryFinder(RecipeType<?> type, Function<RecipeHolder<?>, RecipeBookCategories> lookup) {
        RECIPE_CATEGORY_LOOKUPS.put(type, lookup);
    }
}
