package willatendo.simplelibrary;

import net.fabricmc.api.ModInitializer;
import willatendo.simplelibrary.server.entity.SimpleEntityDataSerializers;
import willatendo.simplelibrary.server.event.FabricSimpleRegistryRegister;

public class FabricSimpleLibrary implements ModInitializer {
    @Override
    public void onInitialize() {
        SimpleEntityDataSerializers.init();

        SimpleLibrary.onInitialize(new FabricSimpleRegistryRegister());
    }
}
