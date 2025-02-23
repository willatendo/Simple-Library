package willatendo.simplelibrary.data.model;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.function.BiConsumer;

public abstract class SimpleItemModelGenerator {
    protected final ItemModelGenerators itemModelGenerators;
    protected final ItemModelOutput itemModelOutput;
    protected final BiConsumer<ResourceLocation, ModelInstance> modelOutput;

    public SimpleItemModelGenerator(ItemModelGenerators itemModelGenerators) {
        this.itemModelGenerators = itemModelGenerators;
        this.itemModelOutput = itemModelGenerators.itemModelOutput;
        this.modelOutput = itemModelGenerators.modelOutput;
    }

    public abstract void run();

    // Basic Providers
    protected void generatedItem(Item item) {
        this.itemModelGenerators.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    protected void generatedItem(Item item, ResourceLocation texture) {
        this.item(item, ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(item, new TextureMapping().put(TextureSlot.LAYER0, texture), this.modelOutput)));
    }

    protected void handheldItem(Item item) {
        this.itemModelGenerators.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
    }

    protected void handheldItem(Item item, ResourceLocation texture) {
        this.item(item, ItemModelUtils.plainModel(ModelTemplates.FLAT_HANDHELD_ITEM.create(item, new TextureMapping().put(TextureSlot.LAYER0, texture), this.modelOutput)));
    }

    protected void spawnEggItem(Item item, int primaryColor, int secondaryColor) {
        this.itemModelGenerators.generateSpawnEgg(item, primaryColor, secondaryColor);
    }

    protected void helmetItem(Item item, ResourceKey<EquipmentAsset> equipmentAsset, boolean dyeable) {
        this.armorItem(item, equipmentAsset, ArmorType.HELMET, dyeable);
    }

    protected void chestplateItem(Item item, ResourceKey<EquipmentAsset> equipmentAsset, boolean dyeable) {
        this.armorItem(item, equipmentAsset, ArmorType.CHESTPLATE, dyeable);
    }

    protected void leggingsItem(Item item, ResourceKey<EquipmentAsset> equipmentAsset, boolean dyeable) {
        this.armorItem(item, equipmentAsset, ArmorType.LEGGINGS, dyeable);
    }

    protected void bootsItem(Item item, ResourceKey<EquipmentAsset> equipmentAsset, boolean dyeable) {
        this.armorItem(item, equipmentAsset, ArmorType.BOOTS, dyeable);
    }

    protected void armorItem(Item item, ResourceKey<EquipmentAsset> equipmentAsset, ArmorType armorType, boolean dyeable) {
        this.itemModelGenerators.generateTrimmableItem(item, equipmentAsset, armorType.getName(), dyeable);
    }

    protected void item(Item item, ResourceLocation model) {
        this.item(item, ItemModelUtils.plainModel(model));
    }

    protected void item(Item item, ItemModel.Unbaked model) {
        this.itemModelOutput.accept(item, model);
    }

    protected enum ArmorType {
        HELMET("helmet"),
        CHESTPLATE("chestplate"),
        LEGGINGS("leggings"),
        BOOTS("boots");

        private final String name;

        ArmorType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
