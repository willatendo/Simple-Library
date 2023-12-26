package willatendo.simplelibrary.server.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.fabricmc.loader.api.FabricLoader;
import willatendo.simplelibrary.server.util.SimpleUtils;

public final class SimpleConfig {
	private final File configPath = new File(FabricLoader.getInstance().getConfigDir().toString());
	private final Properties properties = new Properties();
	private final Map<String, ConfigValue> values = new HashMap<>();

	private final String configName;
	private final File configFile;

	public SimpleConfig(String configName) {
		this.configName = configName;
		this.configFile = new File(this.configPath, configName + ".toml");
	}

	private void saveConfig() {
		try {
			this.save(this.values);
		} catch (IOException e) {
			SimpleUtils.LOGGER.error("Cannot Save Config: " + this.configName + "! Error!");
		}
	}

	private void save(Map<String, ? extends ConfigValue> values) throws IOException {
		if (this.configFile.exists()) {
			this.configFile.delete();
		}

		this.configFile.createNewFile();
		FileOutputStream fileOutputStream = new FileOutputStream(this.configFile);
		Properties properties = new Properties();
		for (Map.Entry<String, ? extends ConfigValue> toSave : values.entrySet()) {
			properties.setProperty(toSave.getKey(), toSave.getValue().toBasicString());

		}
		properties.store(fileOutputStream, this.configName);
		fileOutputStream.close();
	}

	public void loadConfig() {
		if (this.configFile.exists()) {
			Properties savedEntries = new Properties();
			try {
				FileInputStream fileInputStream = new FileInputStream(this.configFile);
				savedEntries.load(fileInputStream);
			} catch (Exception e) {
			}
			if (savedEntries.size() == this.values.size()) {
				try {
					this.load();
				} catch (IOException e) {
					SimpleUtils.LOGGER.error("Cannot Load Config: " + this.configName + "! Error!");
				}
			} else {
				SimpleUtils.LOGGER.info("Reloading: " + this.configName + " New Entries Detected (Double Check)!");
				this.saveConfig();
				if (this.configFile.exists()) {
					this.loadConfig();
				}
			}
		} else {
			SimpleUtils.LOGGER.info("Cannot Load Config: " + this.configName + " Doesn't Exist!");
			this.saveConfig();
			if (this.configFile.exists()) {
				this.loadConfig();
			}
		}
	}

	private void load() throws IOException {
		FileInputStream fileInputStream = new FileInputStream(this.configFile);
		this.properties.load(fileInputStream);
		fileInputStream.close();
		SimpleUtils.LOGGER.info("" + this.properties.toString());

	}

	private void save(String key, ConfigValue value) {
		this.values.put(key.toLowerCase(), value);
	}

	public boolean saveBooleanValue(String key, boolean defaultValue) {
		if (!this.values.containsKey(key)) {
			this.save(key, new BooleanValue(defaultValue));
		} else {
			SimpleUtils.LOGGER.error("Error! Two Entries Have the Same Key!");
		}
		return defaultValue;
	}

	public int saveIntegerValue(String key, int defaultValue) {
		if (!this.values.containsKey(key)) {
			this.save(key, new IntegerValue(defaultValue));
		} else {
			SimpleUtils.LOGGER.error("Error! Two Entries Have the Same Key!");
		}
		return defaultValue;
	}

	public boolean getBooleanValue(String key) {
		return this.getBoolean(this.getValue(key));
	}

	public int getIntegerValue(String key) {
		return this.getInteger(this.getValue(key));
	}

	private String getValue(String key) {
		String realKey = key.toLowerCase();
		if (this.properties.containsKey(realKey)) {
			return this.properties.getProperty(realKey);
		} else {
			SimpleUtils.LOGGER.error(realKey + ": Doesn't Exist In " + this.configName);
			throw new ConfigLoadingException();
		}
	}

	private boolean getBoolean(String value) {
		if (value.equals("true")) {
			return true;
		} else if (value.equals("false")) {
			return false;
		} else {
			SimpleUtils.LOGGER.error(value + ": Is Not Right In " + this.configName);
			throw new ConfigLoadingException();
		}
	}

	private int getInteger(String value) {
		return Integer.parseInt(value);
	}

	public static interface ConfigValue {
		String toBasicString();

		void save(Properties properties, String key);
	}

	public static record BooleanValue(boolean value) implements ConfigValue {
		@Override
		public String toBasicString() {
			return this.value == true ? "true" : "false";
		}

		@Override
		public void save(Properties properties, String key) {
			properties.setProperty(key, this.toBasicString());
		}
	}

	public static record IntegerValue(int value) implements ConfigValue {
		@Override
		public String toBasicString() {
			return this.value + "";
		}

		@Override
		public void save(Properties properties, String key) {
			properties.setProperty(key, this.toBasicString());
		}
	}
}
