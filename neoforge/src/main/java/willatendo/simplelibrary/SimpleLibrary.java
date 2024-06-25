package willatendo.simplelibrary;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import willatendo.simplelibrary.server.event.EventsHolder;
import willatendo.simplelibrary.server.event.NeoForgeEvents;
import willatendo.simplelibrary.server.util.SimpleUtils;

@Mod(SimpleUtils.SIMPLE_ID)
public class SimpleLibrary {
    public SimpleLibrary(ModContainer modContainer, IEventBus iEventBus) {
        iEventBus.register(new NeoForgeEvents(new EventsHolder()));
    }
}