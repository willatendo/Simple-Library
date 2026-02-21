package ca.willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface RegisterRecipeBookSearchCategoriesEvent {
    Event<RegisterRecipeBookSearchCategoriesEvent> EVENT = EventFactory.createArrayBacked(RegisterRecipeBookSearchCategoriesEvent.class, callbacks -> map -> {
        for (RegisterRecipeBookSearchCategoriesEvent callback : callbacks) {
            callback.register(map);
        }
    });

    void register(Map<ExtendedRecipeBookCategory, List<RecipeBookCategory>> map);
}
