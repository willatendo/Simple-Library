package ca.willatendo.simplelibrary.server.event;

import ca.willatendo.simplelibrary.server.recipe.SyncedRecipeData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

@EventBusSubscriber
public final class ModEvents {
    @SubscribeEvent
    public static void registerRecipesEvent(RecipesReceivedEvent event) {
        SyncedRecipeData.addRecipes(event.getRecipeMap());
    }
}
