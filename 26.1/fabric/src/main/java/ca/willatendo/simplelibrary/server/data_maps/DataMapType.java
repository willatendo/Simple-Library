package ca.willatendo.simplelibrary.server.data_maps;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Objects;

// Modified from Neoforge
public sealed class DataMapType<R, T> permits AdvancedDataMapType {
    private final ResourceKey<Registry<R>> registryKey;
    private final Identifier id;
    private final Codec<T> codec;
    private final Codec<T> networkCodec;
    private final boolean mandatorySync;

    DataMapType(ResourceKey<Registry<R>> registryKey, Identifier id, Codec<T> codec, Codec<T> networkCodec, boolean mandatorySync) {
        Preconditions.checkArgument(networkCodec != null || !mandatorySync, "Mandatory sync cannot be enabled when the attachment isn't synchronized");

        this.registryKey = Objects.requireNonNull(registryKey, "registryKey must not be null");
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.codec = Objects.requireNonNull(codec, "codec must not be null");
        this.networkCodec = networkCodec;
        this.mandatorySync = mandatorySync;
    }

    public static <T, R> Builder<T, R> builder(Identifier id, ResourceKey<Registry<R>> registry, Codec<T> codec) {
        return new Builder<>(registry, id, codec);
    }

    public ResourceKey<Registry<R>> registryKey() {
        return registryKey;
    }

    public Identifier id() {
        return id;
    }

    public Codec<T> codec() {
        return codec;
    }

    public Codec<T> networkCodec() {
        return networkCodec;
    }

    public boolean mandatorySync() {
        return mandatorySync;
    }

    public static sealed class Builder<T, R> permits AdvancedDataMapType.Builder {
        protected final ResourceKey<Registry<R>> registryKey;
        protected final Identifier id;
        protected final Codec<T> codec;

        protected Codec<T> networkCodec;
        protected boolean mandatorySync;

        Builder(ResourceKey<Registry<R>> registryKey, Identifier id, Codec<T> codec) {
            this.registryKey = registryKey;
            this.id = id;
            this.codec = codec;
        }

        public Builder<T, R> synced(Codec<T> networkCodec, boolean mandatory) {
            this.mandatorySync = mandatory;
            this.networkCodec = networkCodec;
            return this;
        }

        public DataMapType<R, T> build() {
            return new DataMapType<>(this.registryKey, this.id, this.codec, this.networkCodec, this.mandatorySync);
        }
    }
}
