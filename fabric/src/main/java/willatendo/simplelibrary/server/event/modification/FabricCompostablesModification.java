package willatendo.simplelibrary.server.event.modification;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public final class FabricCompostablesModification implements CompostablesModification {
    @Override
    public void add(ItemLike itemLike, float chance) {
        ComposterBlock.COMPOSTABLES.put(itemLike, chance);
    }
}
