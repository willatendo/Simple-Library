package willatendo.simplelibrary.server.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import willatendo.simplelibrary.server.SimpleBuiltInRegistries;
import willatendo.simplelibrary.server.event.registry.NeoforgeNewRegistryRegister;
import willatendo.simplelibrary.server.util.SimpleUtils;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = SimpleUtils.SIMPLE_ID)
public class ModEvents {
    @SubscribeEvent
    public static void newRegistryEvent(NewRegistryEvent event) {
        new NeoforgeNewRegistryRegister(event).register(SimpleBuiltInRegistries.BOAT_TYPES);
    }
}
