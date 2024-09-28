package willatendo.simplelibrary.common.config;

import java.io.IOException;

public record BooleanConfigValue(boolean value, String[] comments) implements ConfigValue<Boolean> {
    @Override
    public Boolean getValue() {
        return this.value();
    }

    @Override
    public String[] getComments() {
        return this.comments();
    }

    @Override
    public Boolean parse(String in) {
        return Boolean.parseBoolean(in);
    }

    @Override
    public ConfigValueType getConfigValueType() {
        return ConfigValueType.BOOLEAN;
    }

    @Override
    public void write(String identifier, ConfigWriter configWriter) throws IOException {
        configWriter.write(identifier + " = " + this.value());
    }
}
