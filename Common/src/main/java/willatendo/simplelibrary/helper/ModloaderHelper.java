package willatendo.simplelibrary.helper;

import java.nio.file.Path;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import willatendo.simplelibrary.server.util.SimpleUtils.ExtendedFactory;

public interface ModloaderHelper {
	public static ModloaderHelper getInstance() {
		return ModloaderHelperImpl.PLATFORM;
	}

	Path getConfigDir();

	CreativeModeTab.Builder getBuilder();

	<T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedFactory<T> extendedFactory);
}
