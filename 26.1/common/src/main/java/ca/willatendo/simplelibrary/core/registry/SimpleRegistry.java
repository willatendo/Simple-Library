package ca.willatendo.simplelibrary.core.registry;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleRegistry<T> {
    private final Map<SimpleHolder<? extends T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
    private final Set<SimpleHolder<? extends T>> entriesView = Collections.unmodifiableSet(this.entries.keySet());

    protected final ResourceKey<? extends Registry<T>> registryKey;
    protected final String modId;

    public SimpleRegistry(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        this.registryKey = Objects.requireNonNull(registryKey);
        this.modId = Objects.requireNonNull(modId);
    }

    public <I extends T> SimpleHolder<I> register(String id, Supplier<I> value) {
        return this.register(id, key -> value.get());
    }

    public <I extends T> SimpleHolder<I> register(String id, Function<Identifier, I> function) {
        Identifier valueIdentifier = CoreUtils.resource(this.modId, id);
        SimpleHolder<I> simpleHolder = this.createHolder(this.registryKey, valueIdentifier);

        if (this.entries.putIfAbsent(simpleHolder, () -> function.apply(valueIdentifier)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + id);
        }

        return simpleHolder;
    }

    public void addEntries(RegisterFunction registerFunction) {
        for (Map.Entry<SimpleHolder<? extends T>, Supplier<? extends T>> entry : entries.entrySet()) {
            registerFunction.register(this.registryKey, entry.getKey().getIdentifier(), () -> entry.getValue().get());
            entry.getKey().bind(false);
        }
    }

    protected <I extends T> SimpleHolder<I> createHolder(ResourceKey<? extends Registry<T>> registryKey, Identifier valueIdentifier) {
        return (SimpleHolder<I>) SimpleHolder.create(registryKey, valueIdentifier);
    }

    public Map<SimpleHolder<? extends T>, Supplier<? extends T>> getEntries() {
        return this.entries;
    }

    public Set<SimpleHolder<? extends T>> getEntriesView() {
        return this.entriesView;
    }

    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registryKey;
    }
}
