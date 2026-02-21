package ca.willatendo.simplelibrary.client;

import ca.willatendo.simplelibrary.client.filter.CreativeModeTabFilter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Consumer;

public record SimpleLibraryClientEventListener() implements ClientEventListener {
    @Override
    public void clientSetup() {
        CustomRecipeBooks.init();
    }

    @Override
    public void screenInitPostEvent(Screen screen, Consumer<AbstractWidget> widgets) {
        CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS.forEach(creativeModeTabFilter -> creativeModeTabFilter.modifyWidgetsEvent(screen, widgets));
    }

    @Override
    public void screenRenderPreEvent(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS.forEach(creativeModeTabFilter -> creativeModeTabFilter.beforeDrawEvent(screen, guiGraphics, mouseX, mouseY));
    }

    @Override
    public void screenCloseEvent(Screen screen) {
        CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS.forEach(creativeModeTabFilter -> creativeModeTabFilter.closedEvent(screen));
    }

    @Override
    public void clientPlayerLoggingOut() {
        CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS.forEach(CreativeModeTabFilter::loggingOutEvent);
    }
}
