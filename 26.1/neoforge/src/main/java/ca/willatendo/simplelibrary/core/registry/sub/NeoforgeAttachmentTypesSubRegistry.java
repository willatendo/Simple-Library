package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.registry.AttachmentTypeBuilder;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class NeoforgeAttachmentTypesSubRegistry extends AttachmentTypesSubRegistry {
    private final SimpleRegistry<AttachmentType<?>> attachmentTypes;

    public NeoforgeAttachmentTypesSubRegistry(String modId) {
        super(modId);
        this.attachmentTypes = new SimpleRegistry<>(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, modId);
    }

    public SimpleRegistry<AttachmentType<?>> getSimpleRegistry() {
        return this.attachmentTypes;
    }

    @Override
    public <T> Identifier register(String id, AttachmentTypeBuilder<T> attachmentTypeBuilder) {
        Identifier identifier = CoreUtils.resource(this.modId, id);
        this.attachmentTypes.register(id, () -> {
            AttachmentType.Builder<T> builder = AttachmentType.builder(attachmentTypeBuilder::getDefaultValue);
            if (attachmentTypeBuilder.doSerialize()) {
                builder.serialize(attachmentTypeBuilder.getCodec().fieldOf(id));
            }
            if (attachmentTypeBuilder.doCopyOnDeath()) {
                builder.copyOnDeath();
            }
            if(attachmentTypeBuilder.doSync()) {
                builder.sync(attachmentTypeBuilder.getStreamCodec());
            }
            return builder.build();
        });
        return identifier;
    }
}
