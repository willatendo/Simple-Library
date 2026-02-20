package ca.willatendo.simplelibrary.mixin.client;

import ca.willatendo.simplelibrary.client.RecipeBookManager;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/Map;copyOf(Ljava/util/Map;)Ljava/util/Map;"), method = "rebuildCollections", locals = LocalCapture.CAPTURE_FAILHARD)
    private void rebuildCollections(CallbackInfo ci, Map<ExtendedRecipeBookCategory, List<RecipeCollection>> map) {
        for (var entry : RecipeBookManager.getSearchCategories().entrySet()) {
            map.put(entry.getKey(), entry.getValue().stream().flatMap(category -> map.getOrDefault(category, List.of()).stream()).collect(ImmutableList.toImmutableList()));
        }
    }
}
