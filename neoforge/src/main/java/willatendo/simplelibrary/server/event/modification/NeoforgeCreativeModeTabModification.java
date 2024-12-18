package willatendo.simplelibrary.server.event.modification;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public final class NeoforgeCreativeModeTabModification implements CreativeModeTabModification {
    private final BuildCreativeModeTabContentsEvent event;

    public NeoforgeCreativeModeTabModification(BuildCreativeModeTabContentsEvent event) {
        this.event = event;
    }

    @Override
    public void add(ResourceKey<CreativeModeTab> creativeModeTab, ItemLike itemLike) {
        if (this.event.getTabKey() == creativeModeTab) {
            if (this.event.getTabKey() == CreativeModeTabs.OP_BLOCKS && this.event.hasPermissions()) {
                this.event.accept(itemLike);
            } else if (this.event.getTabKey() != CreativeModeTabs.OP_BLOCKS) {
                this.event.accept(itemLike);
            }
        }
    }

    @Override
    public void addBefore(ResourceKey<CreativeModeTab> creativeModeTab, ItemLike add, ItemLike before) {
        if (this.event.getTabKey() == creativeModeTab) {
            this.event.insertBefore(new ItemStack(before), new ItemStack(add), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    @Override
    public void addAfter(ResourceKey<CreativeModeTab> creativeModeTab, ItemLike add, ItemLike after) {
        if (this.event.getTabKey() == creativeModeTab) {
            this.event.insertAfter(new ItemStack(after), new ItemStack(add), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
