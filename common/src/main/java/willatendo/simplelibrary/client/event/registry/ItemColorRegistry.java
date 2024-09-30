package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;

public interface ItemColorRegistry {
    BlockColors getBlockColors();

    void registerItemColor(ItemColor itemColor, ItemLike... items);

    default void registerLeavesColor(ItemLike... items) {
        this.registerItemColor((itemStack, tintIndex) -> {
            BlockState blockState = ((BlockItem) itemStack.getItem()).getBlock().defaultBlockState();
            return this.getBlockColors().getColor(blockState, null, null, tintIndex);
        }, items);
    }
}
