package willatendo.simplelibrary.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import willatendo.simplelibrary.client.SimpleLibraryClient;
import willatendo.simplelibrary.client.event.registry.NeoforgeModelLayerRegister;
import willatendo.simplelibrary.client.event.registry.NeoforgeModelRegister;
import willatendo.simplelibrary.server.util.SimpleUtils;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = SimpleUtils.SIMPLE_ID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void fmlClientSetupEvent(FMLClientSetupEvent event) {
        SimpleLibraryClient.init();
    }

    @SubscribeEvent
    public static void entityRenderersEvent_RegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        SimpleLibraryClient.modelEvent(new NeoforgeModelRegister(event));
    }

    @SubscribeEvent
    public static void entityRenderersEvent_RegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        SimpleLibraryClient.modelLayerEvent(new NeoforgeModelLayerRegister(event));
    }
}
