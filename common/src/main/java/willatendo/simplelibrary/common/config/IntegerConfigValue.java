package willatendo.simplelibrary.common.config;

import java.io.IOException;

public record IntegerConfigValue(int value, String[] comments) implements ConfigValue<Integer> {
    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String[] getComments() {
        return this.comments;
    }

    @Override
    public Integer parse(String in) {
        return Integer.parseInt(in);
    }

    @Override
    public ConfigValueType getConfigValueType() {
        return ConfigValueType.INTEGER;
    }

    @Override
    public void write(String identifier, ConfigWriter configWriter) throws IOException {
        configWriter.write(identifier + " = " + this.value());
    }
}
