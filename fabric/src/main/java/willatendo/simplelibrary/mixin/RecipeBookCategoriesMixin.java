package willatendo.simplelibrary.mixin;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import willatendo.simplelibrary.server.util.RecipeBookRegistry;

import java.util.List;

@Mixin(RecipeBookCategories.class)
public class RecipeBookCategoriesMixin {
    @Inject(method = "getCategories", at = @At("HEAD"), cancellable = true)
    private static void getCustomCategories(RecipeBookType recipeBookType, CallbackInfoReturnable<List<RecipeBookCategories>> cir) {
        List<RecipeBookCategories> categories = RecipeBookRegistry.getCustomCategoriesOrNull(recipeBookType);
        if (categories != null) {
            cir.setReturnValue(categories);
        }
    }
}
