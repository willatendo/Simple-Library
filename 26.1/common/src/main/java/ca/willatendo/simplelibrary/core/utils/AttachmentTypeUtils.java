package ca.willatendo.simplelibrary.core.utils;

import ca.willatendo.simplelibrary.platform.SimpleLibraryPlatformHelper;
import net.minecraft.resources.Identifier;

public final class AttachmentTypeUtils {
    private AttachmentTypeUtils() {
    }

    public static <T> boolean hasData(T value, Identifier attachmentType) {
        return SimpleLibraryPlatformHelper.INSTANCE.hasData(value, attachmentType);
    }

    public static <T, V> V getData(T value, Identifier attachmentType) {
        return SimpleLibraryPlatformHelper.INSTANCE.getData(value, attachmentType);
    }

    public static <T, V> void setData(T value, Identifier attachmentType, V data) {
        SimpleLibraryPlatformHelper.INSTANCE.setData(value, attachmentType, data);
    }

    public static <T> void removeData(T value, Identifier attachmentType) {
        SimpleLibraryPlatformHelper.INSTANCE.removeData(value, attachmentType);
    }
}
