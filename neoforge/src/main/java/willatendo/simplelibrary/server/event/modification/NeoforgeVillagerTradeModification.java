package willatendo.simplelibrary.server.event.modification;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;

public final class NeoforgeVillagerTradeModification implements VillagerTradeModification {
    private final VillagerTradesEvent event;

    public NeoforgeVillagerTradeModification(VillagerTradesEvent event) {
        this.event = event;
    }

    @Override
    public void add(VillagerProfession villagerProfession, List<VillagerTrades.ItemListing> level1Trades, List<VillagerTrades.ItemListing> level2Trades, List<VillagerTrades.ItemListing> level3Trades, List<VillagerTrades.ItemListing> level4Trades, List<VillagerTrades.ItemListing> level5Trades) {
        if (this.event.getType() == villagerProfession) {
            this.event.getTrades().get(1).addAll(level1Trades);
            this.event.getTrades().get(2).addAll(level2Trades);
            this.event.getTrades().get(3).addAll(level3Trades);
            this.event.getTrades().get(4).addAll(level4Trades);
            this.event.getTrades().get(5).addAll(level5Trades);
        }
    }
}
