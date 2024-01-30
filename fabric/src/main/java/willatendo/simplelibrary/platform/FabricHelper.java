package willatendo.simplelibrary.platform;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.CreativeModeTab.Builder;

public class FabricHelper implements ModloaderHelper {
	@Override
	public boolean isDevEnviroment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public Builder createCreativeModeTab() {
		return FabricItemGroup.builder();
	}
}
