package ca.willatendo.simplelibrary.server.conditions;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.SimpleLibraryRegistries;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import com.mojang.serialization.MapCodec;

// Modified from Neoforge
public class SimpleLibraryConditions {
    public static final SimpleRegistry<MapCodec<? extends ICondition>> CONDITION_CODECS = new SimpleRegistry<>(SimpleLibraryRegistries.CONDITION_CODECS, "neoforge");

    public static final SimpleHolder<MapCodec<AndCondition>> AND_CONDITION = CONDITION_CODECS.register("and", () -> AndCondition.CODEC);
    public static final SimpleHolder<MapCodec<NeverCondition>> NEVER_CONDITION = CONDITION_CODECS.register("never", () -> NeverCondition.CODEC);
    public static final SimpleHolder<MapCodec<RegisteredCondition<?>>> REGISTERED_CONDITION = CONDITION_CODECS.register("registered", () -> RegisteredCondition.CODEC);
    public static final SimpleHolder<MapCodec<ModLoadedCondition>> MOD_LOADED_CONDITION = CONDITION_CODECS.register("mod_loaded", () -> ModLoadedCondition.CODEC);
    public static final SimpleHolder<MapCodec<NotCondition>> NOT_CONDITION = CONDITION_CODECS.register("not", () -> NotCondition.CODEC);
    public static final SimpleHolder<MapCodec<OrCondition>> OR_CONDITION = CONDITION_CODECS.register("or", () -> OrCondition.CODEC);
    public static final SimpleHolder<MapCodec<TagEmptyCondition<?>>> TAG_EMPTY_CONDITION = CONDITION_CODECS.register("tag_empty", () -> TagEmptyCondition.CODEC);
    public static final SimpleHolder<MapCodec<AlwaysCondition>> ALWAYS_CONDITION = CONDITION_CODECS.register("always", () -> AlwaysCondition.CODEC);
    public static final SimpleHolder<MapCodec<FeatureFlagsEnabledCondition>> FEATURE_FLAGS_ENABLED_CONDITION = CONDITION_CODECS.register("feature_flags_enabled", () -> FeatureFlagsEnabledCondition.CODEC);
}
