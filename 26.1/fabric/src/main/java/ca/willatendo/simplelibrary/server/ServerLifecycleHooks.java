package ca.willatendo.simplelibrary.server;

import net.minecraft.server.MinecraftServer;

public final class ServerLifecycleHooks {
    private static MinecraftServer currentServer;

    public static void handleServerAboutToStart(MinecraftServer minecraftServer) {
        currentServer = minecraftServer;
    }

    public static void handleServerStopped(MinecraftServer minecraftServer) {
        ServerLifecycleHooks.currentServer = minecraftServer;
    }

    public static MinecraftServer getCurrentServer() {
        return ServerLifecycleHooks.currentServer;
    }
}
