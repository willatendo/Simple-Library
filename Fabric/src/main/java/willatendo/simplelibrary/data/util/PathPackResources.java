package willatendo.simplelibrary.data.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.FileUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;

public class PathPackResources extends AbstractPackResources {
	private static final Logger LOGGER = LogUtils.getLogger();
	private final Path path;

	public PathPackResources(String packId, boolean isBuiltin, Path path) {
		super(packId, isBuiltin);
		this.path = path;
	}

	public Path getSource() {
		return this.path;
	}

	protected Path resolve(String... paths) {
		Path path = getSource();
		for (String name : paths) {
			path = path.resolve(name);
		}
		return path;
	}

	@Nullable
	@Override
	public IoSupplier<InputStream> getRootResource(String... paths) {
		Path path = resolve(paths);
		if (!Files.exists(path)) {
			return null;
		}

		return IoSupplier.create(path);
	}

	@Override
	public void listResources(PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
		FileUtil.decomposePath(path).get().ifLeft(parts -> net.minecraft.server.packs.PathPackResources.listPath(namespace, resolve(packType.getDirectory(), namespace).toAbsolutePath(), parts, resourceOutput)).ifRight(dataResult -> LOGGER.error("Invalid path {}: {}", path, dataResult.message()));
	}

	@Override
	public Set<String> getNamespaces(PackType packType) {
		return this.getNamespacesFromDisk(packType);
	}

	@NotNull
	private Set<String> getNamespacesFromDisk(PackType packType) {
		try {
			Path root = resolve(packType.getDirectory());
			try (Stream<Path> walker = Files.walk(root, 1)) {
				return walker.filter(Files::isDirectory).map(root::relativize).filter(p -> p.getNameCount() > 0).map(p -> p.toString().replaceAll("/$", "")).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
			}
		} catch (IOException e) {
			if (packType == PackType.SERVER_DATA) {
				return this.getNamespaces(PackType.CLIENT_RESOURCES);
			} else {
				return Collections.emptySet();
			}
		}
	}

	@Override
	public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) {
		return this.getRootResource(getPathFromLocation(resourceLocation.getPath().startsWith("lang/") ? PackType.CLIENT_RESOURCES : packType, resourceLocation));
	}

	private static String[] getPathFromLocation(PackType packType, ResourceLocation resourceLocation) {
		String[] parts = resourceLocation.getPath().split("/");
		String[] result = new String[parts.length + 2];
		result[0] = packType.getDirectory();
		result[1] = resourceLocation.getNamespace();
		System.arraycopy(parts, 0, result, 2, parts.length);
		return result;
	}

	@Override
	public void close() {
	}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "%s: %s (%s)", getClass().getName(), this.packId(), getSource());
	}
}
