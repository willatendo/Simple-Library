package willatendo.simplelibrary;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegisterEvent;
import willatendo.simplelibrary.server.registry.ForgeRegister;
import willatendo.simplelibrary.server.util.SimpleUtils;

@EventBusSubscriber(bus = Bus.MOD, modid = SimpleUtils.SIMPLE_ID)
public class Test {
	@SubscribeEvent
	public static void register(RegisterEvent registerEvent) {
		ForgeRegister.register(registerEvent, Items.ITEMS);
	}
}
