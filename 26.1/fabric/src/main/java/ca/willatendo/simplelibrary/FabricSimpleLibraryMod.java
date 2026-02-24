package ca.willatendo.simplelibrary;

import ca.willatendo.simplelibrary.core.registry.SimpleLibraryBuiltInRegistries;
import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import ca.willatendo.simplelibrary.server.FabricModInit;
import ca.willatendo.simplelibrary.server.FabricSimpleLibraryEventListener;
import ca.willatendo.simplelibrary.server.MonsterRoomHooks;
import ca.willatendo.simplelibrary.server.biome_modifier.SimpleLibraryBiomeModifierSerializers;
import ca.willatendo.simplelibrary.server.conditions.SimpleLibraryConditions;
import ca.willatendo.simplelibrary.server.data_maps.DataMapHooks;
import ca.willatendo.simplelibrary.server.data_maps.DataMapRegister;
import ca.willatendo.simplelibrary.server.data_maps.SimpleLibraryDataMaps;
import net.fabricmc.api.ModInitializer;

public final class FabricSimpleLibraryMod implements ModInitializer {
    @Override
    public void onInitialize() {
        SimpleLibraryDataMaps.init();
        MonsterRoomHooks.init();
        DataMapHooks.init();
        DataMapRegister.initDataMaps();
        SimpleLibraryBuiltInRegistries.init();
        FabricModInit fabricModInit = new FabricModInit(SimpleCoreUtils.ID);
        fabricModInit.register(SimpleLibraryBiomeModifierSerializers.BIOME_MODIFIER_SERIALIZERS, SimpleLibraryConditions.CONDITION_CODECS);
        fabricModInit.eventListener(new FabricSimpleLibraryEventListener());
    }
}
