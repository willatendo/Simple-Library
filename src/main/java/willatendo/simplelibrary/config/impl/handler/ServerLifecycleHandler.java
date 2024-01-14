package willatendo.simplelibrary.config.impl.handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import willatendo.simplelibrary.config.ConfigTracker;
import willatendo.simplelibrary.config.ModConfig;
import willatendo.simplelibrary.mixin.accessor.LevelResourceAccessor;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class ServerLifecycleHandler {
	public static final ResourceLocation BEFORE_PHASE = SimpleUtils.resource("before");
	public static final ResourceLocation AFTER_PHASE = SimpleUtils.resource("after");
	private static final LevelResource SERVER_CONFIG_LEVEL_RESOURCE = LevelResourceAccessor.config$create("serverconfig");

	public static void onServerStarting(MinecraftServer server) {
		ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.SERVER, getServerConfigPath(server));
	}

	public static void onServerStopped(MinecraftServer server) {
		ConfigTracker.INSTANCE.unloadConfigs(ModConfig.Type.SERVER, getServerConfigPath(server));
	}

	public static Path getServerConfigPath(MinecraftServer minecraftServer) {
		Path serverConfig = minecraftServer.getWorldPath(SERVER_CONFIG_LEVEL_RESOURCE);
		if (!Files.isDirectory(serverConfig)) {
			try {
				Files.createDirectories(serverConfig);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return serverConfig;
	}
}
