package willatendo.simplelibrary.client;

import net.fabricmc.api.ClientModInitializer;
import willatendo.simplelibrary.client.event.registry.FabricModelLayerRegister;
import willatendo.simplelibrary.client.event.registry.FabricModelRegister;

public class SimpleLibraryFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SimpleLibraryClient.init();

        SimpleLibraryClient.modelEvent(new FabricModelRegister());
        SimpleLibraryClient.modelLayerEvent(new FabricModelLayerRegister());
    }
}
