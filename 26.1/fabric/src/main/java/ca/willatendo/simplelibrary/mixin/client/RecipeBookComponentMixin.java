package ca.willatendo.simplelibrary.mixin.client;

import ca.willatendo.simplelibrary.client.RecipeBookManager;
import ca.willatendo.simplelibrary.server.stats.utils.RecipeBookUtils;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RecipeBookComponent.class)
public class RecipeBookComponentMixin<T extends RecipeBookMenu> {
    @Shadow
    @Final
    protected T menu;
    @Shadow
    protected Minecraft minecraft;
    @Shadow
    @Final
    private List<RecipeBookTabButton> tabButtons;
    @Shadow
    private int xOffset;
    @Shadow
    private int width;
    @Shadow
    private int height;
    @Shadow
    private ClientRecipeBook book;

    @Inject(at = @At("HEAD"), method = "updateTabs", cancellable = true)
    private void updateTabs(boolean isFiltering, CallbackInfo ci) {
        int x = (this.width - 147) / 2 - this.xOffset - 30;
        int y = (this.height - 166) / 2 + 3;
        int i = 0;

        for (RecipeBookTabButton recipeBookTabButton : this.tabButtons) {
            ExtendedRecipeBookCategory extendedRecipeBookCategory = recipeBookTabButton.getCategory();
            if (extendedRecipeBookCategory instanceof SearchRecipeBookCategory || RecipeBookManager.getSearchCategories().containsKey(extendedRecipeBookCategory)) {
                recipeBookTabButton.visible = true;
                recipeBookTabButton.setPosition(x, y + 27 * i++);
            } else if (recipeBookTabButton.updateVisibility(this.book)) {
                recipeBookTabButton.setPosition(x, y + 27 * i++);
                recipeBookTabButton.startAnimation(this.book, isFiltering);
            }
        }
        ci.cancel();
    }

    @Inject(at = @At(value = "HEAD"), method = "sendUpdateSettings", cancellable = true)
    private void sendUpdateSettings(CallbackInfo ci) {
        if (this.minecraft.getConnection() != null) {
            RecipeBookType recipeBookType = this.menu.getRecipeBookType();
            if (RecipeBookUtils.isModded(recipeBookType)) {
                boolean isOpen = this.book.getCustomRecipeBookSettings().isOpen(recipeBookType);
                boolean isFiltering = this.book.getCustomRecipeBookSettings().isFiltering(recipeBookType);
                this.minecraft.getConnection().send(new ServerboundRecipeBookChangeSettingsPacket(recipeBookType, isOpen, isFiltering));
                ci.cancel();
            }
        }
    }
}
