package willatendo.simplelibrary.client.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import willatendo.simplelibrary.client.SimpleLibraryClient;
import willatendo.simplelibrary.server.util.SimpleUtils;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = SimpleUtils.SIMPLE_ID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void entityRenderersEvent_RegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        SimpleLibraryClient.modelEvent(new ForgeModelRegister(event));
    }

    @SubscribeEvent
    public static void entityRenderersEvent_RegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        SimpleLibraryClient.modelLayerEvent(new ForgeModelLayerRegister(event));
    }
}
