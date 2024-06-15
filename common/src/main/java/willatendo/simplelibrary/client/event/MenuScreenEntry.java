package willatendo.simplelibrary.client.event;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public record MenuScreenEntry <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>>(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, U> screenConstructor) {
}
