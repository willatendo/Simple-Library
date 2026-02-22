package ca.willatendo.simplelibrary.server.stats;

import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import ca.willatendo.simplelibrary.server.stats.utils.RecipeBookUtils;
import com.google.common.base.CaseFormat;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.world.inventory.RecipeBookType;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public final class CustomRecipeBookSettings {
    public static final MapCodec<CustomRecipeBookSettings> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(CustomRecipeBookSettings.makeModdedRecipeBookTypesSettingsCodec().forGetter(customRecipeBookSettings -> customRecipeBookSettings.moddedSettings)).apply(instance, CustomRecipeBookSettings::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CustomRecipeBookSettings> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public CustomRecipeBookSettings decode(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
            Map<RecipeBookType, RecipeBookSettings.TypeSettings> map = new EnumMap<>(RecipeBookType.class);

            for (RecipeBookType recipeBookType : RecipeBookUtils.getModdedRecipeBookTypes().toList()) {
                map.put(recipeBookType, RecipeBookSettings.TypeSettings.STREAM_CODEC.decode(registryFriendlyByteBuf));
            }

            return new CustomRecipeBookSettings(map);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf registryFriendlyByteBuf, CustomRecipeBookSettings customRecipeBookSettings) {
            for (RecipeBookType recipeBookType : RecipeBookUtils.getModdedRecipeBookTypes().toList()) {
                RecipeBookSettings.TypeSettings typeSettings = customRecipeBookSettings.moddedSettings.getOrDefault(recipeBookType, RecipeBookSettings.TypeSettings.DEFAULT);
                RecipeBookSettings.TypeSettings.STREAM_CODEC.encode(registryFriendlyByteBuf, typeSettings);
            }
        }
    };

    private final Map<RecipeBookType, RecipeBookSettings.TypeSettings> moddedSettings;

    public static MapCodec<Map<RecipeBookType, RecipeBookSettings.TypeSettings>> makeModdedRecipeBookTypesSettingsCodec() {
        final Map<RecipeBookType, MapCodec<RecipeBookSettings.TypeSettings>> codecs = RecipeBookUtils.getModdedRecipeBookTypes().map(recipeBookType -> {
            String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, recipeBookType.name());
            String openName = "is" + name + "GuiOpen";
            String filteringName = "is" + name + "FilteringCraftable";
            MapCodec<RecipeBookSettings.TypeSettings> codec = RecipeBookSettings.TypeSettings.codec(openName, filteringName);
            return Pair.of(recipeBookType, codec);
        }).collect(Pair.toMap());
        return new MapCodec<>() {
            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return codecs.values().stream().flatMap(codec -> codec.keys(dynamicOps));
            }

            @Override
            public <T> DataResult<Map<RecipeBookType, RecipeBookSettings.TypeSettings>> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLikeInput) {
                Map<RecipeBookType, RecipeBookSettings.TypeSettings> map = new EnumMap<>(RecipeBookType.class);

                for (Map.Entry<RecipeBookType, MapCodec<RecipeBookSettings.TypeSettings>> entry : codecs.entrySet()) {
                    DataResult<RecipeBookSettings.TypeSettings> result = entry.getValue().decode(dynamicOps, mapLikeInput);
                    result.error().ifPresent(error -> SimpleCoreUtils.LOGGER.error("Failed to decode RecipeBookSettings.TypeSettings for key {}: {}", entry.getKey(), error));
                    result.result().ifPresent(settings -> map.put(entry.getKey(), settings));
                }

                return DataResult.success(map);
            }

            @Override
            public <T> RecordBuilder<T> encode(Map<RecipeBookType, RecipeBookSettings.TypeSettings> mapInput, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                Map.Entry<RecipeBookType, MapCodec<RecipeBookSettings.TypeSettings>> entry;
                for (Iterator<Map.Entry<RecipeBookType, MapCodec<RecipeBookSettings.TypeSettings>>> iterator = codecs.entrySet().iterator(); iterator.hasNext(); recordBuilder = ((MapCodec) entry.getValue()).encode(mapInput.getOrDefault(entry.getKey(), RecipeBookSettings.TypeSettings.DEFAULT), dynamicOps, recordBuilder)) {
                    entry = iterator.next();
                }

                return recordBuilder;
            }
        };
    }

    public CustomRecipeBookSettings() {
        this(new EnumMap<>(RecipeBookType.class));
    }

    public CustomRecipeBookSettings(Map<RecipeBookType, RecipeBookSettings.TypeSettings> moddedSettings) {
        this.moddedSettings = moddedSettings;
    }

    public RecipeBookSettings.TypeSettings getSettings(RecipeBookType recipeBookType) {
        return this.moddedSettings.getOrDefault(recipeBookType, RecipeBookSettings.TypeSettings.DEFAULT);
    }

    public void updateSettings(RecipeBookType recipeBookType, UnaryOperator<RecipeBookSettings.TypeSettings> updater) {
        this.moddedSettings.put(recipeBookType, updater.apply(this.moddedSettings.getOrDefault(recipeBookType, RecipeBookSettings.TypeSettings.DEFAULT)));
    }

    public boolean isOpen(RecipeBookType recipeBookType) {
        return this.getSettings(recipeBookType).open();
    }

    public void setOpen(RecipeBookType recipeBookType, boolean open) {
        this.updateSettings(recipeBookType, typeSettings -> typeSettings.setOpen(open));
    }

    public boolean isFiltering(RecipeBookType recipeBookType) {
        return this.getSettings(recipeBookType).filtering();
    }

    public void setFiltering(RecipeBookType recipeBookType, boolean filtering) {
        this.updateSettings(recipeBookType, typeSettings -> typeSettings.setFiltering(filtering));
    }

    public void replaceFrom(CustomRecipeBookSettings customRecipeBookSettings) {
        this.moddedSettings.clear();
        this.moddedSettings.putAll(customRecipeBookSettings.moddedSettings);
    }

    public CustomRecipeBookSettings copy() {
        return new CustomRecipeBookSettings(new EnumMap<>(this.moddedSettings));
    }
}
