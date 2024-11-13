package willatendo.simplelibrary.server.event.modification;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.List;

public final class FabricVillagerTradeModification implements VillagerTradeModification {
    @Override
    public void add(VillagerProfession villagerProfession, List<VillagerTrades.ItemListing> level1Trades, List<VillagerTrades.ItemListing> level2Trades, List<VillagerTrades.ItemListing> level3Trades, List<VillagerTrades.ItemListing> level4Trades, List<VillagerTrades.ItemListing> level5Trades) {
        TradeOfferHelper.registerVillagerOffers(villagerProfession, 1, itemListings -> itemListings.addAll(level1Trades));
        TradeOfferHelper.registerVillagerOffers(villagerProfession, 2, itemListings -> itemListings.addAll(level2Trades));
        TradeOfferHelper.registerVillagerOffers(villagerProfession, 3, itemListings -> itemListings.addAll(level3Trades));
        TradeOfferHelper.registerVillagerOffers(villagerProfession, 4, itemListings -> itemListings.addAll(level4Trades));
        TradeOfferHelper.registerVillagerOffers(villagerProfession, 5, itemListings -> itemListings.addAll(level5Trades));
    }
}
