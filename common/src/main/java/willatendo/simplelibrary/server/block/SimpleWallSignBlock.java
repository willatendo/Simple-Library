package willatendo.simplelibrary.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import willatendo.simplelibrary.server.block.entity.SimpleSignBlockEntity;

public class SimpleWallSignBlock extends WallSignBlock {
    public SimpleWallSignBlock(WoodType woodType, Properties properties) {
        super(woodType, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SimpleSignBlockEntity(blockPos, blockState);
    }
}
