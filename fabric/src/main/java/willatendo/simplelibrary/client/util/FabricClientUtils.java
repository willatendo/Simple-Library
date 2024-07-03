package willatendo.simplelibrary.client.util;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.client.RecipeBookCategories;

public class FabricClientUtils {
    public static RecipeBookCategories createRecipeBookCategory(String internalName) {
        return ClassTinkerers.getEnum(RecipeBookCategories.class, internalName.toUpperCase());
    }
}
