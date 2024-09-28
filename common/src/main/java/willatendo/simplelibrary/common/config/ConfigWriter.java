package willatendo.simplelibrary.common.config;

import java.io.BufferedWriter;
import java.io.IOException;

public final class ConfigWriter {
    private final BufferedWriter bufferedWriter;

    public ConfigWriter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }

    public void write(String line) throws IOException {
        this.bufferedWriter.write(line);
        this.bufferedWriter.newLine();
    }
}
