package willatendo.simplelibrary.server.event.modification;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.storage.loot.LootTable;
import willatendo.simplelibrary.server.registry.SimpleHolder;

import java.util.HashMap;

public final class ForgeHeroOfTheVillageGiftModification implements HeroOfTheVillageGiftModification {
    @Override
    public void add(SimpleHolder<VillagerProfession> villagerProfession, ResourceKey<LootTable> giftLootTable) {
        GiveGiftToHero.GIFTS = new HashMap<>(GiveGiftToHero.GIFTS);
        GiveGiftToHero.GIFTS.put(villagerProfession.get(), giftLootTable);
    }
}
