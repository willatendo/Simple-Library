package willatendo.simplelibrary.server.event.modification;

import net.minecraft.world.level.block.Block;

public interface StrippablesModification {
    void register(Block in, Block out);
}
