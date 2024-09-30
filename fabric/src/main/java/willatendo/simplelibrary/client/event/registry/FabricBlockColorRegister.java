package willatendo.simplelibrary.client.event.registry;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;

public final class FabricBlockColorRegister implements BlockColorRegistry {
    @Override
    public void registerBlockColor(BlockColor blockColor, Block... blocks) {
        ColorProviderRegistry.BLOCK.register(blockColor, blocks);
    }
}
