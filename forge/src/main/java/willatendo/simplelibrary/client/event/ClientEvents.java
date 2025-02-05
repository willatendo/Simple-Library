package willatendo.simplelibrary.client.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import willatendo.simplelibrary.client.SimpleLibraryClient;
import willatendo.simplelibrary.server.util.SimpleUtils;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = SimpleUtils.SIMPLE_ID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void fmlClientSetupEvent(FMLClientSetupEvent event) {
        SimpleLibraryClient.init();
    }
}
