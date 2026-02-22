package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.stats.CustomRecipeBookSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Shadow
    @Final
    private ServerRecipeBook recipeBook;
    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void readAdditionalSaveData(ValueInput valueInput, CallbackInfo ci) {
        valueInput.read("recipe_book_extras", CustomRecipeBookSettings.MAP_CODEC.codec()).ifPresent(customRecipeBookSettings -> this.recipeBook.loadCustomUntrusted(customRecipeBookSettings, recipeResourceKey -> this.server.getRecipeManager().byKey(recipeResourceKey).isPresent()));
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void addAdditionalSaveData(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.store("recipe_book_extras", CustomRecipeBookSettings.MAP_CODEC.codec(), this.recipeBook.getCustomRecipeBookSettings());
    }
}
