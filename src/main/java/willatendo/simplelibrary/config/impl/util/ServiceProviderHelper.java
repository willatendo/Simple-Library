package willatendo.simplelibrary.config.impl.util;

import java.util.ServiceLoader;

public final class ServiceProviderHelper {
	public static <T> T load(Class<T> clazz) {
		return ServiceLoader.load(clazz, ServiceProviderHelper.class.getClassLoader()).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
	}
}
