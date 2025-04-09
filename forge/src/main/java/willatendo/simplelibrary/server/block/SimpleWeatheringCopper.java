package willatendo.simplelibrary.server.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.Supplier;

public interface SimpleWeatheringCopper extends ChangeOverTimeBlock<WeatheringCopper.WeatherState> {
    Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(HashBiMap::create);
    Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());

    static Optional<Block> getPrevious(Block block) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(block));
    }

    static Block getFirst(Block block) {
        for (Block blockIn = PREVIOUS_BY_BLOCK.get().get(block); blockIn != null; blockIn = PREVIOUS_BY_BLOCK.get().get(block)) {
            block = blockIn;
        }

        return block;
    }

    static Optional<BlockState> getPrevious(BlockState blockState) {
        return SimpleWeatheringCopper.getPrevious(blockState.getBlock()).map(block -> block.withPropertiesOf(blockState));
    }

    static Optional<Block> getNext(Block block) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(block));
    }

    static BlockState getFirst(BlockState blockState) {
        return SimpleWeatheringCopper.getFirst(blockState.getBlock()).withPropertiesOf(blockState);
    }

    default Optional<BlockState> getNext(BlockState blockState) {
        return SimpleWeatheringCopper.getNext(blockState.getBlock()).map(block -> block.withPropertiesOf(blockState));
    }

    default float getChanceModifier() {
        return this.getAge() == WeatheringCopper.WeatherState.UNAFFECTED ? 0.75F : 1.0F;
    }
}
