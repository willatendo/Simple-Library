package willatendo.simplelibrary.client;

import java.util.function.Consumer;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.FriendlyByteBuf;
import willatendo.simplelibrary.config.impl.network.client.config.ConfigSyncClient;
import willatendo.simplelibrary.config.impl.network.config.ConfigSync;

public class SimpleLibraryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerMessages();
	}

	private static void registerMessages() {
		ClientLoginNetworking.registerGlobalReceiver(ConfigSync.SYNC_CONFIGS_CHANNEL, (Minecraft client1, ClientHandshakePacketListenerImpl handler1, FriendlyByteBuf buf1, Consumer<GenericFutureListener<? extends Future<? super Void>>> listenerAdder1) -> {
			return ConfigSyncClient.onSyncConfigs(client1, handler1, buf1);
		});
		ClientLoginNetworking.registerGlobalReceiver(ConfigSync.ESTABLISH_MODDED_CONNECTION_CHANNEL, (client, handler, buf, listenerAdder) -> ConfigSyncClient.onEstablishModdedConnection(client, handler, buf));
	}
}