package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeBookCategory;

public class RecipeBookCategorySubRegistry extends SimpleRegistry<RecipeBookCategory> {
    public RecipeBookCategorySubRegistry(String modId) {
        super(Registries.RECIPE_BOOK_CATEGORY, modId);
    }

    public SimpleHolder<RecipeBookCategory> registerSimple(String name) {
        return this.register(name, RecipeBookCategory::new);
    }
}
