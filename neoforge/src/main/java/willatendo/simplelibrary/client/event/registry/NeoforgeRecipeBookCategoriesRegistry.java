package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;
import willatendo.simplelibrary.server.registry.SimpleHolder;

import java.util.List;
import java.util.function.Function;

public class NeoforgeRecipeBookCategoriesRegistry implements RecipeBookCategoriesRegistry {
    private final RegisterRecipeBookCategoriesEvent event;

    public NeoforgeRecipeBookCategoriesRegistry(RegisterRecipeBookCategoriesEvent event) {
        this.event = event;
    }

    @Override
    public void register(SimpleHolder<RecipeType<?>> recipeType, RecipeBookType recipeBookType, RecipeBookCategories recipeBookCategories, List<RecipeBookCategories> categories, List<RecipeBookCategories> aggregate, Function<Recipe<?>, RecipeBookCategories> lookup, String openTag, String filteringTag) {
        this.event.registerBookCategories(recipeBookType, categories);
        this.event.registerAggregateCategory(recipeBookCategories, aggregate);
        this.event.registerRecipeCategoryFinder(recipeType.get(), recipeHolder -> lookup.apply(recipeHolder.value()));
    }
}
