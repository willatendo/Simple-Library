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

	protected SimpleTagsProvider.TagAppender<T> tag(TagKey<T> tagKey) {
		TagBuilder tagbuilder = this.getOrCreateRawBuilder(tagKey);
		return new SimpleTagsProvider.TagAppender<>(tagbuilder, this.modId);
	}

	protected TagBuilder getOrCreateRawBuilder(TagKey<T> tagKey) {
		return this.builders.computeIfAbsent(tagKey.location(), (resourceLocation) -> {
			if (this.existingFileHelper != null) {
				this.existingFileHelper.trackGenerated(resourceLocation, this.resourceType);
			}
			return TagBuilder.create();
		});
	}

	public CompletableFuture<SimpleTagsProvider.TagLookup<T>> contentsGetter() {
		return this.contentsDone.thenApply((void_) -> {
			return (tagKey) -> {
				return Optional.ofNullable(this.builders.get(tagKey.location()));
			};
		});
	}

	protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
		return this.lookupProvider.thenApply((provider) -> {
			this.builders.clear();
			this.addTags(provider);
			return provider;
		});
	}

	public static class TagAppender<T> {
		private final TagBuilder tagBuilder;
		private final String modId;

		protected TagAppender(TagBuilder tagBuilder, String modId) {
			this.tagBuilder = tagBuilder;
			this.modId = modId;
		}

		public final TagAppender<T> add(ResourceKey<T> resourceKey) {
			this.tagBuilder.addElement(resourceKey.location());
			return this;
		}

		public TagAppender<T> add(ResourceKey<T>... resourceKeys) {
			for (ResourceKey<T> resourceKey : resourceKeys) {
				this.tagBuilder.addElement(resourceKey.location());
			}
			return this;
		}

		public TagAppender<T> addOptional(ResourceLocation resourceLocation) {
			this.tagBuilder.addOptionalElement(resourceLocation);
			return this;
		}

		public TagAppender<T> addTag(TagKey<T> tagKey) {
			this.tagBuilder.addTag(tagKey.location());
			return this;
		}

		public TagAppender<T> addOptionalTag(ResourceLocation resourceLocation) {
			this.tagBuilder.addOptionalTag(resourceLocation);
			return this;
		}

		public SimpleTagsProvider.TagAppender<T> addTags(TagKey<T>... tagKeys) {
			for (TagKey<T> tagKey : tagKeys) {
				this.addTag(tagKey);
			}
			return this;
		}

		public SimpleTagsProvider.TagAppender<T> replace() {
			return this.replace(true);
		}

		public SimpleTagsProvider.TagAppender<T> replace(boolean value) {
			this.getInternalBuilder().replace(value);
			return this;
		}

		public SimpleTagsProvider.TagAppender<T> remove(ResourceLocation resourceLocation) {
			this.getInternalBuilder().removeElement(resourceLocation, this.getModId());
			return this;
		}

		public SimpleTagsProvider.TagAppender<T> remove(ResourceLocation first, ResourceLocation... resourceLocations) {
			this.remove(first);
			for (ResourceLocation resourceLocation : resourceLocations) {
				this.remove(resourceLocation);
			}
			return this;
		}

		public SimpleTagsProvider.TagAppender<T> remove(ResourceKey<T> resourceKey) {
			this.remove(resourceKey.location());
			return this;
		}

		public SimpleTagsProvider.TagAppender<T> remove(ResourceKey<T> firstResourceKey, ResourceKey<T>... resourceKeys) {
			this.remove(firstResourceKey.location());
			for (ResourceKey<T> resourceKey : resourceKeys) {
				this.remove(resourceKey.location());
			}
			return this;
		}

		public SimpleTagsProvider.TagAppender<T> remove(TagKey<T> tagKey) {
			this.getInternalBuilder().removeTag(tagKey.location(), this.getModId());
			return this;
		}

		public SimpleTagsProvider.TagAppender<T> remove(TagKey<T> first, TagKey<T>... tags) {
			this.remove(first);
			for (TagKey<T> tagKey : tags) {
				this.remove(tagKey);
			}
			return this;
		}

		public TagBuilder getInternalBuilder() {
			return this.tagBuilder;
		}

		public String getModId() {
			return this.modId;
		}
	}

	@FunctionalInterface
	public interface TagLookup<T> extends Function<TagKey<T>, Optional<TagBuilder>> {
		static <T> TagsProvider.TagLookup<T> empty() {
			return (tagKey) -> {
				return Optional.empty();
			};
		}

		default boolean contains(TagKey<T> tagKey) {
			return this.apply(tagKey).isPresent();
		}
	}
}
