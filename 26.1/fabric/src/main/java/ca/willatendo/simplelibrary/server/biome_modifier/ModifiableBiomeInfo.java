package ca.willatendo.simplelibrary.server.biome_modifier;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

// Modified from Neoforge
public final class ModifiableBiomeInfo {
    private static final Logger LOGGER = LogManager.getLogger(ModifiableBiomeInfo.class);

    private final BiomeInfo originalBiomeInfo;
    private BiomeInfo modifiedBiomeInfo = null;

    public ModifiableBiomeInfo(final BiomeInfo originalBiomeInfo) {
        this.originalBiomeInfo = originalBiomeInfo;
    }

    public BiomeInfo get() {
        return this.modifiedBiomeInfo == null ? this.originalBiomeInfo : this.modifiedBiomeInfo;
    }

    public BiomeInfo getOriginalBiomeInfo() {
        return this.originalBiomeInfo;
    }

    public BiomeInfo getModifiedBiomeInfo() {
        return this.modifiedBiomeInfo;
    }

    public boolean applyBiomeModifiers(final Holder<Biome> biome, final List<BiomeModifier> biomeModifiers, RegistryAccess registryAccess) {
        if (this.modifiedBiomeInfo != null) {
            LOGGER.info("Modified already exists");
            return true;
        }

        BiomeInfo original = this.getOriginalBiomeInfo();
        BiomeInfo.Builder builder = BiomeInfo.Builder.copyOf(original);
        for (BiomeModifier.Phase phase : BiomeModifier.Phase.values()) {
            for (BiomeModifier modifier : biomeModifiers) {
                modifier.modify(biome, phase, builder);
            }
        }
        DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registryAccess);
        JsonElement originalJson = Biome.NETWORK_CODEC.encodeStart(ops, biome.value()).result().orElse(null);
        this.modifiedBiomeInfo = builder.build();
        JsonElement modifiedJson = Biome.NETWORK_CODEC.encodeStart(ops, biome.value()).result().orElse(null);
        if (originalJson == null || modifiedJson == null) {
            LOGGER.warn("Failed to determine whether biome {} was modified", biome);
            return true;
        }
        return !originalJson.equals(modifiedJson);
    }

    public record BiomeInfo(Biome.ClimateSettings climateSettings, BiomeSpecialEffects effects, BiomeGenerationSettings generationSettings, MobSpawnSettings mobSpawnSettings) {
        public static final class Builder {
            private final ClimateSettingsBuilder climateSettings;
            private final BiomeSpecialEffectsBuilder effects;
            private final BiomeGenerationSettingsBuilder generationSettings;
            private final MobSpawnSettingsBuilder mobSpawnSettings;

            public static Builder copyOf(final BiomeInfo original) {
                final ClimateSettingsBuilder climateBuilder = ClimateSettingsBuilder.copyOf(original.climateSettings());
                final BiomeSpecialEffectsBuilder effectsBuilder = BiomeSpecialEffectsBuilder.copyOf(original.effects());
                final BiomeGenerationSettingsBuilder generationBuilder = new BiomeGenerationSettingsBuilder(original.generationSettings());
                final MobSpawnSettingsBuilder mobSpawnBuilder = new MobSpawnSettingsBuilder(original.mobSpawnSettings());

                return new Builder(climateBuilder, effectsBuilder, generationBuilder, mobSpawnBuilder);
            }

            private Builder(final ClimateSettingsBuilder climateSettings, final BiomeSpecialEffectsBuilder effects, final BiomeGenerationSettingsBuilder generationSettings, final MobSpawnSettingsBuilder mobSpawnSettings) {
                this.climateSettings = climateSettings;
                this.effects = effects;
                this.generationSettings = generationSettings;
                this.mobSpawnSettings = mobSpawnSettings;
            }

            public BiomeInfo build() {
                return new BiomeInfo(this.climateSettings.build(), this.effects.build(), this.generationSettings.build(), this.mobSpawnSettings.build());
            }

            public ClimateSettingsBuilder getClimateSettings() {
                return this.climateSettings;
            }

            public BiomeSpecialEffectsBuilder getSpecialEffects() {
                return this.effects;
            }

            public BiomeGenerationSettingsBuilder getGenerationSettings() {
                return this.generationSettings;
            }

            public MobSpawnSettingsBuilder getMobSpawnSettings() {
                return this.mobSpawnSettings;
            }
        }
    }
}
