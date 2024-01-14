/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package willatendo.simplelibrary.config.impl.network.client;

import net.minecraft.network.Connection;
import willatendo.simplelibrary.config.ConfigTracker;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class NetworkHooks {
	private static boolean isVanillaConnection = true;

	public static void setModdedConnection() {
		isVanillaConnection = false;
	}

	private static void setVanillaConnection() {
		isVanillaConnection = true;
	}

	public static boolean isVanillaConnection(Connection conection) {
		return isVanillaConnection;
	}

	public static void handleClientLoginSuccess(Connection conection) {
		if (isVanillaConnection(conection)) {
			SimpleUtils.LOGGER.debug("Connected to a vanilla server. Catching up missing behaviour.");
			ConfigTracker.INSTANCE.loadDefaultServerConfigs();
		} else {
			setVanillaConnection();
			SimpleUtils.LOGGER.debug("Connected to a modded server.");
		}
	}
}
