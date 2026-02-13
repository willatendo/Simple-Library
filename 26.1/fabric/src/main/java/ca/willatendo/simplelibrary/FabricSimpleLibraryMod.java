package ca.willatendo.simplelibrary;

import ca.willatendo.simplelibrary.core.registry.SimpleLibraryBuiltInRegistries;
import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import ca.willatendo.simplelibrary.server.FabricModInit;
import ca.willatendo.simplelibrary.server.FabricSimpleLibraryEventListener;
import ca.willatendo.simplelibrary.server.biome_modifier.SimpleLibraryBiomeModifierSerializers;
import ca.willatendo.simplelibrary.server.conditions.SimpleLibraryConditions;
import net.fabricmc.api.ModInitializer;

public final class FabricSimpleLibraryMod implements ModInitializer {
    @Override
    public void onInitialize() {
        SimpleLibraryBuiltInRegistries.init();
        FabricModInit fabricModInit = new FabricModInit(SimpleCoreUtils.ID);
        SimpleLibraryMod.modInit(fabricModInit);
        fabricModInit.register(SimpleLibraryBiomeModifierSerializers.BIOME_MODIFIER_SERIALIZERS, SimpleLibraryConditions.CONDITION_CODECS);
        fabricModInit.eventListener(new FabricSimpleLibraryEventListener());
    }
}
