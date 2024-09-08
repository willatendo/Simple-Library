package willatendo.simplelibrary.server.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.NewRegistryEvent;
import willatendo.simplelibrary.server.SimpleBuiltInRegistries;
import willatendo.simplelibrary.server.SimpleRegistries;
import willatendo.simplelibrary.server.event.registry.ForgeNewRegistryRegister;
import willatendo.simplelibrary.server.util.SimpleUtils;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = SimpleUtils.SIMPLE_ID)
public class ModEvents {
    @SubscribeEvent
    public static void newRegistryEvent(NewRegistryEvent event) {
        new ForgeNewRegistryRegister(event).register(SimpleBuiltInRegistries.BOAT_TYPES, SimpleRegistries.BOAT_TYPES);
    }
}
