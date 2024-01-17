/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package willatendo.simplelibrary.config;

import static willatendo.simplelibrary.config.ConfigTracker.CONFIG;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;

import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileWatcher;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.mojang.logging.LogUtils;

import net.fabricmc.loader.api.FabricLoader;
import willatendo.simplelibrary.config.api.ModConfigEvents;
import willatendo.simplelibrary.config.impl.util.ConfigLoadingHelper;

public class ConfigFileTypeHandler {
	private static final Logger LOGGER = LogUtils.getLogger();
	static ConfigFileTypeHandler TOML = new ConfigFileTypeHandler();
	private static final Path DEFAULT_CONFIG_PATH = SimpleLibraryConfig.getDefaultConfigsDirectory();

	public Function<ModConfig, CommentedFileConfig> reader(Path configBasePath) {
		return (c) -> {
			Path configPath = SimpleLibraryConfig.getConfigPath(configBasePath, c.getFileName());
			if (SimpleLibraryConfig.INSTANCE.<Boolean>getValue("forceGlobalServerConfigs") && Files.notExists(configPath)) {
				configPath = FabricLoader.getInstance().getConfigDir().resolve(c.getFileName());
			}
			final CommentedFileConfig configData = CommentedFileConfig.builder(configPath, TomlFormat.instance()).sync().preserveInsertionOrder().autosave().onFileNotFound((newfile, configFormat) -> this.setupConfigFile(c, newfile, configFormat)).writingMode(WritingMode.REPLACE).build();
			LOGGER.debug(ConfigTracker.CONFIG, "Built TOML config for {}", configPath);
			try {
				ConfigLoadingHelper.tryLoadConfigFile(configData);
			} catch (ParsingException ex) {
				throw new ConfigFileTypeHandler.ConfigLoadingException(c, ex);
			}

			ConfigLoadingHelper.tryRegisterDefaultConfig(c.getFileName());
			LOGGER.debug(CONFIG, "Loaded TOML config file {}", configPath);
			try {
				FileWatcher.defaultInstance().addWatch(configPath, new ConfigWatcher(c, configData, Thread.currentThread().getContextClassLoader()));
				LOGGER.debug(CONFIG, "Watching TOML config file {} for changes", configPath);
			} catch (IOException e) {
				throw new RuntimeException("Couldn't watch config file", e);
			}
			return configData;
		};
	}

	public void unload(Path configBasePath, ModConfig modConfig) {
		Path configPath = SimpleLibraryConfig.getConfigPath(configBasePath, modConfig.getFileName());
		try {
			FileWatcher.defaultInstance().removeWatch(configBasePath.resolve(modConfig.getFileName()));
		} catch (RuntimeException e) {
			LOGGER.error("Failed to remove config {} from tracker!", configPath, e);
		}
	}

	private boolean setupConfigFile(ModConfig modConfig, Path file, ConfigFormat<?> conf) throws IOException {
		Files.createDirectories(file.getParent());
		Path p = DEFAULT_CONFIG_PATH.resolve(modConfig.getFileName());
		if (Files.exists(p)) {
			LOGGER.info(CONFIG, "Loading default config file from path {}", p);
			Files.copy(p, file);
		} else {
			Files.createFile(file);
			conf.initEmptyFile(file);
		}
		return true;
	}

	public static void backUpConfig(CommentedFileConfig commentedFileConfig) {
		backUpConfig(commentedFileConfig, 5);
	}

	public static void backUpConfig(CommentedFileConfig commentedFileConfig, int maxBackups) {
		Path bakFileLocation = commentedFileConfig.getNioPath().getParent();
		String bakFileName = FilenameUtils.removeExtension(commentedFileConfig.getFile().getName());
		String bakFileExtension = FilenameUtils.getExtension(commentedFileConfig.getFile().getName()) + ".bak";
		Path bakFile = bakFileLocation.resolve(bakFileName + "-1" + "." + bakFileExtension);
		try {
			for (int i = maxBackups; i > 0; i--) {
				Path oldBak = bakFileLocation.resolve(bakFileName + "-" + i + "." + bakFileExtension);
				if (Files.exists(oldBak)) {
					if (i >= maxBackups)
						Files.delete(oldBak);
					else
						Files.move(oldBak, bakFileLocation.resolve(bakFileName + "-" + (i + 1) + "." + bakFileExtension));
				}
			}
			Files.copy(commentedFileConfig.getNioPath(), bakFile);
		} catch (IOException exception) {
			LOGGER.warn(CONFIG, "Failed to back up config file {}", commentedFileConfig.getNioPath(), exception);
		}
	}

	private static class ConfigWatcher implements Runnable {
		private final ModConfig modConfig;
		private final CommentedFileConfig commentedFileConfig;
		private final ClassLoader realClassLoader;

		private ConfigWatcher(ModConfig modConfig, CommentedFileConfig commentedFileConfig, ClassLoader classLoader) {
			this.modConfig = modConfig;
			this.commentedFileConfig = commentedFileConfig;
			this.realClassLoader = classLoader;
		}

		@Override
		public void run() {
			Thread.currentThread().setContextClassLoader(this.realClassLoader);
			if (!this.modConfig.getConfigSpec().isCorrecting()) {
				try {
					this.commentedFileConfig.load();
					if (!this.modConfig.getConfigSpec().isCorrect(this.commentedFileConfig)) {
						LOGGER.warn(CONFIG, "Configuration file {} is not correct. Correcting", this.commentedFileConfig.getFile().getAbsolutePath());
						ConfigFileTypeHandler.backUpConfig(this.commentedFileConfig);
						this.modConfig.getConfigSpec().correct(this.commentedFileConfig);
						this.commentedFileConfig.save();
					}
				} catch (ParsingException ex) {
					throw new ConfigLoadingException(this.modConfig, ex);
				}
				LOGGER.debug(CONFIG, "Config file {} changed, sending notifies", this.modConfig.getFileName());
				this.modConfig.getConfigSpec().afterReload();
				ModConfigEvents.reloading(this.modConfig.getModId()).invoker().onModConfigReloading(this.modConfig);
			}
		}
	}

	private static class ConfigLoadingException extends RuntimeException {
		public ConfigLoadingException(ModConfig config, Exception cause) {
			super("Failed loading config file " + config.getFileName() + " of type " + config.getType() + " for modid " + config.getModId(), cause);
		}
	}
}