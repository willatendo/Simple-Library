package willatendo.simplelibrary.server.event.modification;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.List;

public interface VillagerTradeModification {
    void add(VillagerProfession villagerProfession, List<VillagerTrades.ItemListing> level1Trades, List<VillagerTrades.ItemListing> level2Trades, List<VillagerTrades.ItemListing> level3Trades, List<VillagerTrades.ItemListing> level4Trades, List<VillagerTrades.ItemListing> level5Trades);
}
