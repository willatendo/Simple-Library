package willatendo.simplelibrary.server.event.modification;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.storage.loot.LootTable;
import willatendo.simplelibrary.server.registry.SimpleHolder;

public interface HeroOfTheVillageGiftModification {
    void add(SimpleHolder<VillagerProfession> villagerProfession, ResourceKey<LootTable> giftLootTable);
}
