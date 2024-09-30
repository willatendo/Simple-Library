package willatendo.simplelibrary.server.event.modification;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

public final class FlammablesModification {
    public static void register(Block block, int encouragement, int flammability) {
        ((FireBlock) Blocks.FIRE).setFlammable(block, encouragement, flammability);
    }
}
