package ca.willatendo.simplelibrary.platform;

import ca.willatendo.simplelibrary.client.event.RegisterRecipeBookOverlayEvent;
import ca.willatendo.simplelibrary.core.FabricPlatform;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistryBuilder;
import ca.willatendo.simplelibrary.core.registry.sub.AttachmentTypesSubRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.EntityDataSerializerSubRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.FabricAttachmentTypesSubRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.FabricEntityDataSerializerSubRegistry;
import ca.willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import com.chocohead.mm.api.ClassTinkerers;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.impl.attachment.AttachmentRegistryImpl;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
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
import java.util.Objects;
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
        FabricRegistryBuilder<T, MappedRegistry<T>> fabricRegistryBuilder = FabricRegistryBuilder.create(resourceKey);
        if (simpleRegistryBuilder.isSynced()) {
            fabricRegistryBuilder.attribute(RegistryAttribute.SYNCED);
        }
        return fabricRegistryBuilder.buildAndRegister();
    }

    @Override
    public AttachmentTypesSubRegistry createAttachmentTypesSubRegistry(String modId) {
        return new FabricAttachmentTypesSubRegistry(modId);
    }

    @Override
    public EntityDataSerializerSubRegistry createEntityDataSerializerSubRegistry(String modId) {
        return new FabricEntityDataSerializerSubRegistry(modId);
    }

    @Override
    public SimpleParticleType createSimpleParticleType(boolean overrideLimiter) {
        return FabricParticleTypes.simple(overrideLimiter);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedMenuSupplier<T> extendedMenuSupplier) {
        return new ExtendedMenuType<>(extendedMenuSupplier::create, BlockPos.STREAM_CODEC.cast());
    }

    @Override
    public CreativeModeTab.Builder createCreativeModeTab() {
        return FabricCreativeModeTab.builder();
    }

    @Override
    public <T> boolean hasData(T value, Identifier attachmentType) {
        if (value instanceof AttachmentTarget attachmentTarget) {
            return attachmentTarget.hasAttached(Objects.requireNonNull(AttachmentRegistryImpl.get(attachmentType)));
        }
        return false;
    }

    @Override
    public <T, V> V getData(T value, Identifier attachmentType) {
        if (value instanceof AttachmentTarget attachmentTarget) {
            return (V) attachmentTarget.getAttachedOrCreate(Objects.requireNonNull(AttachmentRegistryImpl.get(attachmentType)));
        }
        return null;
    }

    @Override
    public <T, V> void setData(T value, Identifier attachmentType, V data) {
        if (value instanceof AttachmentTarget attachmentTarget) {
            attachmentTarget.setAttached((AttachmentType<V>) Objects.requireNonNull(AttachmentRegistryImpl.get(attachmentType)), data);
        }
    }

    @Override
    public <T> void removeData(T value, Identifier attachmentType) {
        if (value instanceof AttachmentTarget attachmentTarget) {
            attachmentTarget.removeAttached(Objects.requireNonNull(AttachmentRegistryImpl.get(attachmentType)));
        }
    }

    @Override
    public void openContainer(MenuProvider menuProvider, BlockPos blockPos, ServerPlayer serverPlayer) {
        serverPlayer.openMenu(new ExtendedMenuProvider<>() {
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
    public void registerRecipeBookOverlayEvent(Map<Identifier, Pair<BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>>, BiFunction<Boolean, Boolean, Identifier>>> map) {
        RegisterRecipeBookOverlayEvent.EVENT.invoker().register(map::put);
    }
}
