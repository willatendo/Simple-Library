package ca.willatendo.simplelibrary.core.registry;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public final class AttachmentTypeBuilder<T> {
    private final T defaultValue;
    private Codec<T> codec;
    private boolean copyOnDeath = false;
    private StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    public static <T> AttachmentTypeBuilder<T> builder(T defaultValue) {
        return new AttachmentTypeBuilder<>(defaultValue);
    }

    private AttachmentTypeBuilder(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public AttachmentTypeBuilder<T> serialize(Codec<T> codec) {
        this.codec = codec;
        return this;
    }

    public AttachmentTypeBuilder<T> copyOnDeath() {
        this.copyOnDeath = true;
        return this;
    }

    public AttachmentTypeBuilder<T> sync(StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        this.streamCodec = streamCodec;
        return this;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public boolean doSerialize() {
        return this.codec != null;
    }

    public Codec<T> getCodec() {
        return this.codec;
    }

    public boolean doCopyOnDeath() {
        return this.copyOnDeath;
    }

    public boolean doSync() {
        return this.streamCodec != null;
    }

    public StreamCodec<RegistryFriendlyByteBuf, T> getStreamCodec() {
        return this.streamCodec;
    }
}
