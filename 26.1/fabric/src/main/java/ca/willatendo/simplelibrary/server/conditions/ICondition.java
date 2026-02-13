package ca.willatendo.simplelibrary.server.conditions;

import ca.willatendo.simplelibrary.core.registry.SimpleLibraryBuiltInRegistries;
import ca.willatendo.simplelibrary.server.ServerLifecycleHooks;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

// Modified from Neoforge
public interface ICondition {
    Codec<ICondition> CODEC = SimpleLibraryBuiltInRegistries.CONDITION_SERIALIZERS.byNameCodec().dispatch(ICondition::codec, Function.identity());
    Codec<List<ICondition>> LIST_CODEC = CODEC.listOf();

    static <V, T> Optional<T> getConditionally(Codec<T> codec, DynamicOps<V> ops, V element) {
        return ICondition.getWithConditionalCodec(ConditionalOps.createConditionalCodec(codec), ops, element);
    }

    static <V, T> Optional<T> getWithConditionalCodec(Codec<Optional<T>> codec, DynamicOps<V> ops, V element) {
        return codec.parse(ops, element).getOrThrow(JsonParseException::new);
    }

    static <V, T> Optional<T> getWithWithConditionsCodec(Codec<Optional<WithConditions<T>>> codec, DynamicOps<V> ops, V elements) {
        return codec.parse(ops, elements).promotePartial((string) -> {
        }).getOrThrow(JsonParseException::new).map(WithConditions::carrier);
    }

    static <V> boolean conditionsMatched(DynamicOps<V> ops, V element) {
        final Codec<Unit> codec = MapCodec.unitCodec(Unit.INSTANCE);
        return ICondition.getConditionally(codec, ops, element).isPresent();
    }

    static void writeConditions(HolderLookup.Provider registries, JsonObject jsonObject, ICondition... conditions) {
        ICondition.writeConditions(registries, jsonObject, List.of(conditions));
    }

    static void writeConditions(HolderLookup.Provider registries, JsonObject jsonObject, List<ICondition> conditions) {
        ICondition.writeConditions(RegistryOps.create(JsonOps.INSTANCE, registries), jsonObject, conditions);
    }

    static void writeConditions(DynamicOps<JsonElement> jsonOps, JsonObject jsonObject, List<ICondition> conditions) {
        if (!conditions.isEmpty()) {
            DataResult<JsonElement> result = LIST_CODEC.encodeStart(jsonOps, conditions);
            JsonElement serializedConditions = result.result().orElseThrow(() -> new RuntimeException("Failed to serialize conditions"));
            jsonObject.add(ConditionalOps.DEFAULT_CONDITIONS_KEY, serializedConditions);
        }
    }

    boolean test(IContext context);

    MapCodec<? extends ICondition> codec();

    interface IContext {
        IContext EMPTY = new IContext() {
            @Override
            public <T> boolean isTagLoaded(TagKey<T> key) {
                return false;
            }
        };

        IContext TAGS_INVALID = new IContext() {
            @Override
            public <T> boolean isTagLoaded(TagKey<T> key) {
                throw new UnsupportedOperationException("Usage of tag-based conditions is not permitted in this context!");
            }
        };

        <T> boolean isTagLoaded(TagKey<T> key);

        default RegistryAccess registryAccess() {
            return RegistryAccess.EMPTY;
        }

        default FeatureFlagSet enabledFeatures() {
            MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
            return minecraftServer == null ? FeatureFlags.VANILLA_SET : minecraftServer.getWorldData().enabledFeatures();
        }
    }
}
