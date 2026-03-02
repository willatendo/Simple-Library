package ca.willatendo.simplelibrary.core.registry;

import com.mojang.serialization.Codec;

public final class AttachmentTypeBuilder<T> {
    private final T defaultValue;
    private boolean serialize = false;
    private Codec<T> codec;
    private boolean copyOnDeath = false;

    public static <T> AttachmentTypeBuilder<T> builder(T defaultValue) {
        return new AttachmentTypeBuilder<>(defaultValue);
    }

    private AttachmentTypeBuilder(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public AttachmentTypeBuilder<T> serialize(Codec<T> codec) {
        this.serialize = true;
        this.codec = codec;
        return this;
    }

    public AttachmentTypeBuilder<T> copyOnDeath() {
        this.copyOnDeath = true;
        return this;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public boolean doSerialize() {
        return this.serialize;
    }

    public Codec<T> getCodec() {
        return this.codec;
    }

    public boolean doCopyOnDeath() {
        return this.copyOnDeath;
    }
}
