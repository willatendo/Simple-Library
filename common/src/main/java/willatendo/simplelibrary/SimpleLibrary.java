package willatendo.simplelibrary;

import willatendo.simplelibrary.server.CommonConfigRegister;
import willatendo.simplelibrary.server.SimpleBuiltInRegistries;
import willatendo.simplelibrary.server.block.entity.SimpleBlockEntityTypes;
import willatendo.simplelibrary.server.entity.SimpleBoatTypes;
import willatendo.simplelibrary.server.entity.SimpleEntityTypes;
import willatendo.simplelibrary.server.event.registry.SimpleRegistryRegister;

public class SimpleLibrary {
    public static void onInitialize(SimpleRegistryRegister simpleRegistryRegister) {
        CommonConfigRegister.registerAll();

        SimpleBuiltInRegistries.init();

        simpleRegistryRegister.register(SimpleBlockEntityTypes.BLOCK_ENTITY_TYPES);
        simpleRegistryRegister.register(SimpleBoatTypes.BOAT_TYPES);
        simpleRegistryRegister.register(SimpleEntityTypes.ENTITY_TYPES);
    }
}
