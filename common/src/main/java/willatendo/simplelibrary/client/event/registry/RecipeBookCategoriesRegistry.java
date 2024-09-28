package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import willatendo.simplelibrary.server.registry.SimpleHolder;

import java.util.List;
import java.util.function.Function;

public interface RecipeBookCategoriesRegistry {
    void register(SimpleHolder<RecipeType<?>> recipeType, RecipeBookType recipeBookType, RecipeBookCategories recipeBookCategories, List<RecipeBookCategories> categories, List<RecipeBookCategories> aggregate, Function<Recipe<?>, RecipeBookCategories> lookup, String openTag, String filteringTag);
}
