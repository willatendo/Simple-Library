package ca.willatendo.simplelibrary.data;

import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class SimpleEquipmentAssetProvider extends EquipmentAssetProvider {
    private final Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> equipment = new HashMap<>();
    private final String modId;

    public SimpleEquipmentAssetProvider(PackOutput packOutput, String modId) {
        super(packOutput);
        this.modId = modId;
    }

    public abstract void registerModels();

    @Override
    protected void registerModels(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        this.registerModels();
        this.equipment.forEach(output);
    }

    public void humanoidOnly(ResourceKey<EquipmentAsset> resourceKey, String name) {
        this.equipment.put(resourceKey, EquipmentClientInfo.builder().addHumanoidLayers(CoreUtils.resource(this.modId, name)).build());
    }

    public void humanoidAndHorse(ResourceKey<EquipmentAsset> resourceKey, String name) {
        this.equipment.put(resourceKey, EquipmentClientInfo.builder().addHumanoidLayers(CoreUtils.resource(this.modId, name)).addLayers(EquipmentClientInfo.LayerType.HORSE_BODY, EquipmentClientInfo.Layer.leatherDyeable(CoreUtils.resource(this.modId, name), false)).build());
    }
}
