package willatendo.simplelibrary.server.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleSignBlockEntity extends SignBlockEntity {
    public SimpleSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return SimpleBlockEntityTypes.SIMPLE_SIGN.get();
    }
}
