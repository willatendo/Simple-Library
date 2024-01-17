package willatendo.simplelibrary.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

import com.electronwill.nightconfig.core.ConfigSpec;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.google.common.collect.ImmutableMap;

import net.fabricmc.loader.api.FabricLoader;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class SimpleLibraryConfig {
	public static final SimpleLibraryConfig INSTANCE;
	private static final String CONFIG_FILE_NAME = SimpleUtils.ID + ".toml";
	private static final Map<String, Object> CONFIG_VALUES = ImmutableMap.<String, Object>builder().put("defaultConfigsPath", "defaultconfigs").put("forceGlobalServerConfigs", true).put("recreateConfigsWhenParsingFails", true).put("correctConfigValuesFromDefaultConfig", true).build();
	private static final ConfigSpec CONFIG_SPEC;

	static {
		CONFIG_SPEC = new ConfigSpec();
		for (Map.Entry<String, Object> entry : CONFIG_VALUES.entrySet()) {
			CONFIG_SPEC.define(entry.getKey(), entry.getValue());
		}
		INSTANCE = new SimpleLibraryConfig();
	}

	private CommentedFileConfig configData;

	private SimpleLibraryConfig() {
		this.loadFrom(FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME));
	}

	private void loadFrom(Path configFile) {
		this.configData = CommentedFileConfig.builder(configFile, TomlFormat.instance()).sync().onFileNotFound(FileNotFoundAction.copyData(Objects.requireNonNull(this.getClass().getResourceAsStream("/" + CONFIG_FILE_NAME)))).autosave().autoreload().writingMode(WritingMode.REPLACE).build();
		try {
			this.configData.load();
		} catch (ParsingException e) {
			throw new RuntimeException("Failed to load %s config from %s".formatted("SimpleLibrary", configFile), e);
		}
		if (!CONFIG_SPEC.isCorrect(this.configData)) {
			SimpleUtils.LOGGER.warn("Configuration file {} is not correct. Correcting", configFile);
			CONFIG_SPEC.correct(this.configData, (action, path, incorrectValue, correctedValue) -> SimpleUtils.LOGGER.warn("Incorrect key {} was corrected from {} to {}", path, incorrectValue, correctedValue));
		}
		this.configData.save();
		getOrCreateGameRelativePath(Paths.get(this.<String>getValue("defaultConfigsPath")));
	}

	private static Path getOrCreateGameRelativePath(Path path) {
		Path gameFolderPath = FabricLoader.getInstance().getGameDir().resolve(path);
		if (!Files.isDirectory(gameFolderPath)) {
			try {
				Files.createDirectories(gameFolderPath);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return gameFolderPath;
	}

	public <T> T getValue(String key) {
		if (!CONFIG_VALUES.containsKey(key)) {
			throw new IllegalArgumentException("%s is not a know config value key".formatted(key));
		}
		return this.configData.<T>getOptional(key).orElse((T) CONFIG_VALUES.get(key));
	}

	public static Path getDefaultConfigsDirectory() {
		return FabricLoader.getInstance().getGameDir().resolve(INSTANCE.<String>getValue("defaultConfigsPath"));
	}

	public static Path getConfigPath(Path configBasePath, String fileName) {
		Path configPath = configBasePath.resolve(fileName);
		if (INSTANCE.<Boolean>getValue("forceGlobalServerConfigs") && Files.notExists(configPath)) {
			configPath = FabricLoader.getInstance().getConfigDir().resolve(fileName);
		}
		return configPath;
	}
}
