package willatendo.simplelibrary.server.event.modification;

import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.Block;

public final class ForgeWaxableModification implements WaxableModification {
    @Override
    public void add(Block in, Block out) {
        HoneycombItem.WAXABLES.get().put(in, out);
    }
}
