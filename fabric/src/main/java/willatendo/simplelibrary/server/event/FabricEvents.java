package willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import willatendo.simplelibrary.server.util.SimpleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class FabricEvents {
    private final EventsHolder eventsHolder;
    private final List<BiomeModificationEntries.FeatureModification> featureModificationEntryList = new ArrayList<>();
    private final List<BiomeModificationEntries.SpawnModification> spawnModificationEntryList = new ArrayList<>();

    public FabricEvents(EventsHolder eventsHolder) {
        this.eventsHolder = eventsHolder;
    }

    public FabricEvents addFeatureModification(Predicate<BiomeSelectionContext> selectionContext, GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> placedFeatureResourceKey) {
        this.featureModificationEntryList.add(new BiomeModificationEntries.FeatureModification(selectionContext, decoration, placedFeatureResourceKey));
        return this;
    }

    public FabricEvents addSpawnModification(Predicate<BiomeSelectionContext> selectionContext, MobCategory mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        this.spawnModificationEntryList.add(new BiomeModificationEntries.SpawnModification(selectionContext, mobCategory, entityType, weight, minGroupSize, maxGroupSize));
        return this;
    }

    public void runEvents() {
        this.featureModificationEntryList.forEach(biomeModificationEntry -> {
            BiomeModifications.addFeature(biomeModificationEntry.selectionContext(), biomeModificationEntry.decoration(), biomeModificationEntry.placedFeatureResourceKey());
        });

        this.spawnModificationEntryList.forEach(spawnModification -> {
            BiomeModifications.addSpawn(spawnModification.selectionContext(), spawnModification.mobCategory(), spawnModification.entityType(), spawnModification.weight(), spawnModification.minGroupSize(), spawnModification.maxGroupSize());
        });

        this.eventsHolder.attributes.forEach(attributeEntry -> {
            FabricDefaultAttributeRegistry.register(attributeEntry.getEntityType(), attributeEntry.getAttributeSupplier());
        });

        this.eventsHolder.spawnPlacements.forEach(spawnPlacementEntry -> {
            SpawnPlacements.register(spawnPlacementEntry.getEntityType(), spawnPlacementEntry.spawnPlacementType(), spawnPlacementEntry.types(), spawnPlacementEntry.spawnPredicate());
        });

        this.eventsHolder.resourcePackEntries.forEach(resourcePackEntry -> {
            String modId = resourcePackEntry.modId();
            String resourcePackName = resourcePackEntry.resourcePackName();
            Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modId);
            ResourceManagerHelper.registerBuiltinResourcePack(SimpleUtils.resource(modId, resourcePackName), modContainer.get(), SimpleUtils.translation(modId, "pack", resourcePackName), ResourcePackActivationType.NORMAL);
        });
    }
}
