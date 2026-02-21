package ca.willatendo.simplelibrary.client;

import ca.willatendo.simplelibrary.platform.SimpleLibraryPlatformHelper;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public final class CustomRecipeBooks {
    private static final Map<Identifier, Pair<BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>>, BiFunction<Boolean, Boolean, Identifier>>> MAP = Maps.newHashMap();

    public static void init() {
        MAP.clear();
        SimpleLibraryPlatformHelper.INSTANCE.registerRecipeBookOverlayEvent(MAP);
    }

    public static void getButton(Identifier identifier, int buttonX, int buttonY, RecipeDisplayId recipeDisplayId, RecipeDisplay recipeDisplay, ContextMap contextMap, boolean isCraftable, List<OverlayRecipeComponent.OverlayRecipeButton> overlayRecipeButtonList, NewButtonInstance newButtonInstance) {
        Pair<BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>>, BiFunction<Boolean, Boolean, Identifier>> pair = MAP.get(identifier);
        OverlayRecipeComponent.OverlayRecipeButton button = newButtonInstance.newButton(buttonX, buttonY, recipeDisplayId, recipeDisplay, contextMap, isCraftable, pair.getFirst(), pair.getSecond());
        overlayRecipeButtonList.add(button);
    }

    public interface NewButtonInstance {
        OverlayRecipeComponent.OverlayRecipeButton newButton(int x, int y, RecipeDisplayId recipeDisplayId, RecipeDisplay recipeDisplay, ContextMap contextMap, boolean isCraftable, BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>> slots, BiFunction<Boolean, Boolean, Identifier> sprite);
    }
}
