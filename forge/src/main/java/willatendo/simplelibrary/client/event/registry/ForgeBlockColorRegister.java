package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public final class ForgeBlockColorRegister implements BlockColorRegistry {
    private final RegisterColorHandlersEvent.Block event;

    public ForgeBlockColorRegister(RegisterColorHandlersEvent.Block event) {
        this.event = event;
    }

    @Override
    public void registerBlockColor(BlockColor blockColor, Block... blocks) {
        this.event.register(blockColor, blocks);
    }
}
