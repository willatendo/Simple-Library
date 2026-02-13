package ca.willatendo.simplelibrary.network;

import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import ca.willatendo.simplelibrary.network.clientbound.ClientboundRecipeContentPacket;
import ca.willatendo.simplelibrary.server.recipe.SyncedRecipeData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeMap;

public final class FabricSimpleClientBoundPackets {
    private FabricSimpleClientBoundPackets() {
    }

    public static void clientboundRecipeContentPacket(ClientboundRecipeContentPacket clientboundRecipeContentPacket, Player player) {
        SyncedRecipeData.addRecipes(RecipeMap.create(clientboundRecipeContentPacket.recipes()));
        SimpleCoreUtils.LOGGER.info("Hello");
    }
}
