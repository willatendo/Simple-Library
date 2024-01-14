/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package willatendo.simplelibrary.config.impl.network.config;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import willatendo.simplelibrary.config.ConfigTracker;
import willatendo.simplelibrary.config.ModConfig;
import willatendo.simplelibrary.server.util.SimpleUtils;

public final class ConfigSync {
	public static final ResourceLocation SYNC_CONFIGS_CHANNEL = SimpleUtils.resource("sync_configs");
	public static final ResourceLocation ESTABLISH_MODDED_CONNECTION_CHANNEL = SimpleUtils.resource("modded_connection");

	private ConfigSync() {
	}

	public static List<Pair<String, FriendlyByteBuf>> writeSyncedConfigs() {
		final Map<String, byte[]> configData = ConfigTracker.INSTANCE.configSets().get(ModConfig.Type.SERVER).stream().collect(Collectors.toMap(ModConfig::getFileName, config -> {
			try {
				return Files.readAllBytes(config.getFullPath());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}));
		ConfigTracker.INSTANCE.configSets().get(ModConfig.Type.SERVER).forEach(config -> {
			try {
				configData.put(config.getFileName(), Files.readAllBytes(config.getFullPath()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		return configData.entrySet().stream().map(entry -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeUtf(entry.getKey());
			buf.writeByteArray(entry.getValue());
			return Pair.of("Config " + entry.getKey(), buf);
		}).collect(Collectors.toList());
	}

	public static void onSyncConfigs(MinecraftServer server, ServerLoginPacketListenerImpl handler, boolean understood, FriendlyByteBuf buf) {
		if (!understood) {
			return;
		}
		SimpleUtils.LOGGER.debug("Received acknowledgement for config sync for {} from client", buf.readUtf(32767));
	}

	public static void onEstablishModdedConnection(MinecraftServer server, ServerLoginPacketListenerImpl handler, boolean understood, FriendlyByteBuf buf) {
		SimpleUtils.LOGGER.debug("Received acknowledgement for modded connection marker from client");
	}

	public static void unloadSyncedConfig() {
		ConfigTracker.INSTANCE.configSets().get(ModConfig.Type.SERVER).forEach(config -> config.acceptSyncedConfig(null));
	}
}