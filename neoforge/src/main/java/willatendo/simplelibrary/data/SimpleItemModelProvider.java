package willatendo.simplelibrary.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;

public abstract class SimpleItemModelProvider extends ItemModelProvider {
    public SimpleItemModelProvider(PackOutput packOutput, String modId, ExistingFileHelper existingFileHelper) {
        super(packOutput, modId, existingFileHelper);
    }

    public ItemModelBuilder handheldItem(Item item) {
        return this.handheldItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)), ResourceLocation.fromNamespaceAndPath(BuiltInRegistries.ITEM.getKey(item).getNamespace(), "item/" + BuiltInRegistries.ITEM.getKey(item).getPath()));
    }

    public ItemModelBuilder handheldItem(Item item, ResourceLocation texture) {
        return this.handheldItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)), texture);
    }

    public ItemModelBuilder basicItem(Item item, ResourceLocation texture) {
        return this.basicItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)), texture);
    }

    public ItemModelBuilder basicItem(ResourceLocation item, ResourceLocation texture) {
        return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", texture);
    }

    public ItemModelBuilder handheldItem(ResourceLocation item, ResourceLocation texture) {
        return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/handheld")).texture("layer0", texture);
    }

    public ItemModelBuilder handheldItem(Item item, ResourceLocation[] textures) {
        return this.handheldItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)), textures);
    }

    public ItemModelBuilder basicItem(Item item, ResourceLocation[] textures) {
        return this.basicItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)), textures);
    }

    public ItemModelBuilder basicItem(ResourceLocation item, ResourceLocation[] textures) {
        ItemModelBuilder itemModelBuilder = this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/generated"));
        for (int i = 0; i < textures.length; i++) {
            itemModelBuilder.texture("layer" + i, textures[i]);
        }
        return itemModelBuilder;
    }

    public ItemModelBuilder handheldItem(ResourceLocation item, ResourceLocation[] textures) {
        ItemModelBuilder itemModelBuilder = this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/handheld"));
        for (int i = 0; i < textures.length; i++) {
            itemModelBuilder.texture("layer" + i, textures[i]);
        }
        return itemModelBuilder;
    }

    public ItemModelBuilder spawnEggItem(Item item) {
        return this.spawnEggItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    public ItemModelBuilder spawnEggItem(ResourceLocation item) {
        return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }

    public void basicBlock(Block block) {
        this.getBuilder(BuiltInRegistries.BLOCK.getKey(block).getPath()).parent(new ModelFile.UncheckedModelFile(this.modLoc("block/" + BuiltInRegistries.BLOCK.getKey(block).getPath())));
    }
}
