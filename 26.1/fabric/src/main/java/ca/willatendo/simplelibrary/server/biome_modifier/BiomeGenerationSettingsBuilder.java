package ca.willatendo.simplelibrary.server.biome_modifier;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.ArrayList;
import java.util.List;

// Modified from Neoforge
public final class BiomeGenerationSettingsBuilder extends BiomeGenerationSettings.PlainBuilder {
    public BiomeGenerationSettingsBuilder(BiomeGenerationSettings biomeGenerationSettings) {
        biomeGenerationSettings.getCarvers().forEach(this.getCarvers()::add);
        biomeGenerationSettings.features().forEach(placedFeatureHolder -> {
            final ArrayList<Holder<PlacedFeature>> featureList = new ArrayList<>();
            placedFeatureHolder.forEach(featureList::add);
            this.features.add(featureList);
        });
    }

    public List<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration) {
        this.addFeatureStepsUpTo(decoration.ordinal());
        return this.features.get(decoration.ordinal());
    }

    public List<Holder<ConfiguredWorldCarver<?>>> getCarvers() {
        return this.carvers;
    }
}
