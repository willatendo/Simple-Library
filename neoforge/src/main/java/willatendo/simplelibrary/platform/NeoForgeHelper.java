package willatendo.simplelibrary.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.RegistryBuilder;
import willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import willatendo.simplelibrary.server.util.Platform;
import willatendo.simplelibrary.server.util.SimpleRegistryBuilder;

import java.nio.file.Path;

public class NeoForgeHelper implements ModloaderHelper {
    @Override
    public Platform getPlatform() {
        return Platform.NEOFORGE;
    }

    @Override
    public boolean isDevEnviroment() {
        return !FMLEnvironment.production;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public CreativeModeTab.Builder createCreativeModeTab() {
        return CreativeModeTab.builder();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedMenuSupplier<T> extendedMenuSupplier) {
        return IMenuTypeExtension.create(extendedMenuSupplier::create);
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
    public SimpleParticleType createParticleType(boolean overrideLimiter) {
        return new SimpleParticleType(overrideLimiter);
    }

    @Override
    public void openContainer(MenuProvider menuProvider, BlockPos blockPos, ServerPlayer serverPlayer) {
        serverPlayer.openMenu(menuProvider, blockPos);
    }
}