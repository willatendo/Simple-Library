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
import willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import willatendo.simplelibrary.server.util.Platform;
import willatendo.simplelibrary.server.util.SimpleRegistryBuilder;
import willatendo.simplelibrary.server.util.SimpleUtils;

import java.nio.file.Path;

public interface ModloaderHelper {
    ModloaderHelper INSTANCE = SimpleUtils.loadModloaderHelper(ModloaderHelper.class);

    // Internal Use

    Platform getPlatform();

    boolean isDevEnviroment();

    boolean isModLoaded(String modId);

    Path getConfigPath();

    CreativeModeTab.Builder createCreativeModeTab();

    <T> Registry<T> createRegistry(ResourceKey<Registry<T>> resourceKey, SimpleRegistryBuilder simpleRegistryBuilder);

    <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedMenuSupplier<T> extendedMenuSupplier);

    SimpleParticleType createParticleType(boolean overrideLimiter);

    void openContainer(MenuProvider menuProvider, BlockPos blockPos, ServerPlayer serverPlayer);
}
