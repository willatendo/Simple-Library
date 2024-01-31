package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import willatendo.simplelibrary.data.util.DataPackRegistriesHooks;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class DatapackEntriesProvider implements DataProvider {
	private final FabricDataOutput fabricDataOutput;
	private final CompletableFuture<HolderLookup.Provider> registries;
	private final CompletableFuture<HolderLookup.Provider> fullRegistries;
	private final Predicate<String> namespacePredicate;

	public DatapackEntriesProvider(FabricDataOutput fabricDataOutput, CompletableFuture<RegistrySetBuilder.PatchedRegistries> registries, Set<String> modIds) {
		this.namespacePredicate = modIds == null ? namespace -> true : modIds::contains;
		this.fabricDataOutput = fabricDataOutput;
		this.registries = registries.thenApply(RegistrySetBuilder.PatchedRegistries::patches);
		this.fullRegistries = registries.thenApply(RegistrySetBuilder.PatchedRegistries::full);
	}

	public DatapackEntriesProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder, Set<String> modIds) {
		this(fabricDataOutput, RegistryPatchGenerator.createLookup(registries, datapackEntriesBuilder), modIds);
	}

	public DatapackEntriesProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder) {
		this(fabricDataOutput, RegistryPatchGenerator.createLookup(registries, datapackEntriesBuilder), Collections.singleton(fabricDataOutput.getModId()));
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		return this.registries.thenCompose(provider -> {
			RegistryOps<JsonElement> dynamicOps = RegistryOps.create(JsonOps.INSTANCE, provider);
			return CompletableFuture.allOf((CompletableFuture<?>[]) DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().flatMap(registryData -> this.dumpRegistryCap(cachedOutput, (HolderLookup.Provider) provider, (DynamicOps<JsonElement>) dynamicOps, (RegistryDataLoader.RegistryData) registryData).stream()).toArray(CompletableFuture[]::new));
		});
	}

	private <T> Optional<CompletableFuture<?>> dumpRegistryCap(CachedOutput cachedOutput, HolderLookup.Provider provider, DynamicOps<JsonElement> dynamicOps, RegistryDataLoader.RegistryData<T> registryData) {
		ResourceKey<? extends Registry<T>> resourceKey = registryData.key();
		return provider.lookup(resourceKey).map(registryLookup -> {
			PackOutput.PathProvider pathProvider = this.fabricDataOutput.createPathProvider(PackOutput.Target.DATA_PACK, SimpleUtils.prefixNamespace(resourceKey.location()));
			return CompletableFuture.allOf((CompletableFuture[]) registryLookup.listElements().filter(holder -> this.namespacePredicate.test(holder.key().location().getNamespace())).map(reference -> DatapackEntriesProvider.dumpValue(pathProvider.json(reference.key().location()), cachedOutput, dynamicOps, registryData.elementCodec(), reference.value())).toArray(CompletableFuture[]::new));
		});
	}

	private static <E> CompletableFuture<?> dumpValue(Path path, CachedOutput cachedOutput, DynamicOps<JsonElement> dynamicOps, Encoder<E> encoder, E object) {
		Optional<JsonElement> optional = encoder.encodeStart(dynamicOps, object).resultOrPartial(string -> LOGGER.error("Couldn't serialize element {}: {}", (Object) path, string));
		if (optional.isPresent()) {
			return DataProvider.saveStable(cachedOutput, optional.get(), path);
		}
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public String getName() {
		return this.fabricDataOutput.getModId() + ": Built In Registries";
	}

	public CompletableFuture<HolderLookup.Provider> getRegistryProvider() {
		return this.fullRegistries;
	}
}
