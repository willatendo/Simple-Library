package willatendo.simplelibrary;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.FriendlyByteBuf;
import willatendo.simplelibrary.config.ModConfig;
import willatendo.simplelibrary.config.ModConfigSpec;
import willatendo.simplelibrary.config.api.ForgeConfigRegistry;
import willatendo.simplelibrary.config.impl.handler.ServerLifecycleHandler;
import willatendo.simplelibrary.config.impl.network.config.ConfigSync;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class SimpleLibrary implements ModInitializer {
	@Override
	public void onInitialize() {
		registerMessages();
		registerHandlers();
		
		ForgeConfigRegistry.INSTANCE.register(SimpleUtils.ID, ModConfig.Type.SERVER, new ModConfigSpec.Builder().comment("Hello world").define("dummy_optoin", true).next().build());
	}

	private static void registerMessages() {
		ServerLoginConnectionEvents.QUERY_START.register((handler2, server2, sender, synchronizer2) -> {
			List<Pair<String, FriendlyByteBuf>> pairs = ConfigSync.writeSyncedConfigs();
			for (Pair<String, FriendlyByteBuf> pair : pairs) {
				synchronizer2.waitFor(server2.submit(() -> sender.sendPacket(ConfigSync.SYNC_CONFIGS_CHANNEL, pair.getValue())));
			}
			synchronizer2.waitFor(server2.submit(() -> sender.sendPacket(ConfigSync.ESTABLISH_MODDED_CONNECTION_CHANNEL, PacketByteBufs.create())));
		});
		ServerLoginNetworking.registerGlobalReceiver(ConfigSync.SYNC_CONFIGS_CHANNEL, (server1, handler1, understood1, buf1, synchronizer1, responseSender1) -> {
			ConfigSync.onSyncConfigs(server1, handler1, understood1, buf1);
		});
		ServerLoginNetworking.registerGlobalReceiver(ConfigSync.ESTABLISH_MODDED_CONNECTION_CHANNEL, (server, handler, understood, buf, synchronizer, responseSender) -> {
			ConfigSync.onEstablishModdedConnection(server, handler, understood, buf);
		});
	}

	private static void registerHandlers() {
		ServerLifecycleEvents.SERVER_STARTING.addPhaseOrdering(ServerLifecycleHandler.BEFORE_PHASE, Event.DEFAULT_PHASE);
		ServerLifecycleEvents.SERVER_STARTING.register(ServerLifecycleHandler.BEFORE_PHASE, ServerLifecycleHandler::onServerStarting);
		ServerLifecycleEvents.SERVER_STOPPED.addPhaseOrdering(Event.DEFAULT_PHASE, ServerLifecycleHandler.AFTER_PHASE);
		ServerLifecycleEvents.SERVER_STOPPED.register(ServerLifecycleHandler.AFTER_PHASE, ServerLifecycleHandler::onServerStopped);
	}
}
