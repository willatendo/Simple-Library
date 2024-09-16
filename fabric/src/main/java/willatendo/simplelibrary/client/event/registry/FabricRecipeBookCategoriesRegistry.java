package willatendo.simplelibrary.client.event.registry;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import willatendo.simplelibrary.server.registry.SimpleHolder;
import willatendo.simplelibrary.server.util.RecipeBookRegistry;

import java.util.List;
import java.util.function.Function;

public final class FabricRecipeBookCategoriesRegistry implements RecipeBookCategoriesRegistry {
    private final ImmutableMap.Builder<RecipeBookType, Pair<String, String>> tags = ImmutableMap.<RecipeBookType, Pair<String, String>>builder().putAll(RecipeBookSettings.TAG_FIELDS);

    @Override
    public void register(SimpleHolder<RecipeType<?>> recipeType, RecipeBookType recipeBookType, RecipeBookCategories recipeBookCategories, List<RecipeBookCategories> categories, List<RecipeBookCategories> aggregate, Function<Recipe<?>, RecipeBookCategories> lookup, String openTag, String filteringTag) {
        RecipeBookRegistry.registerBookCategories(recipeBookType, categories);
        RecipeBookRegistry.registerAggregateCategory(recipeBookCategories, aggregate);
        RecipeBookRegistry.registerRecipeCategoryFinder(recipeType.get(), recipeHolder -> lookup.apply(recipeHolder.value()));
        this.tags.put(recipeBookType, Pair.of(openTag, filteringTag));
    }

    public void buildAll() {
        RecipeBookSettings.TAG_FIELDS = this.tags.build();
    }
}
