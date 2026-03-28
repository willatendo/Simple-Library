package ca.willatendo.simplelibrary.data;

import ca.willatendo.simplelibrary.data.providers.SimplePackMetadataProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.DetectedVersion;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FeatureFlagsMetadataSection;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.flag.FeatureFlagSet;

import java.util.Optional;
import java.util.function.Consumer;

public final class PackSubDataProvider {
    private final String modId;
    private final String packName;
    private final DataGenerator dataGenerator;
    private final PackOutput packOutput;

    public static PackSubDataProvider createResourcePack(String modId, String packName, DataGenerator dataGenerator, FabricDataGenerator.Pack pack, Component packDisplayName, Consumer<SimplePackMetadataProvider> consumer) {
        return new PackSubDataProvider(modId, packName, dataGenerator, pack, "resourcepacks", SimplePackMetadataProvider -> {
            SimplePackMetadataProvider.add(PackMetadataSection.CLIENT_TYPE, new PackMetadataSection(packDisplayName, new InclusiveRange<>(DetectedVersion.BUILT_IN.packVersion(PackType.CLIENT_RESOURCES))));
            consumer.accept(SimplePackMetadataProvider);
        });
    }

    public static PackSubDataProvider createDataPack(String modId, String packName, DataGenerator dataGenerator, FabricDataGenerator.Pack pack, Component packDisplayName, Consumer<SimplePackMetadataProvider> consumer) {
        return new PackSubDataProvider(modId, packName, dataGenerator, pack, "data/" + modId + "/datapacks", SimplePackMetadataProvider -> {
            SimplePackMetadataProvider.add(PackMetadataSection.SERVER_TYPE, new PackMetadataSection(packDisplayName, new InclusiveRange<>(DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA))));
            consumer.accept(SimplePackMetadataProvider);
        });
    }

    public static PackSubDataProvider createFeaturePack(String modId, String packName, DataGenerator dataGenerator, FabricDataGenerator.Pack pack, Component packDisplayName, FeatureFlagSet featureFlags, Consumer<SimplePackMetadataProvider> consumer) {
        return PackSubDataProvider.createDataPack(modId, packName, dataGenerator, pack, packDisplayName, SimplePackMetadataProvider -> {
            SimplePackMetadataProvider.add(FeatureFlagsMetadataSection.TYPE, new FeatureFlagsMetadataSection(featureFlags));
            consumer.accept(SimplePackMetadataProvider);
        });
    }

    private PackSubDataProvider(String modId, String packName, DataGenerator dataGenerator, FabricDataGenerator.Pack pack, String packType, Consumer<SimplePackMetadataProvider> consumer) {
        this.modId = modId;
        this.packName = packName;
        this.dataGenerator = dataGenerator;
        this.packOutput = new PackOutput(pack.output.getOutputFolder().resolve(packType).resolve(packName));
        SimplePackMetadataProvider metadataGenerator = new SimplePackMetadataProvider(this.packOutput, modId, Optional.of(packName));
        consumer.accept(metadataGenerator);
        this.addProvider(true, metadataGenerator);
    }

    public <T extends DataProvider> void addProvider(boolean run, T dataProvider) {
        String name = dataProvider.getName() + " FOR " + this.modId + "/" + this.packName;
        if (!this.dataGenerator.allProviderIds.add(name)) {
            throw new IllegalStateException("Duplicate provider: " + name);
        } else {
            if (run) {
                this.dataGenerator.providersToRun.put(name, dataProvider);
            }
        }
    }
}
