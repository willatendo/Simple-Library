package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.block.Block;

public interface BlockColorRegistry {
    void registerBlockColor(BlockColor blockColor, Block... blocks);

    default void registerLeavesColor(Block... blocks) {
        this.registerBlockColor((blockState, blockAndTintGetter, blockPos, tintIndex) -> blockAndTintGetter != null && blockPos != null ? BiomeColors.getAverageFoliageColor(blockAndTintGetter, blockPos) : -12012264, blocks);
    }
}
