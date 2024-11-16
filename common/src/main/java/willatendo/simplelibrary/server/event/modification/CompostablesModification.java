package willatendo.simplelibrary.server.event.modification;

import net.minecraft.world.level.ItemLike;

public interface CompostablesModification {
    void add(ItemLike itemLike, float chance);
}
