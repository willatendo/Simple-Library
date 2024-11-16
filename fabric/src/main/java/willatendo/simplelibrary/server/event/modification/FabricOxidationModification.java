package willatendo.simplelibrary.server.event.modification;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.world.level.block.Block;

public final class FabricOxidationModification implements OxidationModification {
    @Override
    public void add(Block in, Block out) {
        OxidizableBlocksRegistry.registerOxidizableBlockPair(in, out);
    }
}
