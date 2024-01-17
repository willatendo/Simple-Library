/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package willatendo.simplelibrary.config;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Locale;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.StringRepresentable;
import willatendo.simplelibrary.config.api.ModConfigEvents;

public class ModConfig {
	private final Type type;
	private final ConfigSpec<?> configSpec;
	private final String fileName;
	private final String modId;
	private final ConfigFileTypeHandler configHandler;
	private CommentedConfig configData;

	public ModConfig(Type type, ConfigSpec<?> configSpec, String modId, String fileName) {
		this.type = type;
		this.configSpec = configSpec;
		this.fileName = fileName;
		if (!FabricLoader.getInstance().isModLoaded(modId)) {
			throw new IllegalArgumentException("No mod with id '%s'".formatted(modId));
		}
		this.modId = modId;
		this.configHandler = ConfigFileTypeHandler.TOML;
		ConfigTracker.INSTANCE.trackConfig(this);
	}

	public ModConfig(ModConfig.Type type, ConfigSpec<?> spec, String modId) {
		this(type, spec, modId, defaultConfigName(type, modId));
	}

	private static String defaultConfigName(Type type, String modId) {
		return String.format(Locale.ROOT, "%s-%s.toml", modId, type.extension());
	}

	public Type getType() {
		return this.type;
	}

	public String getFileName() {
		return this.fileName;
	}

	public ConfigFileTypeHandler getHandler() {
		return this.configHandler;
	}

	public <T extends ConfigSpec<T>> ConfigSpec<T> getConfigSpec() {
		return (ConfigSpec<T>) this.configSpec;
	}

	public String getModId() {
		return this.modId;
	}

	public CommentedConfig getConfigData() {
		return this.configData;
	}

	void setConfigData(CommentedConfig commentedConfig) {
		this.configData = commentedConfig;
		this.configSpec.acceptConfig(this.configData);
	}

	public void save() {
		((CommentedFileConfig) this.configData).save();
	}

	public Path getFullPath() {
		return ((CommentedFileConfig) this.configData).getNioPath();
	}

	public void acceptSyncedConfig(byte[] bytes) {
		if (bytes != null) {
			this.setConfigData(TomlFormat.instance().createParser().parse(new ByteArrayInputStream(bytes)));
			ModConfigEvents.reloading(this.getModId()).invoker().onModConfigReloading(this);
		} else {
			this.setConfigData(null);
			ModConfigEvents.unloading(this.getModId()).invoker().onModConfigUnloading(this);
		}
	}

	public enum Type implements StringRepresentable {
		COMMON,
		CLIENT,
		SERVER;

		public String extension() {
			return this.name().toLowerCase(Locale.ROOT);
		}

		@Override
		public String getSerializedName() {
			return this.extension();
		}
	}
}