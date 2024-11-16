package willatendo.simplelibrary.server.event.modification;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;

public final class FabricStrippablesModification implements StrippablesModification {
    @Override
    public void register(Block in, Block out) {
        AxeItem.STRIPPABLES.put(in, out);
    }
}
