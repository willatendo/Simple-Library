package willatendo.simplelibrary.server.event.modification;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public final class CompostablesModification {
    public static void register(ItemLike itemLike, float chance) {
        ComposterBlock.COMPOSTABLES.put(itemLike, chance);
    }
}
