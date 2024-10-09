package willatendo.simplelibrary.common.config;

import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import willatendo.simplelibrary.platform.ModloaderHelper;
import willatendo.simplelibrary.server.util.SimpleUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

public final class SimpleConfig {
    private static final File CONFIG_DIRECTORY = ModloaderHelper.INSTANCE.getConfigPath().toFile();
    private final Map<String, ConfigValue<?>> defaultValues = Maps.newHashMap();
    private final Map<String, ConfigValue<?>> configValues = Maps.newHashMap();
    private final String name;
    private final File file;

    public SimpleConfig(String name, ConfigType configType) {
        this.name = name;
        this.file = new File(CONFIG_DIRECTORY, name + "-" + configType.getName() + ".toml");
    }

    public String getName() {
        return this.name;
    }

    public void addConfigValue(String identifier, ConfigValue<?> configValue) {
        this.defaultValues.put(identifier, configValue);
        this.configValues.put(identifier, configValue);
    }

    public void addBooleanValue(String identifier, boolean value, String... comments) {
        this.addConfigValue(identifier, new BooleanConfigValue(value, comments));
    }

    public void addIntegerValue(String identifier, int value, String... comments) {
        this.addConfigValue(identifier, new IntegerConfigValue(value, comments));
    }

    public <T> T getValue(String identifier) {
        if (this.configValues.containsKey(identifier)) {
            return (T) this.configValues.get(identifier).getValue();
        } else {
            if (this.defaultValues.containsKey(identifier)) {
                return (T) this.defaultValues.get(identifier).getValue();
            } else {
                SimpleUtils.SIMPLE_LOGGER.error("Cannot find value for key {}!", identifier);
                return null;
            }
        }
    }

    public boolean create() {
        if (!this.file.exists()) {
            try {
                return this.file.createNewFile();
            } catch (IOException e) {
                SimpleUtils.SIMPLE_LOGGER.error("Could not make config: {}!", this.file.getName(), e);
            }
        }
        return false;
    }

    public void write() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.file));
            for (int i = 0; i < this.defaultValues.size(); i++) {
                String defaultKey = this.defaultValues.keySet().stream().toList().get(i);
                ConfigValue<?> configValue = this.configValues.get(defaultKey);
                ConfigWriter configWriter = new ConfigWriter(bufferedWriter);
                configValue.writeComments(configWriter);
                if (this.configValues.containsKey(defaultKey)) {
                    configValue.write(defaultKey, configWriter);
                } else {
                    this.defaultValues.get(defaultKey).write(defaultKey, configWriter);
                }
            }
            bufferedWriter.close();
        } catch (IOException e) {
            SimpleUtils.SIMPLE_LOGGER.error("Could not write to config: {}!", this.file.getName(), e);
        }
    }

    public void read() {
        List<String[]> values = Lists.newArrayList();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.startsWith("#") && line.contains(" = ")) {
                    values.add(line.split(" = "));
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            SimpleUtils.SIMPLE_LOGGER.error("Could not find config: {}!", this.file.getName(), e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        values.forEach(stringArray -> {
            String key = stringArray[0];
            ConfigValue<?> configValue = this.configValues.get(key);
            ConfigValueType configValueType = configValue.getConfigValueType();
            String[] comments = configValue.getComments();
            if (configValueType == ConfigValueType.BOOLEAN) {
                boolean value = (boolean) configValue.parse(stringArray[1]);
                this.configValues.replace(key, new BooleanConfigValue(value, comments));
            }
            if (configValueType == ConfigValueType.INTEGER) {
                int value = (int) configValue.parse(stringArray[1]);
                this.configValues.replace(key, new IntegerConfigValue(value, comments));
            }
        });
    }
}
