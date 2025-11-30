package willatendo.simplelibrary.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import willatendo.simplelibrary.client.filter.CreativeModeTabFilter;
import willatendo.simplelibrary.client.filter.Filter;

public class Client {
    public static void init() {
        ImmutableList.Builder<Filter> filters = ImmutableList.builder();
        filters.add(new Filter(ItemTags.SPRUCE_LOGS, new ItemStack(Blocks.SPRUCE_LOG)));
        CreativeModeTabFilter.create(CreativeModeTabs.getDefaultTab(), filters);
    }
}
