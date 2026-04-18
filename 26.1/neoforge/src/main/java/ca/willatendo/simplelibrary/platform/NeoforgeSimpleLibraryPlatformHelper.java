package ca.willatendo.simplelibrary.platform;

import ca.willatendo.simplelibrary.client.event.RegisterRecipeBookOverlayEvent;
import ca.willatendo.simplelibrary.core.NeoforgePlatform;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistryBuilder;
import ca.willatendo.simplelibrary.core.registry.sub.AttachmentTypesSubRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.EntityDataSerializerSubRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.NeoforgeAttachmentTypesSubRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.NeoforgeEntityDataSerializerSubRegistry;
import ca.willatendo.simplelibrary.server.item.SimpleCreativeModeTabBuilder;
import ca.willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public final class NeoforgeSimpleLibraryPlatformHelper implements SimpleLibraryPlatformHelper {
    @Override
    public Platform getPlatform() {
        return new NeoforgePlatform();
    }

    @Override
    public void sendToServer(CustomPacketPayload customPacketPayload, CustomPacketPayload... customPacketPayloads) {
        ClientPacketDistributor.sendToServer(customPacketPayload, customPacketPayloads);
    }

    @Override
    public void sendToClient(ServerPlayer serverPlayer, CustomPacketPayload customPacketPayload) {
        PacketDistributor.sendToPlayer(serverPlayer, customPacketPayload);
    }

    @Override
    public FeatureFlag getFeatureFlag(Identifier identifier) {
        return FeatureFlags.REGISTRY.getFlag(identifier);
    }

    @Override
    public RecipeBookType getRecipeBookType(String modId, String name) {
        return RecipeBookType.valueOf(modId + name);
    }

    @Override
    public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> resourceKey, SimpleRegistryBuilder simpleRegistryBuilder) {
        RegistryBuilder<T> registryBuilder = new RegistryBuilder<T>(resourceKey);
        if (simpleRegistryBuilder.isSynced()) {
            registryBuilder.sync(true);
        }
        return registryBuilder.create();
    }

    @Override
    public AttachmentTypesSubRegistry createAttachmentTypesSubRegistry(String modId) {
        return new NeoforgeAttachmentTypesSubRegistry(modId);
    }

    @Override
    public EntityDataSerializerSubRegistry createEntityDataSerializerSubRegistry(String modId) {
        return new NeoforgeEntityDataSerializerSubRegistry(modId);
    }

    @Override
    public SimpleParticleType createSimpleParticleType(boolean overrideLimiter) {
        return new SimpleParticleType(overrideLimiter);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedMenuSupplier<T> extendedMenuSupplier) {
        return IMenuTypeExtension.create(extendedMenuSupplier::create);
    }

    @Override
    public CreativeModeTab.Builder createCreativeModeTab(SimpleCreativeModeTabBuilder simpleCreativeModeTabBuilder) {
        CreativeModeTab.Builder builder = CreativeModeTab.builder();
        if (simpleCreativeModeTabBuilder.hasAfter()) {
            builder.withTabsBefore(simpleCreativeModeTabBuilder.getBefore());
        }
        if (simpleCreativeModeTabBuilder.hasBefore()) {
            builder.withTabsAfter(simpleCreativeModeTabBuilder.getAfter());
        }
        return builder;
    }

    @Override
    public <T> boolean hasData(T value, Identifier attachmentType) {
        if (value instanceof AttachmentHolder attachmentHolder) {
            return attachmentHolder.hasData(Objects.requireNonNull(NeoForgeRegistries.ATTACHMENT_TYPES.getValue(attachmentType)));
        }
        return false;
    }

    @Override
    public <T, V> V getData(T value, Identifier attachmentType) {
        if (value instanceof AttachmentHolder attachmentHolder) {
            return (V) attachmentHolder.getData(Objects.requireNonNull(NeoForgeRegistries.ATTACHMENT_TYPES.getValue(attachmentType)));
        }
        return null;
    }

    @Override
    public <T, V> void setData(T value, Identifier attachmentType, V data) {
        if (value instanceof AttachmentHolder attachmentHolder) {
            attachmentHolder.setData((AttachmentType<V>) NeoForgeRegistries.ATTACHMENT_TYPES.getValue(attachmentType), data);
        }
    }

    @Override
    public <T> void removeData(T value, Identifier attachmentType) {
        if (value instanceof AttachmentHolder attachmentHolder) {
            attachmentHolder.removeData(NeoForgeRegistries.ATTACHMENT_TYPES.getValue(attachmentType));
        }
    }

    @Override
    public void openContainer(MenuProvider menuProvider, BlockPos blockPos, ServerPlayer serverPlayer) {
        serverPlayer.openMenu(menuProvider, blockPos);
    }

    @Override
    public void registerRecipeBookOverlayEvent(Map<Identifier, Pair<BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>>, BiFunction<Boolean, Boolean, Identifier>>> map) {
        NeoForge.EVENT_BUS.post(new RegisterRecipeBookOverlayEvent(map));
    }
}
