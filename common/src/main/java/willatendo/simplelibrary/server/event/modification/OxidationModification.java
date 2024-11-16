package willatendo.simplelibrary.server.event.modification;

import net.minecraft.world.level.block.Block;

public interface OxidationModification {
    void add(Block in, Block out);
}
