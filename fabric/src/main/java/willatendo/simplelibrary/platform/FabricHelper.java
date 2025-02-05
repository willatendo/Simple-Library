package willatendo.simplelibrary.platform;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.level.block.entity.BlockEntity;
import willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import willatendo.simplelibrary.server.util.Platform;
import willatendo.simplelibrary.server.util.SimpleRegistryBuilder;

import java.nio.file.Path;

public class FabricHelper implements ModloaderHelper {
    @Override
    public Platform getPlatform() {
        return Platform.FABRIC;
    }

    @Override
    public boolean isDevEnviroment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public Builder createCreativeModeTab() {
        return FabricItemGroup.builder();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedMenuSupplier<T> extendedMenuSupplier) {
        return new ExtendedScreenHandlerType<T, BlockPos>(extendedMenuSupplier::create, BlockPos.STREAM_CODEC.cast());
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
    public SimpleParticleType createParticleType(boolean overrideLimiter) {
        return FabricParticleTypes.simple(overrideLimiter);
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
}
