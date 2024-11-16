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

    public void basicBlock(Block block, String blockModel) {
        this.getBuilder(BuiltInRegistries.BLOCK.getKey(block).getPath()).parent(new ModelFile.UncheckedModelFile(this.modLoc("block/" + blockModel)));
    }

    public void armorItem(Item item, String type) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        String name = id.getPath();
        this.basicItem(item).override().model(this.withExistingParent(name + "_quartz_trim", this.mcLoc("generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + name)).texture("layer1", this.mcLoc("trims/items/" + type + "_trim_quartz"))).predicate(this.mcLoc("trim_type"), 0.1F).end().override().model(this.withExistingParent(name + "_iron_darker_trim", this.mcLoc("generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + name)).texture("layer1", this.mcLoc("trims/items/" + type + "_trim_iron_darker"))).predicate(this.mcLoc("trim_type"), 0.2F).end().override().model(this.withExistingParent(name + "_netherite_trim", this.mcLoc("generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + name)).texture("layer1", this.mcLoc("trims/items/" + type + "_trim_netherite"))).predicate(this.mcLoc("trim_type"), 0.3F).end().override().model(this.withExistingParent(name + "_redstone_trim", this.mcLoc("generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + name)).texture("layer1", this.mcLoc("trims/items/" + type + "_trim_redstone"))).predicate(this.mcLoc("trim_type"), 0.4F).end().override().model(this.withExistingParent(name + "_copper_trim", this.mcLoc("generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + name)).texture("layer1", this.mcLoc("trims/items/" + type + "_trim_copper"))).predicate(this.mcLoc("trim_type"), 0.5F).end().override().model(this.withExistingParent(name + "_gold_trim", this.mcLoc("generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + name)).texture("layer1", this.mcLoc("trims/items/" + type + "_trim_gold"))).predicate(this.mcLoc("trim_type"), 0.6F).end().override().model(this.withExistingParent(name + "_emerald_trim", this.mcLoc("generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + name)).texture("layer1", this.mcLoc("trims/items/" + type + "_trim_emerald"))).predicate(this.mcLoc("trim_type"), 0.7F).end().override().model(this.withExistingParent(name + "_diamond_trim", this.mcLoc("generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + name)).texture("layer1", this.mcLoc("trims/items/" + type + "_trim_diamond"))).predicate(this.mcLoc("trim_type"), 0.8F).end().override().model(this.withExistingParent(name + "_lapis_trim", this.mcLoc("generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + name)).texture("layer1", this.mcLoc("trims/items/" + type + "_trim_lapis"))).predicate(this.mcLoc("trim_type"), 0.9F).end().override().model(this.withExistingParent(name + "_amethyst_trim", this.mcLoc("generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + name)).texture("layer1", this.mcLoc("trims/items/" + type + "_trim_amethyst"))).predicate(this.mcLoc("trim_type"), 1.0F).end();
    }
}
