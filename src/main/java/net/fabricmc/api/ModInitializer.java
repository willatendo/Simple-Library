package net.fabricmc.api;

/*
 * Used as compatibility layer
 */

@Deprecated
@FunctionalInterface
public interface ModInitializer {
	void onInitialize();
}
