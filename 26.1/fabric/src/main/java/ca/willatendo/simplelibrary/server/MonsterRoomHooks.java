package ca.willatendo.simplelibrary.server;

import ca.willatendo.simplelibrary.server.data_maps.SimpleLibraryDataMaps;
import ca.willatendo.simplelibrary.server.data_maps.buillt_in.MonsterRoomMob;
import ca.willatendo.simplelibrary.server.event.DataMapEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;

import java.util.Map;
import java.util.Objects;

public final class MonsterRoomHooks {
    private static WeightedList<EntityType> monsterRoomMobs = WeightedList.of();

    public static void init() {
        DataMapEvents.UPDATE_DATA_MAPS.register((registryAccess, registry, updateCause) -> {
            if (registry.key() == Registries.ENTITY_TYPE) {
                monsterRoomMobs = WeightedList.of(((Map<ResourceKey<EntityType<?>>, MonsterRoomMob>) registry.getDataMap(SimpleLibraryDataMaps.MONSTER_ROOM_MOBS)).entrySet().stream().map(entry -> {
                    EntityType type = (EntityType) Objects.requireNonNull(registry.getValue((ResourceKey) entry.getKey()), "Nonexistent entity " + entry.getKey() + " in monster room datamap!");
                    return new Weighted<>(type, entry.getValue().weight());
                }).toList());
            }
        });
    }

    public static EntityType<?> getRandomMonsterRoomMob(RandomSource rand) {
        return monsterRoomMobs.getRandomOrThrow(rand);
    }
}
