package ca.willatendo.simplelibrary.data.providers;

import ca.willatendo.simplelibrary.server.conditions.ConditionalOps;
import ca.willatendo.simplelibrary.server.conditions.ICondition;
import ca.willatendo.simplelibrary.server.conditions.WithConditions;
import ca.willatendo.simplelibrary.server.data_maps.*;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class SimpleDataMapProvider implements DataProvider {
    protected final CompletableFuture<HolderLookup.Provider> lookupProvider;
    protected final PackOutput.PathProvider pathProvider;
    private final String modId;
    private final Map<DataMapType<?, ?>, Builder<?, ?>> builders = new HashMap<>();

    public SimpleDataMapProvider(PackOutput packOutput, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.lookupProvider = lookupProvider;
        this.modId = modId;
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, DataMapLoader.PATH);
    }

    protected abstract void gather(HolderLookup.Provider provider);

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return lookupProvider.thenCompose(provider -> {
            gather(provider);

            final DynamicOps<JsonElement> dynamicOps = provider.createSerializationContext(JsonOps.INSTANCE);

            return CompletableFuture.allOf(this.builders.entrySet().stream().map(entry -> {
                DataMapType<?, ?> type = entry.getKey();
                final Path path = this.pathProvider.json(type.id().withPrefix(DataMapLoader.getFolderLocation(type.registryKey().identifier()) + "/"));
                return generate(path, cache, entry.getValue(), dynamicOps);
            }).toArray(CompletableFuture[]::new));
        });
    }

    private <T, R> CompletableFuture<?> generate(Path out, CachedOutput cache, Builder<T, R> builder, DynamicOps<JsonElement> ops) {
        return CompletableFuture.supplyAsync(() -> {
            final Codec<Optional<WithConditions<DataMapFile<T, R>>>> withConditionsCodec = ConditionalOps.createConditionalCodecWithConditions(DataMapFile.codec(builder.registryKey, builder.type));
            return withConditionsCodec.encodeStart(ops, Optional.of(builder.build())).getOrThrow(msg -> new RuntimeException("Failed to encode %s: %s".formatted(out, msg)));
        }).thenComposeAsync(encoded -> DataProvider.saveStable(cache, encoded, out));
    }

    public <T, R> Builder<T, R> builder(DataMapType<R, T> type) {
        if (type instanceof AdvancedDataMapType<R, T, ?> advanced) {
            return builder(advanced);
        }
        return (Builder<T, R>) builders.computeIfAbsent(type, k -> new Builder<>(type));
    }

    public <T, R, VR extends DataMapValueRemover<R, T>> AdvancedBuilder<T, R, VR> builder(AdvancedDataMapType<R, T, VR> type) {
        return (AdvancedBuilder<T, R, VR>) builders.computeIfAbsent(type, k -> new AdvancedBuilder<>(type));
    }

    @Override
    public String getName() {
        return "SimpleLibrary: Data Map Provider for " + this.modId;
    }

    public static class Builder<T, R> {
        private final Map<Either<TagKey<R>, ResourceKey<R>>, Optional<WithConditions<DataMapEntry<T>>>> values = new LinkedHashMap<>();
        protected final List<DataMapEntry.Removal<T, R>> removals = new ArrayList<>();
        protected final ResourceKey<Registry<R>> registryKey;
        private final DataMapType<R, T> type;
        private final List<ICondition> conditions = new ArrayList<>();

        private boolean replace;

        public Builder(DataMapType<R, T> type) {
            this.type = type;
            this.registryKey = type.registryKey();
        }

        public Builder<T, R> add(ResourceKey<R> key, T value, boolean replace, ICondition... conditions) {
            this.values.put(Either.right(key), Optional.of(new WithConditions<>(new DataMapEntry<>(value, replace), conditions)));
            return this;
        }

        public Builder<T, R> add(Identifier id, T value, boolean replace, ICondition... conditions) {
            return add(ResourceKey.create(registryKey, id), value, replace, conditions);
        }

        public Builder<T, R> add(Holder<R> object, T value, boolean replace, ICondition... conditions) {
            return add(object.unwrapKey().orElseThrow(), value, replace, conditions);
        }

        public Builder<T, R> add(TagKey<R> tag, T value, boolean replace, ICondition... conditions) {
            this.values.put(Either.left(tag), Optional.of(new WithConditions<>(new DataMapEntry<>(value, replace), conditions)));
            return this;
        }

        public Builder<T, R> remove(Identifier id) {
            this.removals.add(new DataMapEntry.Removal<>(Either.right(ResourceKey.create(registryKey, id)), Optional.empty()));
            return this;
        }

        public Builder<T, R> remove(TagKey<R> tag) {
            this.removals.add(new DataMapEntry.Removal<>(Either.left(tag), Optional.empty()));
            return this;
        }

        public Builder<T, R> remove(Holder<R> value) {
            this.removals.add(new DataMapEntry.Removal<>(Either.right(value.unwrap().orThrow()), Optional.empty()));
            return this;
        }

        public Builder<T, R> replace(boolean replace) {
            this.replace = replace;
            return this;
        }

        public Builder<T, R> conditions(ICondition... conditions) {
            Collections.addAll(this.conditions, conditions);
            return this;
        }

        public WithConditions<DataMapFile<T, R>> build() {
            return new WithConditions<>(conditions, new DataMapFile<>(replace, values, removals));
        }
    }

    public static class AdvancedBuilder<T, R, VR extends DataMapValueRemover<R, T>> extends Builder<T, R> {
        public AdvancedBuilder(AdvancedDataMapType<R, T, VR> type) {
            super(type);
        }

        public AdvancedBuilder<T, R, VR> remove(TagKey<R> tag, VR remover) {
            this.removals.add(new DataMapEntry.Removal<>(Either.left(tag), Optional.of(remover)));
            return this;
        }

        public AdvancedBuilder<T, R, VR> remove(Holder<R> value, VR remover) {
            this.removals.add(new DataMapEntry.Removal<>(Either.right(value.unwrap().orThrow()), Optional.of(remover)));
            return this;
        }

        public AdvancedBuilder<T, R, VR> remove(Identifier id, VR remover) {
            this.removals.add(new DataMapEntry.Removal<>(Either.right(ResourceKey.create(registryKey, id)), Optional.of(remover)));
            return this;
        }
    }
}
