package ca.willatendo.simplelibrary.network.utils;

import ca.willatendo.simplelibrary.platform.SimpleLibraryPlatformHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public final class NetworkUtils {
    private NetworkUtils() {
    }

    public static void sendToClient(ServerPlayer serverPlayer, CustomPacketPayload customPacketPayload) {
        SimpleLibraryPlatformHelper.INSTANCE.sendToClient(serverPlayer, customPacketPayload);
    }

    public static void sendToServer(CustomPacketPayload customPacketPayload) {
        SimpleLibraryPlatformHelper.INSTANCE.sendToServer(customPacketPayload);
    }
}
