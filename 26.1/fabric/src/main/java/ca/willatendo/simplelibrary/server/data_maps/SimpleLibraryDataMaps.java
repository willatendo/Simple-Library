package ca.willatendo.simplelibrary.server.data_maps;

import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import ca.willatendo.simplelibrary.server.data_maps.buillt_in.*;
import ca.willatendo.simplelibrary.server.event.DataMapEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;

public final class SimpleLibraryDataMaps {
    public static final DataMapType<EntityType<?>, AcceptableVillagerDistance> ACCEPTABLE_VILLAGER_DISTANCES = DataMapType.builder(SimpleLibraryDataMaps.id("acceptable_villager_distances"), Registries.ENTITY_TYPE, AcceptableVillagerDistance.CODEC).synced(AcceptableVillagerDistance.DISTANCE_CODEC, false).build();
    public static final DataMapType<Item, Compostable> COMPOSTABLES = DataMapType.builder(SimpleLibraryDataMaps.id("compostables"), Registries.ITEM, Compostable.CODEC).synced(Compostable.CHANCE_CODEC, false).build();
    public static final DataMapType<Item, FurnaceFuel> FURNACE_FUELS = DataMapType.builder(SimpleLibraryDataMaps.id("furnace_fuels"), Registries.ITEM, FurnaceFuel.CODEC).synced(FurnaceFuel.BURN_TIME_CODEC, false).build();
    public static final DataMapType<EntityType<?>, MonsterRoomMob> MONSTER_ROOM_MOBS = DataMapType.builder(SimpleLibraryDataMaps.id("monster_room_mobs"), Registries.ENTITY_TYPE, MonsterRoomMob.CODEC).synced(MonsterRoomMob.WEIGHT_CODEC, false).build();
    public static final DataMapType<Block, Oxidizable> OXIDIZABLES = DataMapType.builder(SimpleLibraryDataMaps.id("oxidizables"), Registries.BLOCK, Oxidizable.CODEC).synced(Oxidizable.OXIDIZABLE_CODEC, false).build();
    public static final DataMapType<EntityType<?>, ParrotImitation> PARROT_IMITATIONS = DataMapType.builder(SimpleLibraryDataMaps.id("parrot_imitations"), Registries.ENTITY_TYPE, ParrotImitation.CODEC).synced(ParrotImitation.SOUND_CODEC, false).build();
    public static final DataMapType<VillagerProfession, RaidHeroGift> RAID_HERO_GIFTS = DataMapType.builder(SimpleLibraryDataMaps.id("raid_hero_gifts"), Registries.VILLAGER_PROFESSION, RaidHeroGift.CODEC).synced(RaidHeroGift.LOOT_TABLE_CODEC, false).build();
    public static final DataMapType<Block, Strippable> STRIPPABLES = DataMapType.builder(SimpleLibraryDataMaps.id("strippables"), Registries.BLOCK, Strippable.CODEC).synced(Strippable.STRIPPED_BLOCK_CODEC, false).build();
    public static final DataMapType<GameEvent, VibrationFrequency> VIBRATION_FREQUENCIES = DataMapType.builder(SimpleLibraryDataMaps.id("vibration_frequencies"), Registries.GAME_EVENT, VibrationFrequency.CODEC).synced(VibrationFrequency.FREQUENCY_CODEC, false).build();
    public static final DataMapType<Biome, BiomeVillagerType> VILLAGER_TYPES = DataMapType.builder(SimpleLibraryDataMaps.id("villager_types"), Registries.BIOME, BiomeVillagerType.CODEC).synced(BiomeVillagerType.TYPE_CODEC, false).build();
    public static final DataMapType<Block, Waxable> WAXABLES = DataMapType.builder(SimpleLibraryDataMaps.id("waxables"), Registries.BLOCK, Waxable.CODEC).synced(Waxable.WAXABLE_CODEC, false).build();

    private SimpleLibraryDataMaps() {
    }

    private static Identifier id(String name) {
        return CoreUtils.resource("neoforge", name);
    }

    public static void init() {
        DataMapEvents.REGISTER_DATA_MAPS.register(dataMapTypes -> {
            DataMapEvents.register(dataMapTypes, (DataMapType) ACCEPTABLE_VILLAGER_DISTANCES);
            DataMapEvents.register(dataMapTypes, (DataMapType) COMPOSTABLES);
            DataMapEvents.register(dataMapTypes, (DataMapType) FURNACE_FUELS);
            DataMapEvents.register(dataMapTypes, (DataMapType) MONSTER_ROOM_MOBS);
            DataMapEvents.register(dataMapTypes, (DataMapType) OXIDIZABLES);
            DataMapEvents.register(dataMapTypes, (DataMapType) PARROT_IMITATIONS);
            DataMapEvents.register(dataMapTypes, (DataMapType) RAID_HERO_GIFTS);
            DataMapEvents.register(dataMapTypes, (DataMapType) STRIPPABLES);
            DataMapEvents.register(dataMapTypes, (DataMapType) VIBRATION_FREQUENCIES);
            DataMapEvents.register(dataMapTypes, (DataMapType) VILLAGER_TYPES);
            DataMapEvents.register(dataMapTypes, (DataMapType) WAXABLES);
        });
    }
}
