package willatendo.simplelibrary.server.event.modification;

import net.minecraft.world.level.block.Block;
import willatendo.simplelibrary.server.block.SimpleWeatheringCopper;

import java.util.HashMap;
import java.util.Map;

public final class ForgeOxidationModification implements OxidationModification {
    private final Map<Block, Block> oxidationMap = new HashMap<>();

    @Override
    public void add(Block in, Block out) {
        SimpleWeatheringCopper.NEXT_BY_BLOCK.get().put(in, out);
    }

    public void build() {
    }
}
