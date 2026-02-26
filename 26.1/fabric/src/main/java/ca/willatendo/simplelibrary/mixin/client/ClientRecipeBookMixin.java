package ca.willatendo.simplelibrary.mixin.client;

import ca.willatendo.simplelibrary.client.RecipeBookManager;
import ca.willatendo.simplelibrary.injects.ClientRecipeBookExtension;
import ca.willatendo.simplelibrary.server.stats.CustomRecipeBookSettings;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin extends RecipeBook implements ClientRecipeBookExtension {
    @Shadow
    @Final
    private Map<RecipeDisplayId, RecipeDisplayEntry> known;
    @Shadow
    private Map<ExtendedRecipeBookCategory, List<RecipeCollection>> collectionsByTab;
    @Shadow
    private List<RecipeCollection> allCollections;

    @Override
    public void setCustomBookSettings(CustomRecipeBookSettings customRecipeBookSettings) {
        this.getCustomRecipeBookSettings().replaceFrom(customRecipeBookSettings);
    }

    @Inject(at = @At("HEAD"), method = "rebuildCollections", cancellable = true)
    private void rebuildCollections(CallbackInfo ci) {
        Map<RecipeBookCategory, List<List<RecipeDisplayEntry>>> map = ClientRecipeBookMixin.categorizeAndGroupRecipes(this.known.values());
        Map<ExtendedRecipeBookCategory, List<RecipeCollection>> collections = new HashMap<>();
        ImmutableList.Builder<RecipeCollection> builder = ImmutableList.builder();
        map.forEach((recipeBookCategory, lists) -> {
            Objects.requireNonNull(builder);
            collections.put(recipeBookCategory, lists.stream().map(RecipeCollection::new).peek(builder::add).collect(ImmutableList.toImmutableList()));
        });

        for (SearchRecipeBookCategory searchRecipeBookCategory : SearchRecipeBookCategory.values()) {
            collections.put(searchRecipeBookCategory, searchRecipeBookCategory.includedCategories().stream().flatMap(recipeBookCategory -> collections.getOrDefault(recipeBookCategory, List.of()).stream()).collect(ImmutableList.toImmutableList()));
        }

        for (Map.Entry<ExtendedRecipeBookCategory, List<RecipeBookCategory>> entry : RecipeBookManager.getSearchCategories().entrySet()) {
            collections.put(entry.getKey(), entry.getValue().stream().flatMap(category -> collections.getOrDefault(category, List.of()).stream()).collect(ImmutableList.toImmutableList()));
        }

        this.collectionsByTab = Map.copyOf(collections);
        this.allCollections = builder.build();
        ci.cancel();
    }


    private static Map<RecipeBookCategory, List<List<RecipeDisplayEntry>>> categorizeAndGroupRecipes(Iterable<RecipeDisplayEntry> recipes) {
        Map<RecipeBookCategory, List<List<RecipeDisplayEntry>>> map = new HashMap<>();
        Table<RecipeBookCategory, Integer, List<RecipeDisplayEntry>> table = HashBasedTable.create();

        for (RecipeDisplayEntry recipeDisplayEntry : recipes) {
            RecipeBookCategory recipeBookCategory = recipeDisplayEntry.category();
            OptionalInt optionalint = recipeDisplayEntry.group();
            if (optionalint.isEmpty()) {
                map.computeIfAbsent(recipeBookCategory, recipeBookCategoryIn -> new ArrayList<>()).add(List.of(recipeDisplayEntry));
            } else {
                List<RecipeDisplayEntry> list = table.get(recipeBookCategory, optionalint.getAsInt());
                if (list == null) {
                    list = new ArrayList<>();
                    table.put(recipeBookCategory, optionalint.getAsInt(), list);
                    map.computeIfAbsent(recipeBookCategory, recipeBookCategoryIn -> new ArrayList<>()).add(list);
                }

                list.add(recipeDisplayEntry);
            }
        }

        return map;
    }
}
