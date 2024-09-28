package willatendo.simplelibrary.common.config;

import java.io.IOException;

public interface ConfigValue<T> {
    T getValue();

    String[] getComments();

    T parse(String in);

    ConfigValueType getConfigValueType();

    void write(String identifier, ConfigWriter configWriter) throws IOException;

    default void writeComments(ConfigWriter configWriter) throws IOException {
        for (String comment : this.getComments()) {
            configWriter.write("# " + comment);
        }
    }
}
