package willatendo.simplelibrary.server.event.modification;

import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public final class ForgeOxidationModification implements OxidationModification {
    public static final Map<Block, Block> OXIDATION_MAP = new HashMap<>();

    @Override
    public void add(Block in, Block out) {
        OXIDATION_MAP.put(in, out);
    }
}
