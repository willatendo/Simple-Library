package ca.willatendo.simplelibrary.data.providers;

import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class SimpleEquipmentAssetProvider implements DataProvider {
    private final Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> equipment = new HashMap<>();
    private final PackOutput packOutput;
    private final String modId;

    public SimpleEquipmentAssetProvider(PackOutput packOutput, String modId) {
        this.packOutput = packOutput;
        this.modId = modId;
    }

    public abstract void registerModels();

    public void humanoidOnly(ResourceKey<EquipmentAsset> resourceKey, String name) {
        this.equipment.put(resourceKey, EquipmentClientInfo.builder().addHumanoidLayers(CoreUtils.resource(this.modId, name)).build());
    }

    public void humanoidAndHorse(ResourceKey<EquipmentAsset> resourceKey, String name) {
        this.equipment.put(resourceKey, EquipmentClientInfo.builder().addHumanoidLayers(CoreUtils.resource(this.modId, name)).addLayers(EquipmentClientInfo.LayerType.HORSE_BODY, EquipmentClientInfo.Layer.leatherDyeable(CoreUtils.resource(this.modId, name), false)).build());
    }

    @Override
    public CompletableFuture<?> run(final CachedOutput cache) {
        this.registerModels();
        PackOutput.PathProvider pathProvider = this.packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
        Objects.requireNonNull(pathProvider);
        return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, pathProvider::json, this.equipment);
    }

    @Override
    public String getName() {
        return "SimpleLibrary: Equipment Asset Provider for " + this.modId;
    }
}
