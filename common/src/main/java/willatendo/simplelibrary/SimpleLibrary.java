package willatendo.simplelibrary;

import willatendo.simplelibrary.server.SimpleBuiltInRegistries;
import willatendo.simplelibrary.server.block.entity.SimpleBlockEntityTypes;
import willatendo.simplelibrary.server.entity.SimpleBoatTypes;
import willatendo.simplelibrary.server.entity.SimpleEntityTypes;
import willatendo.simplelibrary.server.event.SimpleRegistryRegister;

public class SimpleLibrary {
    public static void onInitialize(SimpleRegistryRegister simpleRegistryRegister) {
        SimpleBuiltInRegistries.init();

        simpleRegistryRegister.register(SimpleBlockEntityTypes.BLOCK_ENTITY_TYPES);
        simpleRegistryRegister.register(SimpleBoatTypes.BOAT_TYPES);
        simpleRegistryRegister.register(SimpleEntityTypes.ENTITY_TYPES);
    }
}
