package willatendo.simplelibrary.common.config;

import java.io.BufferedReader;
import java.util.Map;

public class ConfigReader {
    private final BufferedReader bufferedReader;
    private final Map<String, ConfigValue<?>> map;

    public ConfigReader(BufferedReader bufferedReader, Map<String, ConfigValue<?>> map) {
        this.bufferedReader = bufferedReader;
        this.map = map;
    }

    public void read(String identifier, ConfigValue<?> configValue) {
        this.map.replace(identifier, configValue);
    }
}
