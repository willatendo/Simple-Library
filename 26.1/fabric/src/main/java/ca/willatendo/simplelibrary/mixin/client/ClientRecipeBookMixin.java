package ca.willatendo.simplelibrary.mixin.client;

import ca.willatendo.simplelibrary.client.RecipeBookManager;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import ca.willatendo.simplelibrary.injects.ClientRecipeBookExtension;
import ca.willatendo.simplelibrary.server.stats.CustomRecipeBookSettings;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin extends RecipeBook implements ClientRecipeBookExtension {
    @Shadow
    private Map<ExtendedRecipeBookCategory, List<RecipeCollection>> collectionsByTab;

    @Override
    public void setCustomBookSettings(CustomRecipeBookSettings customRecipeBookSettings) {
        this.getCustomRecipeBookSettings().replaceFrom(customRecipeBookSettings);
    }

    @Inject(at = @At("TAIL"), method = "rebuildCollections", locals = LocalCapture.CAPTURE_FAILHARD)
    private void rebuildCollections(CallbackInfo ci, Map<ExtendedRecipeBookCategory, List<RecipeCollection>> map) {
        Map<ExtendedRecipeBookCategory, List<RecipeCollection>> newMap = new HashMap<>();
        for (Map.Entry<ExtendedRecipeBookCategory, List<RecipeBookCategory>> entry : RecipeBookManager.getSearchCategories().entrySet()) {
            newMap.put(entry.getKey(), entry.getValue().stream().flatMap(category -> map.getOrDefault(category, List.of()).stream()).collect(ImmutableList.toImmutableList()));
        }

        this.collectionsByTab = new HashMap<>(this.collectionsByTab);
        this.collectionsByTab.putAll(newMap);
        CoreUtils.LOGGER.info("{}", this.collectionsByTab);
    }
}
