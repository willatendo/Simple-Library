package ca.willatendo.simplelibrary.server.biome_modifier;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.EnumSet;
import java.util.Set;

public final class BiomeModifiers {
    private BiomeModifiers() {
    }

    public record AddFeaturesBiomeModifier(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features, GenerationStep.Decoration step) implements BiomeModifier {
        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.ADD && this.biomes.contains(biome)) {
                BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
                this.features.forEach(holder -> generationSettings.addFeature(this.step, holder));
            }
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec() {
            return SimpleLibraryBiomeModifierSerializers.ADD_FEATURES_BIOME_MODIFIER_TYPE.get();
        }
    }

    public record RemoveFeaturesBiomeModifier(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features, Set<GenerationStep.Decoration> steps) implements BiomeModifier {
        public static RemoveFeaturesBiomeModifier allSteps(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features) {
            return new RemoveFeaturesBiomeModifier(biomes, features, EnumSet.allOf(GenerationStep.Decoration.class));
        }

        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.REMOVE && this.biomes.contains(biome)) {
                BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
                for (GenerationStep.Decoration step : this.steps) {
                    generationSettings.getFeatures(step).removeIf(this.features::contains);
                }
            }
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec() {
            return SimpleLibraryBiomeModifierSerializers.REMOVE_FEATURES_BIOME_MODIFIER_TYPE.get();
        }
    }

    public record AddSpawnsBiomeModifier(HolderSet<Biome> biomes, WeightedList<MobSpawnSettings.SpawnerData> spawners) implements BiomeModifier {
        public static AddSpawnsBiomeModifier singleSpawn(HolderSet<Biome> biomes, Weighted<MobSpawnSettings.SpawnerData> spawner) {
            return new AddSpawnsBiomeModifier(biomes, WeightedList.<MobSpawnSettings.SpawnerData>of(spawner));
        }

        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.ADD && this.biomes.contains(biome)) {
                MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
                for (Weighted<MobSpawnSettings.SpawnerData> spawner : this.spawners.unwrap()) {
                    EntityType<?> type = spawner.value().type();
                    spawns.addSpawn(type.getCategory(), spawner.weight(), spawner.value());
                }
            }
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec() {
            return SimpleLibraryBiomeModifierSerializers.ADD_SPAWNS_BIOME_MODIFIER_TYPE.get();
        }
    }

    public record RemoveSpawnsBiomeModifier(HolderSet<Biome> biomes, HolderSet<EntityType<?>> entityTypes) implements BiomeModifier {
        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.REMOVE && this.biomes.contains(biome)) {
                MobSpawnSettingsBuilder spawnBuilder = builder.getMobSpawnSettings();
                for (MobCategory category : MobCategory.values()) {
                    WeightedList.Builder<MobSpawnSettings.SpawnerData> spawns = spawnBuilder.getSpawner(category);
                    spawns.removeIf(spawnerData -> this.entityTypes.contains(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(((Weighted<MobSpawnSettings.SpawnerData>) spawnerData).value().type())));
                }
            }
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec() {
            return SimpleLibraryBiomeModifierSerializers.REMOVE_SPAWNS_BIOME_MODIFIER_TYPE.get();
        }
    }

    public record AddCarversBiomeModifier(HolderSet<Biome> biomes, HolderSet<ConfiguredWorldCarver<?>> carvers) implements BiomeModifier {
        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.ADD && this.biomes.contains(biome)) {
                BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
                this.carvers.forEach(generationSettings::addCarver);
            }
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec() {
            return SimpleLibraryBiomeModifierSerializers.ADD_CARVERS_BIOME_MODIFIER_TYPE.get();
        }
    }

    public record RemoveCarversBiomeModifier(HolderSet<Biome> biomes, HolderSet<ConfiguredWorldCarver<?>> carvers) implements BiomeModifier {
        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.REMOVE && this.biomes.contains(biome)) {
                BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
                generationSettings.getCarvers().removeIf(this.carvers::contains);
            }
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec() {
            return SimpleLibraryBiomeModifierSerializers.REMOVE_CARVERS_BIOME_MODIFIER_TYPE.get();
        }
    }

    public record AddSpawnCostsBiomeModifier(HolderSet<Biome> biomes, HolderSet<EntityType<?>> entityTypes, MobSpawnSettings.MobSpawnCost spawnCost) implements BiomeModifier {
        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.ADD) {
                MobSpawnSettingsBuilder spawnBuilder = builder.getMobSpawnSettings();
                for (var entityType : entityTypes) {
                    spawnBuilder.addMobCharge(entityType.value(), spawnCost.charge(), spawnCost.energyBudget());
                }
            }
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec() {
            return SimpleLibraryBiomeModifierSerializers.ADD_SPAWN_COSTS_BIOME_MODIFIER_TYPE.get();
        }
    }

    public record RemoveSpawnCostsBiomeModifier(HolderSet<Biome> biomes, HolderSet<EntityType<?>> entityTypes) implements BiomeModifier {
        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.REMOVE) {
                MobSpawnSettingsBuilder spawnBuilder = builder.getMobSpawnSettings();
                for (var entityType : entityTypes) {
                    spawnBuilder.removeSpawnCost(entityType.value());
                }
            }
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec() {
            return SimpleLibraryBiomeModifierSerializers.REMOVE_SPAWN_COSTS_BIOME_MODIFIER_TYPE.get();
        }
    }
}
