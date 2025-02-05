package willatendo.simplelibrary.client;

import net.fabricmc.api.ClientModInitializer;

public class SimpleLibraryFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SimpleLibraryClient.init();
    }
}
