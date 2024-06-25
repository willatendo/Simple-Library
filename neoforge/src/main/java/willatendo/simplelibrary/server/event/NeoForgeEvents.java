package willatendo.simplelibrary.server.event;

import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import willatendo.simplelibrary.server.util.SimpleUtils;

import java.nio.file.Path;
import java.util.Optional;

public final class NeoForgeEvents {
    private final EventsHolder eventsHolder;

    public NeoForgeEvents(EventsHolder eventsHolder) {
        this.eventsHolder = eventsHolder;
    }

    @SubscribeEvent
    public void registerEntityAttributes(EntityAttributeCreationEvent event) {
        this.eventsHolder.attributes.forEach(attributeEntry -> {
            event.put(attributeEntry.entityType(), attributeEntry.attributeSupplier());
        });
    }

    @SubscribeEvent
    public void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        this.eventsHolder.spawnPlacements.forEach(spawnPlacementEntry -> {
            event.register(spawnPlacementEntry.entityType(), spawnPlacementEntry.spawnPlacementType(), spawnPlacementEntry.types(), spawnPlacementEntry.spawnPredicate(), SpawnPlacementRegisterEvent.Operation.OR);
        });
    }

    @SubscribeEvent
    public void registerRegistries(NewRegistryEvent event) {
        this.eventsHolder.registries.forEach(registry -> {
            event.register(registry);
        });
    }

    @SubscribeEvent
    public void registerResourcePacks(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            this.eventsHolder.resourcePackEntries.forEach(resourcePackEntry -> {
                String modId = resourcePackEntry.modId();
                String resourcePackName = resourcePackEntry.resourcePackName();
                Path resourcePath = ModList.get().getModFileById(modId).getFile().findResource("resourcepacks/" + resourcePackName);
                event.addRepositorySource(consumer -> {
                    Pack pack = Pack.readMetaAndCreate(new PackLocationInfo(SimpleUtils.resource(modId, "resourcepacks." + resourcePackName).toString(), SimpleUtils.translation(modId, "resourcePack", resourcePackName + ".description"), PackSource.BUILT_IN, Optional.empty()), new PathPackResources.PathResourcesSupplier(resourcePath), PackType.CLIENT_RESOURCES, new PackSelectionConfig(false, Pack.Position.TOP, false));
                    if (pack != null) {
                        consumer.accept(pack);
                    }
                });
            });
        }
    }
}
