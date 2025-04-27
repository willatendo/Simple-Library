package willatendo.simplelibrary.server.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeBookCategory;

public final class RecipeBookCategoryRegistry extends SimpleRegistry<RecipeBookCategory> {
    RecipeBookCategoryRegistry(String modId) {
        super(Registries.RECIPE_BOOK_CATEGORY, modId);
    }

    public SimpleHolder<RecipeBookCategory> registerSimple(String id) {
        return this.register(id, RecipeBookCategory::new);
    }
}
