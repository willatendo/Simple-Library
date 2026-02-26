package ca.willatendo.simplelibrary.client;

import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = SimpleCoreUtils.ID, dist = Dist.CLIENT)
public final class NeoforgeSimpleLibraryClient {
    public NeoforgeSimpleLibraryClient(IEventBus iEventBus) {
        NeoforgeClientModInit neoforgeClientModInit = new NeoforgeClientModInit(iEventBus);
        neoforgeClientModInit.clientEventListener(new SimpleLibraryClientEventListener());
        iEventBus.addListener(FMLClientSetupEvent.class, fmlClientSetupEvent -> CustomRecipeBooks.init());
    }
}