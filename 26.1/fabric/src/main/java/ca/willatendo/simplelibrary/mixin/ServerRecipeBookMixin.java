package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.injects.ServerRecipeBookExtension;
import ca.willatendo.simplelibrary.network.clientbound.ClientboundCustomRecipeBookSettingsPacket;
import ca.willatendo.simplelibrary.network.utils.NetworkUtils;
import ca.willatendo.simplelibrary.server.stats.CustomRecipeBookSettings;
import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin extends RecipeBook implements ServerRecipeBookExtension {
    @Shadow
    @Final
    protected Set<ResourceKey<Recipe<?>>> known;
    @Shadow
    @Final
    protected Set<ResourceKey<Recipe<?>>> highlight;
    @Shadow
    @Final
    private ServerRecipeBook.DisplayResolver displayResolver;

    @Override
    public void loadCustomUntrusted(CustomRecipeBookSettings customRecipeBookSettings, Predicate<ResourceKey<Recipe<?>>> predicate) {
        this.getCustomRecipeBookSettings().replaceFrom(customRecipeBookSettings);
    }

    @Inject(at = @At("HEAD"), method = "copyOverData")
    private void copyOverData(ServerRecipeBook serverRecipeBook, CallbackInfo ci) {
        this.getCustomRecipeBookSettings().replaceFrom(serverRecipeBook.getCustomRecipeBookSettings());
    }

    @Inject(at = @At("HEAD"), method = "sendInitialRecipeBook", cancellable = true)
    private void sendInitialRecipeBook(ServerPlayer serverPlayer, CallbackInfo ci) {
        NetworkUtils.sendToClient(serverPlayer, new ClientboundCustomRecipeBookSettingsPacket(this.bookSettings.copy(), this.getCustomRecipeBookSettings().copy()));
        List<ClientboundRecipeBookAddPacket.Entry> list = new ArrayList<>(this.known.size());

        for (ResourceKey<Recipe<?>> recipeResourceKey : this.known) {
            this.displayResolver.displaysForRecipe(recipeResourceKey, recipeDisplayEntry -> list.add(new ClientboundRecipeBookAddPacket.Entry(recipeDisplayEntry, false, this.highlight.contains(recipeResourceKey))));
        }

        serverPlayer.connection.send(new ClientboundRecipeBookAddPacket(list, true));
        ci.cancel();
    }
}
