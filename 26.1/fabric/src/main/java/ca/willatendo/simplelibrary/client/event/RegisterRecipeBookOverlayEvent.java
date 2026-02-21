package ca.willatendo.simplelibrary.client.event;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@FunctionalInterface
public interface RegisterRecipeBookOverlayEvent {
    Event<RegisterRecipeBookOverlayEvent> EVENT = EventFactory.createArrayBacked(RegisterRecipeBookOverlayEvent.class, callbacks -> map -> {
        for (RegisterRecipeBookOverlayEvent callback : callbacks) {
            callback.register(map);
        }
    });

    void register(Map<Class<? extends RecipeBookComponent<?>>, Pair<BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>>, BiFunction<Boolean, Boolean, Identifier>>> map);
}
