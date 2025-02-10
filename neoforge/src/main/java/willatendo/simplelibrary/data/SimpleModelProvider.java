package willatendo.simplelibrary.data;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;

public abstract class SimpleModelProvider extends ModelProvider {
    public SimpleModelProvider(PackOutput packOutput, String modId) {
        super(packOutput, modId);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.generateItemModels(itemModels);
        this.generateBlockModels(blockModels);
    }

    protected abstract void generateItemModels(ItemModelGenerators itemModelGenerators);

    protected abstract void generateBlockModels(BlockModelGenerators blockModelGenerators);
}
