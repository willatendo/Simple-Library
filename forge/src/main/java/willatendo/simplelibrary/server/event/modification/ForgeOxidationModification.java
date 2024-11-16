package willatendo.simplelibrary.server.event.modification;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;

public final class ForgeOxidationModification implements OxidationModification {
    @Override
    public void add(Block in, Block out) {
        WeatheringCopper.NEXT_BY_BLOCK.get().put(in, out);
    }
}
