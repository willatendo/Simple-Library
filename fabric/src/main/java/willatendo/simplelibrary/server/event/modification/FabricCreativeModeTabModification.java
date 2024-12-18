package willatendo.simplelibrary.server.event.modification;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.Map;

public final class FabricCreativeModeTabModification implements CreativeModeTabModification {
    private final Map<ResourceKey<CreativeModeTab>, List<ItemLike>> addItems = Maps.newHashMap();
    private final Map<ResourceKey<CreativeModeTab>, List<Pair<ItemLike, ItemLike>>> addItemsBefore = Maps.newHashMap();
    private final Map<ResourceKey<CreativeModeTab>, List<Pair<ItemLike, ItemLike>>> addItemsAfter = Maps.newHashMap();

    public void build() {
        this.addItems.forEach((creativeModeTab, itemLikes) -> ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register(fabricItemGroupEntries -> {
            if (creativeModeTab == CreativeModeTabs.OP_BLOCKS && fabricItemGroupEntries.shouldShowOpRestrictedItems()) {
                itemLikes.forEach(fabricItemGroupEntries::accept);
            } else if (creativeModeTab != CreativeModeTabs.OP_BLOCKS) {
                itemLikes.forEach(fabricItemGroupEntries::accept);
            }
        }));
        this.addItemsBefore.forEach((creativeModeTab, itemLikes) -> ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register(fabricItemGroupEntries -> {
            if (creativeModeTab == CreativeModeTabs.OP_BLOCKS && fabricItemGroupEntries.shouldShowOpRestrictedItems()) {
                itemLikes.forEach(items -> fabricItemGroupEntries.addBefore(items.getFirst(), items.getSecond()));
            } else if (creativeModeTab != CreativeModeTabs.OP_BLOCKS) {
                itemLikes.forEach(items -> fabricItemGroupEntries.addBefore(items.getFirst(), items.getSecond()));
            }
        }));
        this.addItemsAfter.forEach((creativeModeTab, itemLikes) -> ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register(fabricItemGroupEntries -> {
            if (creativeModeTab == CreativeModeTabs.OP_BLOCKS && fabricItemGroupEntries.shouldShowOpRestrictedItems()) {
                itemLikes.forEach(items -> fabricItemGroupEntries.addAfter(items.getFirst(), items.getSecond()));
            } else if (creativeModeTab != CreativeModeTabs.OP_BLOCKS) {
                itemLikes.forEach(items -> fabricItemGroupEntries.addAfter(items.getFirst(), items.getSecond()));
            }
        }));
    }

    @Override
    public void add(ResourceKey<CreativeModeTab> creativeModeTab, ItemLike itemLike) {
        if (!this.addItems.containsKey(creativeModeTab)) {
            List<ItemLike> itemLikes = Lists.newArrayList();
            itemLikes.add(itemLike);
            this.addItems.put(creativeModeTab, itemLikes);
        } else {
            List<ItemLike> itemLikes = this.addItems.get(creativeModeTab);
            itemLikes.add(itemLike);
            this.addItems.replace(creativeModeTab, itemLikes);
        }
    }

    @Override
    public void addBefore(ResourceKey<CreativeModeTab> creativeModeTab, ItemLike add, ItemLike before) {
        if (!this.addItemsAfter.containsKey(creativeModeTab)) {
            List<Pair<ItemLike, ItemLike>> itemLikes = Lists.newArrayList();
            itemLikes.add(Pair.of(before, add));
            this.addItemsAfter.put(creativeModeTab, itemLikes);
        } else {
            List<Pair<ItemLike, ItemLike>> itemLikes = this.addItemsAfter.get(creativeModeTab);
            itemLikes.add(Pair.of(before, add));
            this.addItemsAfter.replace(creativeModeTab, itemLikes);
        }
    }

    @Override
    public void addAfter(ResourceKey<CreativeModeTab> creativeModeTab, ItemLike add, ItemLike after) {
        if (!this.addItemsAfter.containsKey(creativeModeTab)) {
            List<Pair<ItemLike, ItemLike>> itemLikes = Lists.newArrayList();
            itemLikes.add(Pair.of(after, add));
            this.addItemsAfter.put(creativeModeTab, itemLikes);
        } else {
            List<Pair<ItemLike, ItemLike>> itemLikes = this.addItemsAfter.get(creativeModeTab);
            itemLikes.add(Pair.of(after, add));
            this.addItemsAfter.replace(creativeModeTab, itemLikes);
        }
    }
}
