package willatendo.simplelibrary.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.components.AbstractWidget;
import willatendo.simplelibrary.client.filter.CreativeModeTabFilter;

import java.util.List;

public class FabricSimpleLibraryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((minecraft, screen, scaledWidth, scaledHeight) -> {
            List<AbstractWidget> widgets = Screens.getButtons(screen);
            CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS.forEach(creativeModeTabFilter -> creativeModeTabFilter.modifyWidgetsEvent(screen, widgets::add));
        });

        ScreenEvents.BEFORE_INIT.register((client, screen1, scaledWidth, scaledHeight) -> ScreenEvents.beforeRender(screen1).register((screen2, guiGraphics, mouseX, mouseY, partialTicks) -> CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS.forEach(creativeModeTabFilter -> creativeModeTabFilter.beforeDrawEvent(screen1, guiGraphics, mouseX, mouseY))));

        ClientPlayConnectionEvents.DISCONNECT.register((clientPacketListener, minecraft) -> CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS.forEach(CreativeModeTabFilter::loggingOutEvent));
    }
}
