package willatendo.simplelibrary.helper;

import java.util.ServiceLoader;

final class ModloaderHelperImpl {
	protected static final ModloaderHelper PLATFORM = load(ModloaderHelper.class);

	private static <T> T load(Class<T> clazz) {
		return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
	}
}
