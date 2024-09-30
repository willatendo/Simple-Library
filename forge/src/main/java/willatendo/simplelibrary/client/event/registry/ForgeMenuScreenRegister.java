package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public final class ForgeMenuScreenRegister implements MenuScreenRegistry {
    private final FMLClientSetupEvent event;

    public ForgeMenuScreenRegister(FMLClientSetupEvent event) {
        this.event = event;
    }

    @Override
    public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void addMenuScreen(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, U> screenConstructor) {
        this.event.enqueueWork(() -> {
            MenuScreens.register(menuType, screenConstructor);
        });
    }
}
