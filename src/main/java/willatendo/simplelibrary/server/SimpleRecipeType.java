package willatendo.simplelibrary.server;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

public class SimpleRecipeType<T extends Recipe<?>> implements RecipeType<T> {
	@Override
	public String toString() {
		return ForgeRegistries.RECIPE_TYPES.getKey(this).getPath();
	}
}
