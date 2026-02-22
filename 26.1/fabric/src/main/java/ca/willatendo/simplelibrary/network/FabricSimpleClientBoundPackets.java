package ca.willatendo.simplelibrary.network;

import ca.willatendo.simplelibrary.network.clientbound.ClientboundCustomRecipeBookSettingsPacket;
import ca.willatendo.simplelibrary.network.clientbound.ClientboundRecipeContentPacket;
import ca.willatendo.simplelibrary.server.recipe.SyncedRecipeData;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeMap;

public final class FabricSimpleClientBoundPackets {
    private FabricSimpleClientBoundPackets() {
    }

    public static void clientboundCustomRecipeBookSettingsPacket(ClientboundCustomRecipeBookSettingsPacket clientboundCustomRecipeBookSettingsPacket, Player player) {
        if (player instanceof LocalPlayer localPlayer) {
            ClientRecipeBook clientRecipeBook = localPlayer.getRecipeBook();
            clientRecipeBook.setCustomBookSettings(clientboundCustomRecipeBookSettingsPacket.customRecipeBookSettings());
        }
    }

    public static void clientboundRecipeContentPacket(ClientboundRecipeContentPacket clientboundRecipeContentPacket, Player player) {
        SyncedRecipeData.addRecipes(RecipeMap.create(clientboundRecipeContentPacket.recipes()));
    }
}
