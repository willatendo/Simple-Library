/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package willatendo.simplelibrary.config.impl.network.client.config;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.FriendlyByteBuf;
import willatendo.simplelibrary.config.ConfigTracker;
import willatendo.simplelibrary.config.impl.network.client.NetworkHooks;
import willatendo.simplelibrary.server.util.SimpleUtils;

public final class ConfigSyncClient {
	private ConfigSyncClient() {
	}

	public static CompletableFuture<@Nullable FriendlyByteBuf> onSyncConfigs(Minecraft minecraft, ClientHandshakePacketListenerImpl clientHandshakePacketListenerImpl, FriendlyByteBuf friendlyByteBuf) {
		String fileName = receiveSyncedConfig(friendlyByteBuf);
		SimpleUtils.LOGGER.debug("Received config sync for {} from server", fileName);
		FriendlyByteBuf response = new FriendlyByteBuf(Unpooled.buffer());
		response.writeUtf(fileName);
		SimpleUtils.LOGGER.debug("Sent config sync for {} to server", fileName);
		return CompletableFuture.completedFuture(response);
	}

	public static CompletableFuture<@Nullable FriendlyByteBuf> onEstablishModdedConnection(Minecraft minecraft, ClientHandshakePacketListenerImpl clientHandshakePacketListenerImpl, FriendlyByteBuf friendlyByteBuf) {
		SimpleUtils.LOGGER.debug("Received modded connection marker from server");
		NetworkHooks.setModdedConnection();
		return CompletableFuture.completedFuture(new FriendlyByteBuf(Unpooled.buffer()));
	}

	private static String receiveSyncedConfig(FriendlyByteBuf friendlyByteBuf) {
		String fileName = friendlyByteBuf.readUtf(32767);
		byte[] fileData = friendlyByteBuf.readByteArray();
		if (!Minecraft.getInstance().isLocalServer()) {
			Optional.ofNullable(ConfigTracker.INSTANCE.fileMap().get(fileName)).ifPresent(config -> config.acceptSyncedConfig(fileData));
		}
		return fileName;
	}
}