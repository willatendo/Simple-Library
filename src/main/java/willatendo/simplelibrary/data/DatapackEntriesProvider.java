package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.RegistrySetBuilder.PatchedRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

public class DatapackEntriesProvider implements DataProvider {
	private final FabricDataOutput fabricDataOutput;
	private final CompletableFuture<HolderLookup.Provider> registries;

	public DatapackEntriesProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder registrySetBuilder) {
		this.fabricDataOutput = fabricDataOutput;
		this.registries = (CompletableFuture) RegistryPatchGenerator.createLookup(registries, registrySetBuilder).thenAccept(PatchedRegistries::full);
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		return this.registries.thenCompose(provider -> {
			RegistryOps<JsonElement> dynamicOps = RegistryOps.create(JsonOps.INSTANCE, provider);
			return CompletableFuture.allOf((CompletableFuture[]) RegistryDataLoader.WORLDGEN_REGISTRIES.stream().flatMap(registryData -> this.dumpRegistryCap(cachedOutput, (HolderLookup.Provider) provider, (DynamicOps<JsonElement>) dynamicOps, (RegistryDataLoader.RegistryData) registryData).stream()).toArray(CompletableFuture[]::new));
		});
	}

	private <T> Optional<CompletableFuture<?>> dumpRegistryCap(CachedOutput cachedOutput, HolderLookup.Provider provider, DynamicOps<JsonElement> dynamicOps, RegistryDataLoader.RegistryData<T> registryData) {
		ResourceKey<? extends Registry<T>> resourceKey = registryData.key();
		return provider.lookup(resourceKey).map(registryLookup -> {
			PackOutput.PathProvider pathProvider = this.fabricDataOutput.createPathProvider(PackOutput.Target.DATA_PACK, resourceKey.location().getPath());
			return CompletableFuture.allOf((CompletableFuture[]) registryLookup.listElements().map(reference -> DatapackEntriesProvider.dumpValue(pathProvider.json(reference.key().location()), cachedOutput, dynamicOps, registryData.elementCodec(), reference.value())).toArray(CompletableFuture[]::new));
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
		return this.fabricDataOutput.getModId() + ": BuiltInRegistries";
	}
}
