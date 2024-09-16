package willatendo.simplelibrary.server.event.modification;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;

public class FabricCreativeModeTabModification implements CreativeModeTabModification {
    private final Map<ResourceKey<CreativeModeTab>, List<ItemLike>> items = Maps.newHashMap();

    public void build() {
        this.items.forEach((creativeModeTab, itemLikes) -> ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register(fabricItemGroupEntries -> {
            if (creativeModeTab == CreativeModeTabs.OP_BLOCKS && fabricItemGroupEntries.shouldShowOpRestrictedItems()) {
                itemLikes.forEach(fabricItemGroupEntries::accept);
            } else if(creativeModeTab != CreativeModeTabs.OP_BLOCKS) {
                itemLikes.forEach(fabricItemGroupEntries::accept);
            }
        }));
    }

    @Override
    public void add(ResourceKey<CreativeModeTab> creativeModeTab, ItemLike itemLike) {
        if (!this.items.containsKey(creativeModeTab)) {
            List<ItemLike> itemLikes = Lists.newArrayList();
            itemLikes.add(itemLike);
            this.items.put(creativeModeTab, itemLikes);
        } else {
            List<ItemLike> itemLikes = this.items.get(creativeModeTab);
            itemLikes.add(itemLike);
            this.items.replace(creativeModeTab, itemLikes);
        }
    }
}
