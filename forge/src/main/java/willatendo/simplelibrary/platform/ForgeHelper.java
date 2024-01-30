package willatendo.simplelibrary.platform;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ForgeHelper implements ModloaderHelper {
	@Override
	public boolean isDevEnviroment() {
		return !FMLEnvironment.production;
	}

	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public CreativeModeTab.Builder createCreativeModeTab() {
		return CreativeModeTab.builder();
	}
}
