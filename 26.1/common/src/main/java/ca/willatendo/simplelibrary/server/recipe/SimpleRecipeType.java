package ca.willatendo.simplelibrary.server.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public final class SimpleRecipeType<T extends Recipe<?>> implements RecipeType<T> {
    @Override
    public String toString() {
        return BuiltInRegistries.RECIPE_TYPE.getKey(this).getPath();
    }
}
