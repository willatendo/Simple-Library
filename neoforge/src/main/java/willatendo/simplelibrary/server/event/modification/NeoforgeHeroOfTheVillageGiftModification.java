package willatendo.simplelibrary.server.event.modification;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.RaidHeroGift;
import willatendo.simplelibrary.data.DataMapEntry;
import willatendo.simplelibrary.server.registry.SimpleHolder;

import java.util.Map;

public final class NeoforgeHeroOfTheVillageGiftModification implements HeroOfTheVillageGiftModification, DataMapEntry<VillagerProfession, RaidHeroGift> {
    private final Map<SimpleHolder<VillagerProfession>, ResourceKey<LootTable>> gifts = Maps.newHashMap();

    @Override
    public void add(SimpleHolder<VillagerProfession> villagerProfession, ResourceKey<LootTable> giftLootTable) {
        this.gifts.put(villagerProfession, giftLootTable);
    }

    @Override
    public DataMapType<VillagerProfession, RaidHeroGift> getDataMapType() {
        return NeoForgeDataMaps.RAID_HERO_GIFTS;
    }

    @Override
    public void addAll(DataMapProvider.Builder<RaidHeroGift, VillagerProfession> builder) {
        this.gifts.forEach((villagerProfession, giftLootTable) -> builder.add(villagerProfession.getKey(), new RaidHeroGift(giftLootTable), false));
    }
}
