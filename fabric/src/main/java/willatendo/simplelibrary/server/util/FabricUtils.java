package willatendo.simplelibrary.server.util;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.world.inventory.RecipeBookType;

public class FabricUtils {
    public static RecipeBookType createRecipeBookType(String internalName) {
        return ClassTinkerers.getEnum(RecipeBookType.class, internalName.toUpperCase());
    }
}
