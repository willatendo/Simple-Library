package ca.willatendo.simplelibrary.platform;

import ca.willatendo.simplelibrary.client.event.RegisterRecipeBookOverlayEvent;
import ca.willatendo.simplelibrary.core.FabricPlatform;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistryBuilder;
import ca.willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import com.chocohead.mm.api.ClassTinkerers;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public final class FabricSimpleLibraryPlatformHelper implements SimpleLibraryPlatformHelper {
    @Override
    public Platform getPlatform() {
        return new FabricPlatform();
    }

    @Override
    public void sendToServer(CustomPacketPayload customPacketPayload, CustomPacketPayload... customPacketPayloads) {
        ClientPlayNetworking.send(customPacketPayload);
        Arrays.asList(customPacketPayloads).forEach(ClientPlayNetworking::send);
    }

    @Override
    public void sendToClient(ServerPlayer serverPlayer, CustomPacketPayload customPacketPayload) {
        ServerPlayNetworking.send(serverPlayer, customPacketPayload);
    }

    @Override
    public FeatureFlag getFeatureFlag(Identifier identifier) {
        return FeatureFlags.REGISTRY.getFlag(identifier);
    }

    @Override
    public RecipeBookType getRecipeBookType(String modId, String name) {
        return ClassTinkerers.getEnum(RecipeBookType.class, name.toUpperCase());
    }

    @Override
    public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> resourceKey, SimpleRegistryBuilder simpleRegistryBuilder) {
        FabricRegistryBuilder<T, MappedRegistry<T>> fabricRegistryBuilder = FabricRegistryBuilder.createSimple(resourceKey);
        if (simpleRegistryBuilder.isSynced()) {
            fabricRegistryBuilder.attribute(RegistryAttribute.SYNCED);
        }
        return fabricRegistryBuilder.buildAndRegister();
    }

    @Override
    public SimpleParticleType createSimpleParticleType(boolean overrideLimiter) {
        return FabricParticleTypes.simple(overrideLimiter);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedMenuSupplier<T> extendedMenuSupplier) {
        return new ExtendedScreenHandlerType<>(extendedMenuSupplier::create, BlockPos.STREAM_CODEC.cast());
    }

    @Override
    public CreativeModeTab.Builder createCreativeModeTab() {
        return FabricItemGroup.builder();
    }

    @Override
    public void openContainer(MenuProvider menuProvider, BlockPos blockPos, ServerPlayer serverPlayer) {
        serverPlayer.openMenu(new ExtendedScreenHandlerFactory<BlockPos>() {
            @Override
            public BlockPos getScreenOpeningData(ServerPlayer serverPlayer1) {
                if (menuProvider instanceof BlockEntity blockEntity) {
                    return blockEntity.getBlockPos();
                } else {
                    return blockPos;
                }
            }

            @Override
            public Component getDisplayName() {
                return menuProvider.getDisplayName();
            }

            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
                return menuProvider.createMenu(windowId, inventory, player);
            }
        });
    }

    @Override
    public void registerRecipeBookOverlayEvent(Map<Class<? extends RecipeBookComponent<?>>, Pair<BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>>, BiFunction<Boolean, Boolean, Identifier>>> map) {
        RegisterRecipeBookOverlayEvent.EVENT.invoker().register(map);
    }
}
