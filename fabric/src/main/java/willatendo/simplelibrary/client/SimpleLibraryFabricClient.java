package willatendo.simplelibrary.client;

import net.fabricmc.api.ClientModInitializer;
import willatendo.simplelibrary.client.event.FabricModelLayerRegister;
import willatendo.simplelibrary.client.event.FabricModelRegister;

public class SimpleLibraryFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SimpleLibraryClient.modelEvent(new FabricModelRegister());
        SimpleLibraryClient.modelLayerEvent(new FabricModelLayerRegister());
    }
}
