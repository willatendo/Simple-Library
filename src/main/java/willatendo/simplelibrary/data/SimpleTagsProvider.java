package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;
import willatendo.simplelibrary.data.tag.TagBuilder;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimpleTagsProvider<T> implements DataProvider {
	private static final Logger LOGGER = LogUtils.getLogger();
	protected final PackOutput.PathProvider pathProvider;
	private final CompletableFuture<HolderLookup.Provider> lookupProvider;
	private final CompletableFuture<Void> contentsDone = new CompletableFuture();
	private final CompletableFuture<TagsProvider.TagLookup<T>> parentProvider;
	protected final ResourceKey<? extends Registry<T>> registryKey;
	private final Map<ResourceLocation, TagBuilder> builders = Maps.newLinkedHashMap();
	protected final String modId;
	protected final ExistingFileHelper existingFileHelper;
	private final ExistingFileHelper.IResourceType resourceType;
	private final ExistingFileHelper.IResourceType elementResourceType;

	public SimpleTagsProvider(FabricDataOutput fabricDataOutput, ResourceKey<? extends Registry<T>> registry, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		this(fabricDataOutput, registry, provider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), modId, existingFileHelper);
	}

	public SimpleTagsProvider(FabricDataOutput fabricDataOutput, ResourceKey<? extends Registry<T>> registry, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<T>> tagLookup, String modId, ExistingFileHelper existingFileHelper) {
		this.pathProvider = fabricDataOutput.createPathProvider(PackOutput.Target.DATA_PACK, TagManager.getTagDir(registry));
		this.registryKey = registry;
		this.parentProvider = tagLookup;
		this.lookupProvider = provider;
		this.modId = modId;
		this.existingFileHelper = existingFileHelper;
		this.resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", TagManager.getTagDir(registry));
		this.elementResourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", prefixNamespace(registry.location()));
	}

	private static String prefixNamespace(ResourceLocation resourceLocation) {
		return resourceLocation.getNamespace().equals("minecraft") ? resourceLocation.getPath() : resourceLocation.getNamespace() + "/" + resourceLocation.getPath();
	}

	protected Path getPath(ResourceLocation resourceLocation) {
		return this.pathProvider.json(resourceLocation);
	}

	@Override
	public String getName() {
		return "Tags for " + this.registryKey.location() + ": " + this.modId;
	}

	protected abstract void addTags(HolderLookup.Provider provider);

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		record CombinedData<T> (HolderLookup.Provider provider, TagsProvider.TagLookup<T> tagLookup) {
		}
		return this.createContentsProvider().thenApply((provider) -> {
			this.contentsDone.complete((Void) null);
			return provider;
		}).thenCombineAsync(this.parentProvider, (provider, tagLookup) -> {
			return new CombinedData<>(provider, tagLookup);
		}).thenCompose((combinedData) -> {
			HolderLookup.RegistryLookup<T> registrylookup = combinedData.provider.lookup(this.registryKey).orElseThrow(() -> {
				return new IllegalStateException("Registry " + this.registryKey.location() + " not found");
			});
			Predicate<ResourceLocation> predicate = (resourceLocation) -> {
				return registrylookup.get(ResourceKey.create(this.registryKey, resourceLocation)).isPresent();
			};
			Predicate<ResourceLocation> predicate1 = (resourceLocation) -> {
				return this.builders.containsKey(resourceLocation) || combinedData.tagLookup.contains(TagKey.create(this.registryKey, resourceLocation));
			};
			return CompletableFuture.allOf(this.builders.entrySet().stream().map((entry) -> {
				ResourceLocation resourceLocation = entry.getKey();
				TagBuilder tagbuilder = entry.getValue();
				List<TagEntry> list = tagbuilder.build();
				List<TagEntry> list1 = list.stream().filter((tagEntry) -> {
					return !tagEntry.verifyIfPresent(predicate, predicate1);
				}).filter(this::missing).toList();
				if (!list1.isEmpty()) {
					throw new IllegalArgumentException(String.format(Locale.ROOT, "Couldn't define tag %s as it is missing following references: %s", resourceLocation, list1.stream().map(Objects::toString).collect(Collectors.joining(","))));
				} else {
					JsonElement jsonelement = TagFile.CODEC.encodeStart(JsonOps.INSTANCE, new TagFile(list, tagbuilder.isReplace())).getOrThrow(false, LOGGER::error);
					Path path = this.getPath(resourceLocation);
					if (path == null)
						return CompletableFuture.completedFuture(null); // Forge: Allow running this data provider without writing it. Recipe provider needs valid tags.
					return DataProvider.saveStable(cachedOutput, jsonelement, path);
				}
			}).toArray((p_253442_) -> {
				return new CompletableFuture[p_253442_];
			}));
		});
	}

	private boolean missing(TagEntry tagEntry) {
		if (tagEntry.required) {
			return existingFileHelper == null || !existingFileHelper.exists(tagEntry.id, tagEntry.tag ? this.resourceType : this.elementResourceType);
		}
		return false;
	}

	protected TagAppender<T> tag(TagKey<T> tagKey) {
		TagBuilder tagBuilder = this.getOrCreateRawBuilder(tagKey);
		return new TagAppender(tagBuilder);
	}

	protected TagBuilder getOrCreateRawBuilder(TagKey<T> tagKey) {
		return this.builders.computeIfAbsent(tagKey.location(), resourceLocation -> TagBuilder.create());
	}

	public CompletableFuture<TagLookup<T>> contentsGetter() {
		return this.contentsDone.thenApply(void_ -> tagKey -> Optional.ofNullable(this.builders.get(tagKey.location())));
	}

	protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
		return this.lookupProvider.thenApply(provider -> {
			this.builders.clear();
			this.addTags((HolderLookup.Provider) provider);
			return provider;
		});
	}

	@FunctionalInterface
	public static interface TagLookup<T> extends Function<TagKey<T>, Optional<TagBuilder>> {
		public static <T> TagLookup<T> empty() {
			return tagKey -> Optional.empty();
		}

		default public boolean contains(TagKey<T> tagKey) {
			return ((Optional) this.apply(tagKey)).isPresent();
		}
	}

	public static class TagAppender<T> {
		private final TagBuilder builder;

		protected TagAppender(TagBuilder tagBuilder) {
			this.builder = tagBuilder;
		}

		public final TagAppender<T> add(ResourceKey<T> resourceKey) {
			this.builder.addElement(resourceKey.location());
			return this;
		}

		public TagAppender<T> add(ResourceKey<T>... resourceKeys) {
			for (ResourceKey<T> resourceKey : resourceKeys) {
				this.builder.addElement(resourceKey.location());
			}
			return this;
		}

		public TagAppender<T> addOptional(ResourceLocation resourceLocation) {
			this.builder.addOptionalElement(resourceLocation);
			return this;
		}

		public TagAppender<T> addTag(TagKey<T> tagKey) {
			this.builder.addTag(tagKey.location());
			return this;
		}

		public TagAppender<T> addOptionalTag(ResourceLocation resourceLocation) {
			this.builder.addOptionalTag(resourceLocation);
			return this;
		}
	}
}
