package willatendo.simplelibrary.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import willatendo.simplelibrary.server.util.RecipeBookRegistry;

import java.util.List;
import java.util.Map;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(method = "setupCollections", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;copyOf(Ljava/util/Map;)Lcom/google/common/collect/ImmutableMap;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void setupModdedAggregateCategories(Iterable<Recipe<?>> iterable, RegistryAccess registryAccess, CallbackInfo ci, Map<RecipeBookCategories, List<List<Recipe<?>>>> categorizeAndGroupRecipes, Map<RecipeBookCategories, List<RecipeCollection>> aggregateCategories) {
        RecipeBookRegistry.AGGREGATE_CATEGORIES.forEach((recipeBookCategories, list) -> {
            aggregateCategories.put(recipeBookCategories, list.stream().flatMap((recipeBookCategoriesx) -> {
                return aggregateCategories.getOrDefault(recipeBookCategoriesx, ImmutableList.of()).stream();
            }).collect(ImmutableList.toImmutableList()));
        });
    }

    @Inject(method = "getCategory", at = @At(value = "INVOKE", target = "Lcom/mojang/logging/LogUtils;defer(Ljava/util/function/Supplier;)Ljava/lang/Object;", ordinal = 0), cancellable = true)
    private static void getCustomRecipeCategory(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> cir) {
        RecipeBookCategories categories = RecipeBookRegistry.findCategories((RecipeType) recipe.getType(), recipe);
        if (categories != null) {
            cir.setReturnValue(categories);
        }
    }
}
