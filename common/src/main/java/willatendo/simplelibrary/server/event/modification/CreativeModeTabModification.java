package willatendo.simplelibrary.server.event.modification;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

public interface CreativeModeTabModification {
    void add(ResourceKey<CreativeModeTab> creativeModeTab, ItemLike itemLike);
}
