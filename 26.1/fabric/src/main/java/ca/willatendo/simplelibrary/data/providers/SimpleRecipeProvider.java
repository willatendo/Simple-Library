package ca.willatendo.simplelibrary.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public abstract class SimpleRecipeProvider extends RecipeProvider {
    private final String modId;

    protected SimpleRecipeProvider(HolderLookup.Provider registries, RecipeOutput recipeOutput, String modId) {
        super(registries, recipeOutput);
        this.modId = modId;
    }

    private void cookFood(Item inputItem, Item outputItem) {
        String outputItemName = BuiltInRegistries.ITEM.getKey(outputItem).getPath();
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(inputItem), RecipeCategory.FOOD, CookingBookCategory.FOOD, outputItem, 0.35F, 200).unlockedBy(RecipeProvider.getHasName(inputItem), this.has(inputItem)).save(this.output, this.modId + ":" + outputItemName + "_from_smelting");
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(inputItem), RecipeCategory.FOOD, outputItem, 0.35F, 100).unlockedBy(RecipeProvider.getHasName(inputItem), this.has(inputItem)).save(this.output, this.modId + ":" + outputItemName + "_from_smoking");
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(inputItem), RecipeCategory.FOOD, outputItem, 0.35F, 400).unlockedBy(RecipeProvider.getHasName(inputItem), this.has(inputItem)).save(this.output, this.modId + ":" + outputItemName + "_from_campfire_cooking");
    }

    public static abstract class Runner extends RecipeProvider.Runner {
        private final String modId;

        public Runner(PackOutput packOutput, String modId, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
            this.modId = modId;
        }

        protected abstract RecipeProvider createRecipeProvider(HolderLookup.Provider registries, String modId, RecipeOutput recipeOutput);

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput recipeOutput) {
            return this.createRecipeProvider(registries, this.modId, recipeOutput);
        }

        @Override
        public String getName() {
            return "SimpleLibrary: Recipe Provider for " + this.modId;
        }
    }
}
