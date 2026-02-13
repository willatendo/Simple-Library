package ca.willatendo.simplelibrary.server.biome_modifier;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.SimpleLibraryRegistries;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public final class SimpleLibraryBiomeModifierSerializers {
    public static final SimpleRegistry<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = new SimpleRegistry<>(SimpleLibraryRegistries.BIOME_MODIFIER_SERIALIZERS, "neoforge");

    public static final SimpleHolder<MapCodec<NoneBiomeModifier>> NONE_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("none", () -> MapCodec.unit(NoneBiomeModifier.INSTANCE));
    public static final SimpleHolder<MapCodec<BiomeModifiers.AddFeaturesBiomeModifier>> ADD_FEATURES_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("add_features", () -> RecordCodecBuilder.mapCodec(builder -> builder.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(BiomeModifiers.AddFeaturesBiomeModifier::biomes), PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(BiomeModifiers.AddFeaturesBiomeModifier::features), GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(BiomeModifiers.AddFeaturesBiomeModifier::step)).apply(builder, BiomeModifiers.AddFeaturesBiomeModifier::new)));
    public static final SimpleHolder<MapCodec<BiomeModifiers.RemoveFeaturesBiomeModifier>> REMOVE_FEATURES_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("remove_features", () -> RecordCodecBuilder.mapCodec(builder -> builder.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(BiomeModifiers.RemoveFeaturesBiomeModifier::biomes), PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(BiomeModifiers.RemoveFeaturesBiomeModifier::features), Codec.either(GenerationStep.Decoration.CODEC.listOf(), GenerationStep.Decoration.CODEC).xmap((either) -> either.map(Set::copyOf, Set::of), set -> set.size() == 1 ? Either.right(((GenerationStep.Decoration[]) set.toArray(GenerationStep.Decoration[]::new))[0]) : Either.left(List.copyOf(set))).optionalFieldOf("steps", EnumSet.allOf(GenerationStep.Decoration.class)).forGetter(BiomeModifiers.RemoveFeaturesBiomeModifier::steps)).apply(builder, BiomeModifiers.RemoveFeaturesBiomeModifier::new)));
    public static final SimpleHolder<MapCodec<BiomeModifiers.AddSpawnsBiomeModifier>> ADD_SPAWNS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("add_spawns", () -> RecordCodecBuilder.mapCodec(instance -> instance.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(BiomeModifiers.AddSpawnsBiomeModifier::biomes), Codec.either(WeightedList.codec(MobSpawnSettings.SpawnerData.CODEC), Weighted.codec(MobSpawnSettings.SpawnerData.CODEC)).xmap(either -> either.map(Function.identity(), WeightedList::<MobSpawnSettings.SpawnerData>of), list -> list.unwrap().size() == 1 ? Either.right(list.unwrap().getFirst()) : Either.left(list)).fieldOf("spawners").forGetter(BiomeModifiers.AddSpawnsBiomeModifier::spawners)).apply(instance, BiomeModifiers.AddSpawnsBiomeModifier::new)));
    public static final SimpleHolder<MapCodec<BiomeModifiers.RemoveSpawnsBiomeModifier>> REMOVE_SPAWNS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("remove_spawns", () -> RecordCodecBuilder.mapCodec(builder -> builder.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(BiomeModifiers.RemoveSpawnsBiomeModifier::biomes), RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).fieldOf("entity_types").forGetter(BiomeModifiers.RemoveSpawnsBiomeModifier::entityTypes)).apply(builder, BiomeModifiers.RemoveSpawnsBiomeModifier::new)));
    public static final SimpleHolder<MapCodec<BiomeModifiers.AddCarversBiomeModifier>> ADD_CARVERS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("add_carvers", () -> RecordCodecBuilder.mapCodec(builder -> builder.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(BiomeModifiers.AddCarversBiomeModifier::biomes), ConfiguredWorldCarver.LIST_CODEC.fieldOf("carvers").forGetter(BiomeModifiers.AddCarversBiomeModifier::carvers)).apply(builder, BiomeModifiers.AddCarversBiomeModifier::new)));
    public static final SimpleHolder<MapCodec<BiomeModifiers.RemoveCarversBiomeModifier>> REMOVE_CARVERS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("remove_carvers", () -> RecordCodecBuilder.mapCodec(builder -> builder.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(BiomeModifiers.RemoveCarversBiomeModifier::biomes), ConfiguredWorldCarver.LIST_CODEC.fieldOf("carvers").forGetter(BiomeModifiers.RemoveCarversBiomeModifier::carvers)).apply(builder, BiomeModifiers.RemoveCarversBiomeModifier::new)));
    public static final SimpleHolder<MapCodec<BiomeModifiers.AddSpawnCostsBiomeModifier>> ADD_SPAWN_COSTS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("add_spawn_costs", () -> RecordCodecBuilder.mapCodec(builder -> builder.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(BiomeModifiers.AddSpawnCostsBiomeModifier::biomes), RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).fieldOf("entity_types").forGetter(BiomeModifiers.AddSpawnCostsBiomeModifier::entityTypes), MobSpawnSettings.MobSpawnCost.CODEC.fieldOf("spawn_cost").forGetter(BiomeModifiers.AddSpawnCostsBiomeModifier::spawnCost)).apply(builder, BiomeModifiers.AddSpawnCostsBiomeModifier::new)));
    public static final SimpleHolder<MapCodec<BiomeModifiers.RemoveSpawnCostsBiomeModifier>> REMOVE_SPAWN_COSTS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("remove_spawn_costs", () -> RecordCodecBuilder.mapCodec(builder -> builder.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(BiomeModifiers.RemoveSpawnCostsBiomeModifier::biomes), RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).fieldOf("entity_types").forGetter(BiomeModifiers.RemoveSpawnCostsBiomeModifier::entityTypes)).apply(builder, BiomeModifiers.RemoveSpawnCostsBiomeModifier::new)));

    private SimpleLibraryBiomeModifierSerializers() {
    }
}
