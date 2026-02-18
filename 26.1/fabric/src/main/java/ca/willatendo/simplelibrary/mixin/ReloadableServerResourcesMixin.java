package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.injects.ReloadableServerResourcesExtension;
import ca.willatendo.simplelibrary.server.conditions.ConditionContext;
import ca.willatendo.simplelibrary.server.conditions.ICondition;
import ca.willatendo.simplelibrary.server.event.AddReloadListenersEvent;
import ca.willatendo.simplelibrary.server.event.TagEvents;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.util.Unit;
import net.minecraft.world.flag.FeatureFlagSet;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin implements ReloadableServerResourcesExtension {
    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    @Final
    private static CompletableFuture<Unit> DATA_RELOAD_INITIAL_TASK;
    @Final
    private HolderLookup.Provider registryLookup;
    @Final
    private ICondition.IContext context;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(LayeredRegistryAccess<RegistryLayer> layeredRegistryAccess, HolderLookup.Provider provider, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, List<Registry.PendingTags<?>> pendingTags, PermissionSet permissionSet, CallbackInfo ci) {
        this.registryLookup = provider;
        this.context = new ConditionContext(pendingTags, layeredRegistryAccess.compositeAccess(), enabledFeatures);
    }

    @Override
    public ICondition.IContext getConditionContext() {
        return this.context;
    }


    @ModifyArg(method = {"method_58296(Lnet/minecraft/world/flag/FeatureFlagSet;Lnet/minecraft/commands/Commands$CommandSelection;Ljava/util/List;Lnet/minecraft/server/permissions/PermissionSet;Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Lnet/minecraft/server/ReloadableServerRegistries$LoadResult;)Ljava/util/concurrent/CompletionStage;"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/SimpleReloadInstance;create(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/server/packs/resources/ReloadInstance;"))
    private static List<PreparableReloadListener> onSetupDataReloaders(List<PreparableReloadListener> reloaders, @Local(argsOnly = true) ReloadableServerRegistries.LoadResult loadResult, @Local(argsOnly = true) FeatureFlagSet featureSet, @Local ReloadableServerResources dataPackContents) {
        ArrayList<PreparableReloadListener> list = new ArrayList(reloaders);
        AddReloadListenersEvent.EVENT.invoker().onAddReloadListeners(list, dataPackContents.getConditionContext(), dataPackContents);
        return Collections.unmodifiableList(list);
    }


    @Inject(at = @At("TAIL"), method = "updateStaticRegistryTags")
    private void updateStaticRegistryTags(CallbackInfo ci) {
        TagEvents.UPDATE_TAGS.invoker().update(this.registryLookup, false, false);
    }
}
