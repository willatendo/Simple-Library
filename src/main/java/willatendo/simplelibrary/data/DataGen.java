package willatendo.simplelibrary.data;

import java.util.List;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import willatendo.simplelibrary.SimpleLibrary;
import willatendo.simplelibrary.data.DataHelper.Codes;

@EventBusSubscriber(bus = Bus.MOD, modid = SimpleLibrary.ID)
public class DataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataHelper.collectAllData(SimpleLibrary.ID, event, BeesLanguageProvider::new).addTranslation(Codes.SWEDISH, List.of("Hallå")).build();
	}
}
