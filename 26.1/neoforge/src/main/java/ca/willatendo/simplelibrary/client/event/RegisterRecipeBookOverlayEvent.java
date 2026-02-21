package ca.willatendo.simplelibrary.client.event;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.bus.api.Event;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class RegisterRecipeBookOverlayEvent extends Event {
    private final Map<Identifier, Pair<BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>>, BiFunction<Boolean, Boolean, Identifier>>> map;

    public RegisterRecipeBookOverlayEvent(Map<Identifier, Pair<BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>>, BiFunction<Boolean, Boolean, Identifier>>> map) {
        this.map = map;
    }

    public void register(Identifier identifier, Pair<BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>>, BiFunction<Boolean, Boolean, Identifier>> pair) {
        this.map.put(identifier, pair);
    }
}
