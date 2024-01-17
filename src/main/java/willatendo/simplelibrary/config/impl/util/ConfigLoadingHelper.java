package willatendo.simplelibrary.config.impl.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BooleanSupplier;

import org.apache.commons.io.FilenameUtils;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.fabricmc.loader.api.FabricLoader;
import willatendo.simplelibrary.config.SimpleLibraryConfig;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class ConfigLoadingHelper {
	public static final Map<String, Map<String, Object>> DEFAULT_CONFIG_VALUES = Maps.newConcurrentMap();

	public static void tryLoadConfigFile(FileConfig fileConfig) {
		tryLoadConfigFile(fileConfig, () -> SimpleLibraryConfig.INSTANCE.<Boolean>getValue("recreateConfigsWhenParsingFails"));
	}

	private static void tryLoadConfigFile(FileConfig fileConfig, BooleanSupplier booleanSupplier) {
		try {
			fileConfig.load();
		} catch (ParsingException e) {
			if (booleanSupplier.getAsBoolean()) {
				try {
					backUpConfig(fileConfig.getNioPath(), 5);
					Files.delete(fileConfig.getNioPath());
					fileConfig.load();
					SimpleUtils.LOGGER.warn("Configuration file {} could not be parsed. Correcting", fileConfig.getNioPath());
					return;
				} catch (Throwable t) {
					e.addSuppressed(t);
				}
			}
			throw e;
		}
	}

	public static void tryRegisterDefaultConfig(String fileName) {
		if (!SimpleLibraryConfig.INSTANCE.<Boolean>getValue("correctConfigValuesFromDefaultConfig")) {
			return;
		}
		Path path = FabricLoader.getInstance().getConfigDir().resolve(fileName);
		if (Files.exists(path)) {
			try (CommentedFileConfig config = CommentedFileConfig.of(path)) {
				config.load();
				Map<String, Object> values = config.valueMap();
				if (values != null && !values.isEmpty()) {
					DEFAULT_CONFIG_VALUES.put(fileName.intern(), ImmutableMap.copyOf(values));
				}
				SimpleUtils.LOGGER.debug("Loaded default config values for future corrections from file at path {}", path);
			} catch (Exception e) {
			}
		}
	}

	public static void backUpConfig(Path commentedFileConfig, int maxBackups) {
		if (!Files.exists(commentedFileConfig)) {
			return;
		}
		Path bakFileLocation = commentedFileConfig.getParent();
		String bakFileName = FilenameUtils.removeExtension(commentedFileConfig.getFileName().toString());
		String bakFileExtension = FilenameUtils.getExtension(commentedFileConfig.getFileName().toString()) + ".bak";
		Path bakFile = bakFileLocation.resolve(bakFileName + "-1" + "." + bakFileExtension);
		try {
			for (int i = maxBackups; i > 0; i--) {
				Path oldBak = bakFileLocation.resolve(bakFileName + "-" + i + "." + bakFileExtension);
				if (Files.exists(oldBak)) {
					if (i >= maxBackups) {
						Files.delete(oldBak);
					} else {
						Files.move(oldBak, bakFileLocation.resolve(bakFileName + "-" + (i + 1) + "." + bakFileExtension));
					}
				}
			}
			Files.copy(commentedFileConfig, bakFile);
		} catch (IOException e) {
			SimpleUtils.LOGGER.warn("Failed to back up config file {}", commentedFileConfig, e);
		}
	}
}
