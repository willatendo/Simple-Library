/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package willatendo.simplelibrary.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;

import net.fabricmc.loader.api.FabricLoader;
import willatendo.simplelibrary.config.api.ModConfigEvents;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class ConfigTracker {
	static final Marker CONFIG = MarkerFactory.getMarker("CONFIG");
	public static final ConfigTracker INSTANCE = new ConfigTracker();
	private final ConcurrentHashMap<String, ModConfig> fileMap;
	private final EnumMap<ModConfig.Type, Set<ModConfig>> configSets;
	private final ConcurrentHashMap<String, Map<ModConfig.Type, Collection<ModConfig>>> configsByMod;

	private ConfigTracker() {
		this.fileMap = new ConcurrentHashMap<>();
		this.configSets = new EnumMap<>(ModConfig.Type.class);
		this.configsByMod = new ConcurrentHashMap<>();
		this.configSets.put(ModConfig.Type.CLIENT, Collections.synchronizedSet(new LinkedHashSet<>()));
		this.configSets.put(ModConfig.Type.COMMON, Collections.synchronizedSet(new LinkedHashSet<>()));
		this.configSets.put(ModConfig.Type.SERVER, Collections.synchronizedSet(new LinkedHashSet<>()));
	}

	protected void trackConfig(ModConfig modConfig) {
		if (this.fileMap.containsKey(modConfig.getFileName())) {
			SimpleUtils.LOGGER.error(CONFIG, "Detected config file conflict {} between {} and {}", modConfig.getFileName(), this.fileMap.get(modConfig.getFileName()).getModId(), modConfig.getModId());
			throw new RuntimeException("Config conflict detected!");
		}
		this.fileMap.put(modConfig.getFileName(), modConfig);
		this.configSets.get(modConfig.getType()).add(modConfig);
		this.configsByMod.computeIfAbsent(modConfig.getModId(), (k) -> new EnumMap<>(ModConfig.Type.class)).computeIfAbsent(modConfig.getType(), type -> new ArrayList<>()).add(modConfig);
		SimpleUtils.LOGGER.debug(CONFIG, "Config file {} for {} tracking", modConfig.getFileName(), modConfig.getModId());
		if (modConfig.getType() != ModConfig.Type.SERVER) {
			this.openConfig(modConfig, FabricLoader.getInstance().getConfigDir());
		}
	}

	public void loadConfigs(ModConfig.Type type, Path configBasePath) {
		SimpleUtils.LOGGER.debug(CONFIG, "Loading configs type {}", type);
		this.configSets.get(type).forEach(config -> this.openConfig(config, configBasePath));
	}

	public void unloadConfigs(ModConfig.Type type, Path configBasePath) {
		SimpleUtils.LOGGER.debug(CONFIG, "Unloading configs type {}", type);
		this.configSets.get(type).forEach(config -> this.closeConfig(config, configBasePath));
	}

	private void openConfig(ModConfig modConfig, Path configBasePath) {
		SimpleUtils.LOGGER.trace(CONFIG, "Loading config file type {} at {} for {}", modConfig.getType(), modConfig.getFileName(), modConfig.getModId());
		CommentedFileConfig configData = modConfig.getHandler().reader(configBasePath).apply(modConfig);
		modConfig.setConfigData(configData);
		ModConfigEvents.loading(modConfig.getModId()).invoker().onModConfigLoading(modConfig);
		modConfig.save();
	}

	private void closeConfig(ModConfig modConfig, Path configBasePath) {
		if (modConfig.getConfigData() != null) {
			SimpleUtils.LOGGER.trace(CONFIG, "Closing config file type {} at {} for {}", modConfig.getType(), modConfig.getFileName(), modConfig.getModId());
			modConfig.getHandler().unload(configBasePath, modConfig);
			ModConfigEvents.unloading(modConfig.getModId()).invoker().onModConfigUnloading(modConfig);
			modConfig.save();
			modConfig.setConfigData(null);
		}
	}

	public void loadDefaultServerConfigs() {
		this.configSets.get(ModConfig.Type.SERVER).forEach(modConfig -> {
			final CommentedConfig commentedConfig = CommentedConfig.inMemory();
			modConfig.getConfigSpec().correct(commentedConfig);
			modConfig.setConfigData(commentedConfig);
			ModConfigEvents.loading(modConfig.getModId()).invoker().onModConfigLoading(modConfig);
		});
	}

	@Nullable
	public String getConfigFileName(String modId, ModConfig.Type type) {
		List<String> fileNames = this.getConfigFileNames(modId, type);
		return fileNames.isEmpty() ? null : fileNames.get(0);
	}

	public List<String> getConfigFileNames(String modId, ModConfig.Type type) {
		return Optional.ofNullable(this.configsByMod.get(modId)).map(map -> map.get(type)).map(configs -> configs.stream().filter(config -> config.getConfigData() instanceof FileConfig).map(ModConfig::getFullPath).map(Object::toString).toList()).orElse(List.of());
	}

	public Map<ModConfig.Type, Set<ModConfig>> configSets() {
		return this.configSets;
	}

	public ConcurrentHashMap<String, ModConfig> fileMap() {
		return this.fileMap;
	}
}