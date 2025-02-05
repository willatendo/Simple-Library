package willatendo.simplelibrary;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import willatendo.simplelibrary.server.util.SimpleUtils;

@Mod(SimpleUtils.SIMPLE_ID)
public class NeoforgeSimpleLibrary {
    public NeoforgeSimpleLibrary(IEventBus iEventBus) {
        SimpleLibrary.onInitialize();
    }
}