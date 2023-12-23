package willatendo.simplelibrary.data.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.jetbrains.annotations.VisibleForTesting;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.client.resources.IndexedAssetSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

@Deprecated
public class ExistingFileHelper {
	private final MultiPackResourceManager clientResources;
	private final MultiPackResourceManager serverData;
	private final boolean enable;
	private final Multimap<PackType, ResourceLocation> generated = HashMultimap.create();

	public ExistingFileHelper(Collection<Path> existingPacks, Set<String> existingMods, boolean enable, String assetIndex, File assetsDir) {
		List<PackResources> candidateClientResources = new ArrayList<>();
		List<PackResources> candidateServerResources = new ArrayList<>();

		if (assetIndex != null && assetsDir != null && assetsDir.exists()) {
			candidateClientResources.add(ClientPackSource.createVanillaPackSource(IndexedAssetSource.createIndexFs(assetsDir.toPath(), assetIndex)));
		}
		candidateServerResources.add(ServerPacksSource.createVanillaPackSource());
//		for (Path existingPaths : existingPacks) {
//			File file = existingPaths.toFile();
//			PackResources packResources = file.isDirectory() ? new PathPackResources(file.getName(), file.toPath(), false) : new FilePackResources(file.getName(), file, false);
//			candidateClientResources.add(packResources);
//			candidateServerResources.add(packResources);
//		}
		for (String existingModId : existingMods) {
			Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(existingModId);
			if (modContainer.isPresent()) {
				PackResources packResources = ResourcePackLoader.createPackForMod(modContainer.get());
				candidateClientResources.add(packResources);
				candidateServerResources.add(packResources);
			}
		}

		this.clientResources = new MultiPackResourceManager(PackType.CLIENT_RESOURCES, candidateClientResources);
		this.serverData = new MultiPackResourceManager(PackType.SERVER_DATA, candidateServerResources);

		this.enable = enable;
	}

	private ResourceManager getManager(PackType packType) {
		return packType == PackType.CLIENT_RESOURCES ? this.clientResources : this.serverData;
	}

	private ResourceLocation getLocation(ResourceLocation base, String suffix, String prefix) {
		return new ResourceLocation(base.getNamespace(), prefix + "/" + base.getPath() + suffix);
	}

	public boolean exists(ResourceLocation resourceLocation, PackType packType) {
		if (!this.enable) {
			return true;
		}
		return this.generated.get(packType).contains(resourceLocation) || this.getManager(packType).getResource(resourceLocation).isPresent();
	}

	public boolean exists(ResourceLocation resourceLocation, IResourceType resourceType) {
		return this.exists(this.getLocation(resourceLocation, resourceType.getSuffix(), resourceType.getPrefix()), resourceType.getPackType());
	}

	public boolean exists(ResourceLocation resourceLocation, PackType packType, String pathSuffix, String pathPrefix) {
		return this.exists(this.getLocation(resourceLocation, pathSuffix, pathPrefix), packType);
	}

	public void trackGenerated(ResourceLocation resourceLocation, IResourceType resourceType) {
		this.generated.put(resourceType.getPackType(), getLocation(resourceLocation, resourceType.getSuffix(), resourceType.getPrefix()));
	}

	public void trackGenerated(ResourceLocation resourceLocation, PackType packType, String pathSuffix, String pathPrefix) {
		this.generated.put(packType, getLocation(resourceLocation, pathSuffix, pathPrefix));
	}

	@VisibleForTesting
	public Resource getResource(ResourceLocation resourceLocation, PackType packType, String pathSuffix, String pathPrefix) throws FileNotFoundException {
		return this.getResource(getLocation(resourceLocation, pathSuffix, pathPrefix), packType);
	}

	@VisibleForTesting
	public Resource getResource(ResourceLocation resourceLocation, PackType packType) throws FileNotFoundException {
		return this.getManager(packType).getResourceOrThrow(resourceLocation);
	}

	@VisibleForTesting
	public List<Resource> getResourceStack(ResourceLocation resourceLocation, PackType packType) {
		return this.getManager(packType).getResourceStack(resourceLocation);
	}

	public boolean isEnabled() {
		return this.enable;
	}

	public interface IResourceType {
		PackType getPackType();

		String getSuffix();

		String getPrefix();
	}

	public static class ResourceType implements IResourceType {
		private final PackType packType;
		private final String suffix, prefix;

		public ResourceType(PackType packType, String suffix, String prefix) {
			this.packType = packType;
			this.suffix = suffix;
			this.prefix = prefix;
		}

		@Override
		public PackType getPackType() {
			return packType;
		}

		@Override
		public String getSuffix() {
			return suffix;
		}

		@Override
		public String getPrefix() {
			return prefix;
		}
	}
}
