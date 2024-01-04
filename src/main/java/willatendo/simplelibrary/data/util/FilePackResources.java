package willatendo.simplelibrary.data.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;

public class FilePackResources extends AbstractPackResources {
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final Splitter SPLITTER = Splitter.on('/').omitEmptyStrings().limit(3);
	private final File file;
	@Nullable
	private ZipFile zipFile;
	private boolean failedToLoad;

	public FilePackResources(String packId, File file, boolean isBuiltin) {
		super(packId, isBuiltin);
		this.file = file;
	}

	@Nullable
	private ZipFile getOrCreateZipFile() {
		if (this.failedToLoad) {
			return null;
		} else {
			if (this.zipFile == null) {
				try {
					this.zipFile = new ZipFile(this.file);
				} catch (IOException ioexception) {
					LOGGER.error("Failed to open pack {}", this.file, ioexception);
					this.failedToLoad = true;
					return null;
				}
			}

			return this.zipFile;
		}
	}

	private static String getPathFromLocation(PackType packType, ResourceLocation resourceLocation) {
		return String.format(Locale.ROOT, "%s/%s/%s", packType.getDirectory(), resourceLocation.getNamespace(), resourceLocation.getPath());
	}

	@Nullable
	public IoSupplier<InputStream> getRootResource(String... paths) {
		return this.getResource(String.join("/", paths));
	}

	public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) {
		return this.getResource(getPathFromLocation(packType, resourceLocation));
	}

	@Nullable
	private IoSupplier<InputStream> getResource(String path) {
		ZipFile zipFile = this.getOrCreateZipFile();
		if (zipFile == null) {
			return null;
		} else {
			ZipEntry zipEntry = zipFile.getEntry(path);
			return zipEntry == null ? null : IoSupplier.create(zipFile, zipEntry);
		}
	}

	public Set<String> getNamespaces(PackType packType) {
		ZipFile zipFile = this.getOrCreateZipFile();
		if (zipFile == null) {
			return Set.of();
		} else {
			Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
			Set<String> set = Sets.newHashSet();

			while (enumeration.hasMoreElements()) {
				ZipEntry zipentry = enumeration.nextElement();
				String s = zipentry.getName();
				if (s.startsWith(packType.getDirectory() + "/")) {
					List<String> list = Lists.newArrayList(SPLITTER.split(s));
					if (list.size() > 1) {
						String s1 = list.get(1);
						if (s1.equals(s1.toLowerCase(Locale.ROOT))) {
							set.add(s1);
						} else {
							LOGGER.warn("Ignored non-lowercase namespace: {} in {}", s1, this.file);
						}
					}
				}
			}

			return set;
		}
	}

	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}

	public void close() {
		if (this.zipFile != null) {
			IOUtils.closeQuietly((Closeable) this.zipFile);
			this.zipFile = null;
		}

	}

	public void listResources(PackType packType, String prefix, String sufix, PackResources.ResourceOutput resourceOutput) {
		ZipFile zipfile = this.getOrCreateZipFile();
		if (zipfile != null) {
			Enumeration<? extends ZipEntry> enumeration = zipfile.entries();
			String s = packType.getDirectory() + "/" + prefix + "/";
			String s1 = s + sufix + "/";

			while (enumeration.hasMoreElements()) {
				ZipEntry zipentry = enumeration.nextElement();
				if (!zipentry.isDirectory()) {
					String s2 = zipentry.getName();
					if (s2.startsWith(s1)) {
						String s3 = s2.substring(s.length());
						ResourceLocation resourcelocation = ResourceLocation.tryBuild(prefix, s3);
						if (resourcelocation != null) {
							resourceOutput.accept(resourcelocation, IoSupplier.create(zipfile, zipentry));
						} else {
							LOGGER.warn("Invalid path in datapack: {}:{}, ignoring", prefix, s3);
						}
					}
				}
			}

		}
	}
}
