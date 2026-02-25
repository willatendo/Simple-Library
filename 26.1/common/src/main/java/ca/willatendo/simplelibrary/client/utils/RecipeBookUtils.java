package ca.willatendo.simplelibrary.client.utils;

import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;

import java.util.Optional;

public final class RecipeBookUtils {
    private RecipeBookUtils() {
    }

    public static RecipeBookComponent.TabInfo createSearch(ExtendedRecipeBookCategory extendedRecipeBookCategory) {
        return new RecipeBookComponent.TabInfo(new ItemStack(Items.COMPASS), Optional.empty(), extendedRecipeBookCategory);
    }
}
