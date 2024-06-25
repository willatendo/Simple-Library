package willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Predicate;

public class BiomeModificationEntries {
    public static record FeatureModification(Predicate<BiomeSelectionContext> selectionContext, GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> placedFeatureResourceKey) {
    }

    public static record SpawnModification(Predicate<BiomeSelectionContext> selectionContext, MobCategory mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
    }
}
