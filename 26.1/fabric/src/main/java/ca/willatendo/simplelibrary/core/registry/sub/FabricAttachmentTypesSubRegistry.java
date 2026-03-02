package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.registry.AttachmentTypeBuilder;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.minecraft.resources.Identifier;

public final class FabricAttachmentTypesSubRegistry extends AttachmentTypesSubRegistry {
    public FabricAttachmentTypesSubRegistry(String modId) {
        super(modId);
    }

    @Override
    public <T> Identifier register(String id, AttachmentTypeBuilder<T> attachmentTypeBuilder) {
        Identifier identifier = CoreUtils.resource(this.modId, id);
        AttachmentRegistry.<T>create(identifier, builder -> {
            builder.initializer(attachmentTypeBuilder::getDefaultValue);
            if (attachmentTypeBuilder.doSerialize()) {
                builder.persistent(attachmentTypeBuilder.getCodec());
            }
            if (attachmentTypeBuilder.doCopyOnDeath()) {
                builder.copyOnDeath();
            }
            if (attachmentTypeBuilder.doSync()) {
                builder.syncWith(attachmentTypeBuilder.getStreamCodec(), AttachmentSyncPredicate.all());
            }
        });
        return identifier;
    }
}
