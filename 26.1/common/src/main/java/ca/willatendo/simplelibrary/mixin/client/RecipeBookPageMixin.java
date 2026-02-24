package ca.willatendo.simplelibrary.mixin.client;

import ca.willatendo.simplelibrary.client.screen.recipe_book.CustomOverlayRecipeComponent;
import ca.willatendo.simplelibrary.client.screen.recipe_book.IdentifiableRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import net.minecraft.client.gui.screens.recipebook.SlotSelectTime;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookPage.class)
public class RecipeBookPageMixin {
    @Shadow
    @Final
    private RecipeBookComponent<?> parent;
    @Final
    private static SlotSelectTime slotSelectTime;

    @Inject(at = @At("INVOKE"), method = "<init>")
    private static void init(RecipeBookComponent<?> parent, SlotSelectTime slotSelectTime, boolean isFurnaceMenu, CallbackInfo ci) {
        RecipeBookPageMixin.slotSelectTime = slotSelectTime;
    }

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookPage;overlay:Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent;", opcode = 181))
    private void init(RecipeBookPage recipeBookPage, OverlayRecipeComponent overlayRecipeComponent) {
        recipeBookPage.overlay = this.parent instanceof IdentifiableRecipeBookComponent identifiableRecipeBookComponent ? new CustomOverlayRecipeComponent(RecipeBookPageMixin.slotSelectTime, identifiableRecipeBookComponent) : overlayRecipeComponent;
    }
}
