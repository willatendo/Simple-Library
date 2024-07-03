package willatendo.simplelibrary.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeHolder;
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
    private void simplelibrary_setupCollections(Iterable<RecipeHolder<?>> iterable, RegistryAccess registryAccess, CallbackInfo callbackInfo, Map<RecipeBookCategories, List<List<RecipeHolder<?>>>> categorizeAndGroupRecipes, Map<RecipeBookCategories, List<RecipeCollection>> aggregateCategories) {
        RecipeBookRegistry.AGGREGATE_CATEGORIES.forEach((recipeBookCategories, list) -> aggregateCategories.put(recipeBookCategories, list.stream().flatMap((recipeBookCategories1) -> aggregateCategories.getOrDefault(recipeBookCategories1, ImmutableList.of()).stream()).collect(ImmutableList.toImmutableList())));
    }

    @Inject(method = "getCategory", at = @At(value = "INVOKE", target = "Lcom/mojang/logging/LogUtils;defer(Ljava/util/function/Supplier;)Ljava/lang/Object;", ordinal = 0), cancellable = true)
    private static void simplelibrary_getCategory(RecipeHolder<?> recipeHolder, CallbackInfoReturnable<RecipeBookCategories> callbackInfoReturnable) {
        RecipeBookCategories categories = RecipeBookRegistry.findCategories((RecipeType) recipeHolder.value().getType(), recipeHolder);
        if (categories != null) {
            callbackInfoReturnable.setReturnValue(categories);
        }
    }
}
