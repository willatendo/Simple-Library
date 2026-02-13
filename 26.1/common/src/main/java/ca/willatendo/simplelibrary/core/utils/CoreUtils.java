package ca.willatendo.simplelibrary.core.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public final class CoreUtils {
    private CoreUtils() {
    }

    public static Identifier resource(String modId, String path) {
        return Identifier.fromNamespaceAndPath(modId, path);
    }

    public static Identifier minecraft(String path) {
        return Identifier.withDefaultNamespace(path);
    }

    public static Component translation(String type, String modId, String key) {
        return Component.translatable(type + "." + modId + "." + key);
    }

    public static String simpleAutoName(String internalName) {
        return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }
}
