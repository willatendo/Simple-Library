package willatendo.simplelibrary.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
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

    private ResourceLocation get(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    private ResourceLocation get(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public ItemModelBuilder handheldItem(Item item) {
        return this.handheldItem(Objects.requireNonNull(this.get(item)), ResourceLocation.fromNamespaceAndPath(this.get(item).getNamespace(), "item/" + this.get(item).getPath()));
    }

    public ItemModelBuilder handheldItem(Item item, ResourceLocation texture) {
        return this.handheldItem(Objects.requireNonNull(this.get(item)), texture);
    }

    public ItemModelBuilder basicItem(Item item, ResourceLocation texture) {
        return this.basicItem(Objects.requireNonNull(this.get(item)), texture);
    }

    public ItemModelBuilder basicItem(ResourceLocation item, ResourceLocation texture) {
        return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", texture);
    }

    public ItemModelBuilder handheldItem(ResourceLocation item, ResourceLocation texture) {
        return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/handheld")).texture("layer0", texture);
    }

    public ItemModelBuilder handheldItem(Item item, ResourceLocation[] textures) {
        return this.handheldItem(Objects.requireNonNull(this.get(item)), textures);
    }

    public ItemModelBuilder basicItem(Item item, ResourceLocation[] textures) {
        return this.basicItem(Objects.requireNonNull(this.get(item)), textures);
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
        return this.spawnEggItem(Objects.requireNonNull(this.get(item)));
    }

    public ItemModelBuilder spawnEggItem(ResourceLocation item) {
        return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }

    public void basicBlock(Block block) {
        this.getBuilder(this.get(block).getPath()).parent(new ModelFile.UncheckedModelFile(this.modLoc("block/" + this.get(block).getPath())));
    }

    public void basicBlock(Block block, ResourceLocation blockModel) {
        this.getBuilder(this.get(block).getPath()).parent(new ModelFile.UncheckedModelFile(blockModel));
    }

    public void entityRendererItem(Item item, ResourceLocation particle) {
        this.getBuilder(this.get(item).getPath()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", particle).transforms().transform(ItemDisplayContext.GUI).rotation(30.0F, 135.0F, 0.0F).translation(0.0F, 0.0F, 0.0F).scale(0.625F, 0.625F, 0.625F).end().transform(ItemDisplayContext.GROUND).rotation(0.0F, 0.0F, 0.0F).translation(0.0F, 3.0F, 0.0F).scale(0.25F, 0.25F, 0.25F).end().transform(ItemDisplayContext.HEAD).rotation(0.0F, 180.0F, 0.0F).translation(0.0F, 0.0F, 0.0F).scale(1.0F, 1.0F, 1.0F).end().transform(ItemDisplayContext.FIXED).rotation(0.0F, 0.0F, 0.0F).translation(0.0F, 3.0F, 0.0F).scale(0.5F, 0.5F, 0.5F).end().transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(75.0F, 135.0F, 0.0F).translation(0.0F, 2.5F, 0.0F).scale(0.375F, 0.375F, 0.375F).end().transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0.0F, 135.0F, 0.0F).translation(0.0F, 0.0F, 0.0F).scale(0.4F, 0.4F, 0.4F).end();
    }

    public void armorItem(ArmorItem armorItem) {
        ItemModelBuilder itemModelBuilder = this.basicItem(armorItem);
        ItemModelGenerators.GENERATED_TRIM_MODELS.forEach(trimModelData -> this.addTrim(itemModelBuilder, this.get(armorItem), armorItem, trimModelData));
    }

    private void addTrim(ItemModelBuilder itemModelBuilder, ResourceLocation id, ArmorItem armorItem, ItemModelGenerators.TrimModelData trimModelData) {
        String trimName = trimModelData.name(armorItem.getMaterial());
        ResourceLocation s = ResourceLocation.withDefaultNamespace(armorItem.getType().getName() + "_trim_" + trimName).withPrefix("trims/items/");
        itemModelBuilder.override().model(this.withExistingParent(id.getPath() + "_" + trimName + "_trim", this.mcLoc("generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + id.getPath())).texture("layer1", s)).predicate(this.mcLoc("trim_type"), trimModelData.itemModelIndex()).end();
    }
}
