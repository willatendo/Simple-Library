package willatendo.simplelibrary.data;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import willatendo.simplelibrary.data.model.SimpleBlockModelGenerator;
import willatendo.simplelibrary.data.model.SimpleItemModelGenerator;

import java.util.function.Function;

public class SimpleModelProvider extends ModelProvider {
    private final Function<ItemModelGenerators, SimpleItemModelGenerator> simpleItemModelGenerator;
    private final Function<BlockModelGenerators, SimpleBlockModelGenerator> simpleBlockModelGeneratorFunction;

    public SimpleModelProvider(Function<ItemModelGenerators, SimpleItemModelGenerator> simpleItemModelGenerator, Function<BlockModelGenerators, SimpleBlockModelGenerator> simpleBlockModelGeneratorFunction, PackOutput packOutput, String modId) {
        super(packOutput, modId);
        this.simpleItemModelGenerator = simpleItemModelGenerator;
        this.simpleBlockModelGeneratorFunction = simpleBlockModelGeneratorFunction;
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModelGenerators, ItemModelGenerators itemModelGenerators) {
        this.simpleItemModelGenerator.apply(itemModelGenerators).run();
        this.simpleBlockModelGeneratorFunction.apply(blockModelGenerators).run();
    }
}
