package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.registry.AttachmentTypeBuilder;
import ca.willatendo.simplelibrary.platform.SimpleLibraryPlatformHelper;
import net.minecraft.resources.Identifier;

public abstract class AttachmentTypesSubRegistry {
    protected final String modId;

    public static AttachmentTypesSubRegistry create(String modId) {
        return SimpleLibraryPlatformHelper.INSTANCE.createAttachmentTypesSubRegistry(modId);
    }

    protected AttachmentTypesSubRegistry(String modId) {
        this.modId = modId;
    }

    public abstract <T> Identifier register(String id, AttachmentTypeBuilder<T> attachmentTypeBuilder);
}
