package ca.willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;

import java.util.List;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface RegisterRecipeBookSearchCategoriesEvent {
    Event<RegisterRecipeBookSearchCategoriesEvent> EVENT = EventFactory.createArrayBacked(RegisterRecipeBookSearchCategoriesEvent.class, callbacks -> biConsumer -> {
        for (RegisterRecipeBookSearchCategoriesEvent callback : callbacks) {
            callback.register(biConsumer);
        }
    });

    void register(BiConsumer<ExtendedRecipeBookCategory, List<RecipeBookCategory>> biConsumer);
}
