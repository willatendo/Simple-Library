package willatendo.simplelibrary.data;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import willatendo.simplelibrary.data.model.SimpleBlockModelGenerator;
import willatendo.simplelibrary.data.model.SimpleItemModelGenerator;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SimpleModelProvider extends ModelProvider {
    private final Function<ItemModelGenerators, SimpleItemModelGenerator> simpleItemModelGenerator;
    private final Function<BlockModelGenerators, SimpleBlockModelGenerator> simpleBlockModelGeneratorFunction;
    private final List<? extends Holder<Block>> forBlocks;
    private final List<? extends Holder<Item>> forItems;

    public SimpleModelProvider(Function<ItemModelGenerators, SimpleItemModelGenerator> simpleItemModelGenerator, Function<BlockModelGenerators, SimpleBlockModelGenerator> simpleBlockModelGeneratorFunction, PackOutput packOutput, String modId) {
        this(simpleItemModelGenerator, simpleBlockModelGeneratorFunction, packOutput, modId, List.of(), List.of());
    }

    private SimpleModelProvider(Function<ItemModelGenerators, SimpleItemModelGenerator> simpleItemModelGenerator, Function<BlockModelGenerators, SimpleBlockModelGenerator> simpleBlockModelGeneratorFunction, PackOutput packOutput, String modId, List<? extends Holder<Block>> forBlocks, List<? extends Holder<Item>> forItems) {
        super(packOutput, modId);
        this.simpleItemModelGenerator = simpleItemModelGenerator;
        this.simpleBlockModelGeneratorFunction = simpleBlockModelGeneratorFunction;
        if (!forBlocks.isEmpty()) {
            this.forBlocks = forBlocks;
        } else {
            this.forBlocks = List.of();
        }
        if (!forItems.isEmpty()) {
            this.forItems = forItems;
        } else {
            this.forItems = List.of();
        }
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return this.forBlocks.isEmpty() ? super.getKnownBlocks() : BuiltInRegistries.BLOCK.listElements().filter(this.forBlocks::contains);
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return this.forItems.isEmpty() ? super.getKnownItems() : BuiltInRegistries.ITEM.listElements().filter(this.forItems::contains);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModelGenerators, ItemModelGenerators itemModelGenerators) {
        this.simpleItemModelGenerator.apply(itemModelGenerators).run();
        this.simpleBlockModelGeneratorFunction.apply(blockModelGenerators).run();
    }
}
