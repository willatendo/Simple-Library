package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.injects.RecipeBookExtension;
import ca.willatendo.simplelibrary.server.stats.CustomRecipeBookSettings;
import ca.willatendo.simplelibrary.server.stats.utils.RecipeBookUtils;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeBook.class)
public class RecipeBookMixin implements RecipeBookExtension {
    protected final CustomRecipeBookSettings customRecipeBookSettings = new CustomRecipeBookSettings();

    @Override
    public CustomRecipeBookSettings getCustomRecipeBookSettings() {
        return this.customRecipeBookSettings;
    }

    @Inject(at = @At("HEAD"), method = "isOpen", cancellable = true)
    private void isOpen(RecipeBookType recipeBookType, CallbackInfoReturnable<Boolean> cir) {
        if (RecipeBookUtils.isModded(recipeBookType)) {
            cir.setReturnValue(this.customRecipeBookSettings.isOpen(recipeBookType));
        }
    }

    @Inject(at = @At("HEAD"), method = "setOpen", cancellable = true)
    private void setOpen(RecipeBookType recipeBookType, boolean open, CallbackInfo ci) {
        if (RecipeBookUtils.isModded(recipeBookType)) {
            this.customRecipeBookSettings.setOpen(recipeBookType, open);
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "isFiltering", cancellable = true)
    private void isFiltering(RecipeBookType recipeBookType, CallbackInfoReturnable<Boolean> cir) {
        if (RecipeBookUtils.isModded(recipeBookType)) {
            cir.setReturnValue(this.customRecipeBookSettings.isFiltering(recipeBookType));
        }
    }

    @Inject(at = @At("HEAD"), method = "setFiltering", cancellable = true)
    private void setFiltering(RecipeBookType recipeBookType, boolean filtering, CallbackInfo ci) {
        if (RecipeBookUtils.isModded(recipeBookType)) {
            this.customRecipeBookSettings.setFiltering(recipeBookType, filtering);
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "setBookSetting", cancellable = true)
    private void setBookSetting(RecipeBookType recipeBookType, boolean open, boolean filtering, CallbackInfo ci) {
        if (RecipeBookUtils.isModded(recipeBookType)) {
            this.customRecipeBookSettings.setOpen(recipeBookType, open);
            this.customRecipeBookSettings.setFiltering(recipeBookType, filtering);
            ci.cancel();
        }
    }
}
