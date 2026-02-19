package ca.willatendo.simplelibrary;

import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import ca.willatendo.simplelibrary.server.EventListener;
import ca.willatendo.simplelibrary.server.NeoforgeModInit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(SimpleCoreUtils.ID)
public final class NeoforgeSimpleLibraryMod {
    public NeoforgeSimpleLibraryMod(IEventBus iEventBus) {
        NeoforgeModInit neoforgeModInit = new NeoforgeModInit(iEventBus);
        neoforgeModInit.eventListener(new EventListener() {
        });
    }
}