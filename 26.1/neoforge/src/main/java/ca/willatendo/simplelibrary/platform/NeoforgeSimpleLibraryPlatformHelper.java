package ca.willatendo.simplelibrary.platform;

import ca.willatendo.simplelibrary.core.NeoforgePlatform;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistryBuilder;
import ca.willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.RegistryBuilder;

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
    public SimpleParticleType createSimpleParticleType(boolean overrideLimiter) {
        return new SimpleParticleType(overrideLimiter);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedMenuSupplier<T> extendedMenuSupplier) {
        return IMenuTypeExtension.create(extendedMenuSupplier::create);
    }

    @Override
    public CreativeModeTab.Builder createCreativeModeTab() {
        return CreativeModeTab.builder();
    }

    @Override
    public void openContainer(MenuProvider menuProvider, BlockPos blockPos, ServerPlayer serverPlayer) {
        serverPlayer.openMenu(menuProvider, blockPos);
    }
}
