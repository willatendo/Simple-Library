package ca.willatendo.simplelibrary.network;

import ca.willatendo.simplelibrary.network.clientbound.ClientboundCustomRecipeBookSettingsPacket;
import ca.willatendo.simplelibrary.network.clientbound.ClientboundRecipeContentPacket;
import ca.willatendo.simplelibrary.server.recipe.SyncedRecipeData;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeMap;

public final class FabricSimpleClientBoundPackets {
    private FabricSimpleClientBoundPackets() {
    }

    public static void clientboundCustomRecipeBookSettingsPacket(ClientboundCustomRecipeBookSettingsPacket clientboundCustomRecipeBookSettingsPacket, Player player) {
        if (player instanceof LocalPlayer localPlayer) {
            Minecraft minecraft = Minecraft.getInstance();
            ClientRecipeBook clientRecipeBook = localPlayer.getRecipeBook();
            clientRecipeBook.setBookSettings(clientboundCustomRecipeBookSettingsPacket.recipeBookSettings());
            clientRecipeBook.setCustomBookSettings(clientboundCustomRecipeBookSettingsPacket.customRecipeBookSettings());
            clientRecipeBook.rebuildCollections();
            minecraft.getConnection().searchTrees().updateRecipes(clientRecipeBook, minecraft.level);
            if (minecraft.screen instanceof RecipeUpdateListener recipeupdatelistener) {
                recipeupdatelistener.recipesUpdated();
            }
        }
    }

    public static void clientboundRecipeContentPacket(ClientboundRecipeContentPacket clientboundRecipeContentPacket, Player player) {
        SyncedRecipeData.addRecipes(RecipeMap.create(clientboundRecipeContentPacket.recipes()));
    }
}
