package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public final class NeoforgeBlockColorRegister implements BlockColorRegistry {
    private final RegisterColorHandlersEvent.Block event;

    public NeoforgeBlockColorRegister(RegisterColorHandlersEvent.Block event) {
        this.event = event;
    }

    @Override
    public void registerBlockColor(BlockColor blockColor, Block... blocks) {
        this.event.register(blockColor, blocks);
    }
}
