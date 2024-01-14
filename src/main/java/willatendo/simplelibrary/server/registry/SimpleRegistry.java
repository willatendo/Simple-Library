/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package willatendo.simplelibrary.server.registry;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class SimpleRegistry<T> {
	private final ResourceKey<? extends Registry<T>> registryKey;
	private final String modId;

	private final Map<SimpleHolder<? extends T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
	private final Set<SimpleHolder<? extends T>> entriesView = Collections.unmodifiableSet(this.entries.keySet());

	public static <T> SimpleRegistry<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
		return new SimpleRegistry<>(registryKey, modId);
	}

	private SimpleRegistry(ResourceKey<? extends Registry<T>> registryKey, String modId) {
		this.registryKey = Objects.requireNonNull(registryKey);
		this.modId = Objects.requireNonNull(modId);
	}

	public SimpleHolder<T> register(String id, Supplier<T> value) {
		return this.register(id, key -> value.get());
	}

	public <I extends T> SimpleHolder<I> register(String id, Function<ResourceLocation, ? extends T> func) {
		Objects.requireNonNull(id);
		Objects.requireNonNull(func);

		ResourceLocation valueId = new ResourceLocation(this.modId, id);
		SimpleHolder<I> simpleHolder = this.createHolder(this.registryKey, valueId);

		if (this.entries.putIfAbsent(simpleHolder, () -> func.apply(valueId)) != null) {
			throw new IllegalArgumentException("Duplicate registration " + id);
		}

		return simpleHolder;
	}

	protected <I extends T> SimpleHolder<I> createHolder(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation valueId) {
		return (SimpleHolder<I>) SimpleHolder.create(registryKey, valueId);
	}

	public Set<SimpleHolder<? extends T>> getEntries() {
		return this.entriesView;
	}

	public void register() {
		for (Entry<SimpleHolder<? extends T>, Supplier<? extends T>> entry : this.entries.entrySet()) {
			Registry.register((Registry) BuiltInRegistries.REGISTRY.get(this.registryKey.location()), entry.getKey().getId(), entry.getValue().get());
			entry.getKey().bind(false);
		}
	}
}
