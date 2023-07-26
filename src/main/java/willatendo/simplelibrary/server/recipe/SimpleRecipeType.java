package willatendo.simplelibrary.server.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

/* 
 * @author Willatendo
 */
public class SimpleRecipeType<T extends Recipe<?>> implements RecipeType<T> {
	@Override
	public String toString() {
		return ForgeRegistries.RECIPE_TYPES.getKey(this).getPath();
	}
}