package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public final class NeoforgeMenuScreenRegister implements MenuScreenRegister {
    private final RegisterMenuScreensEvent event;

    public NeoforgeMenuScreenRegister(RegisterMenuScreensEvent event) {
        this.event = event;
    }

    @Override
    public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void addMenuScreen(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, U> screenConstructor) {
        this.event.register(menuType, screenConstructor);
    }
}
