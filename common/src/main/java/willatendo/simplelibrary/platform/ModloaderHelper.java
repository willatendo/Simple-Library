package willatendo.simplelibrary.platform;

import net.minecraft.world.item.CreativeModeTab;
import willatendo.simplelibrary.server.util.SimpleUtils;

public interface ModloaderHelper {
	public static final ModloaderHelper INSTANCE = SimpleUtils.loadModloaderHelper(ModloaderHelper.class);

	// Internal Use
	boolean isDevEnviroment();

	boolean isModLoaded(String modId);

	CreativeModeTab.Builder createCreativeModeTab();
}
