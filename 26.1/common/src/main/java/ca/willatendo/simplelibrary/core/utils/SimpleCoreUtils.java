package ca.willatendo.simplelibrary.core.utils;

import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SimpleCoreUtils {
    public static final String ID = "simplelibrary";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    private SimpleCoreUtils() {
    }

    public static Identifier resource(String path) {
        return CoreUtils.resource(ID, path);
    }

}
