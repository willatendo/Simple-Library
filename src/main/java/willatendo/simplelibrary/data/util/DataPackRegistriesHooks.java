/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package willatendo.simplelibrary.data.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;

public class DataPackRegistriesHooks {
	private DataPackRegistriesHooks() {
	}

//	private static final Map<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> NETWORKABLE_REGISTRIES = new LinkedHashMap<>();
	private static final List<RegistryDataLoader.RegistryData<?>> DATA_PACK_REGISTRIES = new ArrayList<>(RegistryDataLoader.WORLDGEN_REGISTRIES);
	private static final List<RegistryDataLoader.RegistryData<?>> DATA_PACK_REGISTRIES_VIEW = Collections.unmodifiableList(DATA_PACK_REGISTRIES);
	private static final Set<ResourceKey<? extends Registry<?>>> SYNCED_CUSTOM_REGISTRIES = new HashSet<>();
	private static final Set<ResourceKey<? extends Registry<?>>> SYNCED_CUSTOM_REGISTRIES_VIEW = Collections.unmodifiableSet(SYNCED_CUSTOM_REGISTRIES);

//	/* Internal forge hook for retaining mutable access to RegistryAccess's codec registry when it bootstraps. */
//	public static Map<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> grabNetworkableRegistries(ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> builder) {
//		if (!StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().equals(RegistrySynchronization.class))
//			throw new IllegalCallerException("Attempted to call DataPackRegistriesHooks#grabNetworkableRegistries!");
//		NETWORKABLE_REGISTRIES.forEach(builder::put);
//		NETWORKABLE_REGISTRIES.clear();
//		NETWORKABLE_REGISTRIES.putAll(builder.build());
//		return Collections.unmodifiableMap(NETWORKABLE_REGISTRIES);
//	}

	public static List<RegistryDataLoader.RegistryData<?>> getDataPackRegistries() {
		return DATA_PACK_REGISTRIES_VIEW;
	}

	public static Stream<RegistryDataLoader.RegistryData<?>> getDataPackRegistriesWithDimensions() {
		return Stream.concat(DATA_PACK_REGISTRIES_VIEW.stream(), RegistryDataLoader.DIMENSION_REGISTRIES.stream());
	}

	public static Set<ResourceKey<? extends Registry<?>>> getSyncedCustomRegistries() {
		return SYNCED_CUSTOM_REGISTRIES_VIEW;
	}
}
