package willatendo.simplelibrary.client.event;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public record BlockModelEntry<T extends BlockEntity>(BlockEntityType<T> blockEntityType, BlockEntityRendererProvider<T> blockEntityRendererProvider) {
}
