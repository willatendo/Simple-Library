package willatendo.simplelibrary.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import willatendo.simplelibrary.client.filter.CreativeModeTabFilter;

@EventBusSubscriber(Dist.CLIENT)
public class NeoforgeSimpleLibraryClient {
    @SubscribeEvent
    public static void screenEvent_Init_Post(ScreenEvent.Init.Post event) {
        CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS.forEach(creativeModeTabFilter -> creativeModeTabFilter.modifyWidgetsEvent(event.getScreen(), event::addListener));
    }

    @SubscribeEvent
    public static void screenEvent_Render_Pre(ScreenEvent.Render.Pre event) {
        CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS.forEach(creativeModeTabFilter -> creativeModeTabFilter.beforeDrawEvent(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY()));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void screenEvent_Closing(ScreenEvent.Closing event) {
        CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS.forEach(creativeModeTabFilter -> creativeModeTabFilter.closedEvent(event.getScreen()));
    }

    @SubscribeEvent
    public static void clientPlayerNetworkEvent_LoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        CreativeModeTabFilter.CREATIVE_MODE_TAB_FILTERS.forEach(CreativeModeTabFilter::loggingOutEvent);
    }
}
