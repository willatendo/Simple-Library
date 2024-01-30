package willatendo.simplelibrary.server.util;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType.ExtendedFactory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FabricUtils {
	public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedFactory<T> extendedFactory) {
		return new ExtendedScreenHandlerType<T>(extendedFactory);
	}
}
