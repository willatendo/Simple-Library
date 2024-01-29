package willatendo.simplelibrary.platform;

import willatendo.simplelibrary.server.util.SimpleUtils;

public interface ModloaderHelper {
	public static final ModloaderHelper INSTANCE = SimpleUtils.loadModloaderHelper(ModloaderHelper.class);

	// Internal Use
}
