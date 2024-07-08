package willatendo.simplelibrary.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import willatendo.simplelibrary.server.block.entity.SimpleSignBlockEntity;

public class SimpleStandingSignBlock extends StandingSignBlock {
    public SimpleStandingSignBlock(WoodType woodType, Properties properties) {
        super(woodType, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SimpleSignBlockEntity(blockPos, blockState);
    }
}
