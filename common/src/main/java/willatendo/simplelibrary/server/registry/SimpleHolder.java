package willatendo.simplelibrary.server.registry;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SimpleHolder<T> implements Holder<T>, Supplier<T> {
    public static <T> SimpleHolder<T> create(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation valueId) {
        return SimpleHolder.create(ResourceKey.create(registryKey, valueId));
    }

    public static <T> SimpleHolder<T> create(ResourceLocation registryId, ResourceLocation valueId) {
        return SimpleHolder.create(ResourceKey.createRegistryKey(registryId), valueId);
    }

    public static <T> SimpleHolder<T> create(ResourceKey<T> key) {
        return new SimpleHolder<>(key);
    }

    private final ResourceKey<T> holderKey;
    private Holder<T> holder = null;

    private SimpleHolder(ResourceKey<T> key) {
        this.holderKey = key;
    }

    protected Registry<T> getRegistry() {
        return (Registry<T>) BuiltInRegistries.REGISTRY.getValue(this.holderKey.registry());
    }

    protected final void bind(boolean throwOnMissingRegistry) {
        if (this.holder != null) {
            return;
        }

        Registry<T> registry = this.getRegistry();
        if (registry != null) {
            this.holder = registry.get(this.holderKey).orElse(null);
        } else if (throwOnMissingRegistry) {
            throw new IllegalStateException("Registry not present for " + this + ": " + this.holderKey.registry());
        }
    }

    public ResourceLocation getId() {
        return this.holderKey.location();
    }

    public ResourceKey<T> getKey() {
        return this.holderKey;
    }

    @Override
    public T get() {
        return this.value();
    }

    @Override
    public T value() {
        this.bind(true);
        if (this.holder == null) {
            throw new NullPointerException("Trying to access unbound value: " + this.holderKey);
        }

        return (T) this.holder.value();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof SimpleHolder<?> simpleHolder && simpleHolder.holderKey == this.holderKey;
    }

    @Override
    public int hashCode() {
        return this.holderKey.hashCode();
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "SimpleHolder{%s}", this.holderKey);
    }

    @Override
    public boolean isBound() {
        this.bind(false);
        return this.holder != null && this.holder.isBound();
    }

    @Override
    public boolean is(ResourceLocation resourceLocation) {
        return resourceLocation.equals(this.holderKey.location());
    }

    @Override
    public boolean is(ResourceKey resourceKey) {
        return resourceKey == this.holderKey;
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> predicate) {
        return predicate.test(this.holderKey);
    }

    @Override
    public boolean is(Holder<T> holder) {
        this.bind(false);
        return this.holder != null && this.holder.is(holder);
    }

    @Override
    public boolean is(TagKey tagKey) {
        this.bind(false);
        return this.holder != null && this.holder.is(tagKey);
    }

    @Override
    public Stream tags() {
        this.bind(false);
        return this.holder != null ? this.holder.tags() : Stream.empty();
    }

    @Override
    public Either unwrap() {
        return Either.left(this.holderKey);
    }

    @Override
    public Optional unwrapKey() {
        return Optional.of(this.holderKey);
    }

    @Override
    public Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner holderOwner) {
        this.bind(false);
        return this.holder != null && this.holder.canSerializeIn(holderOwner);
    }
}
