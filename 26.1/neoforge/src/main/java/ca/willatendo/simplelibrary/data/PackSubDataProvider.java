package ca.willatendo.simplelibrary.data;

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

    public static PackSubDataProvider createResourcePack(String modId, String packName, DataGenerator dataGenerator, Component packDisplayName, Consumer<SimplePackMetadataGenerator> consumer) {
        return new PackSubDataProvider(modId, packName, dataGenerator, "resourcepacks", simplePackMetadataGenerator -> {
            simplePackMetadataGenerator.add(PackMetadataSection.CLIENT_TYPE, new PackMetadataSection(packDisplayName, new InclusiveRange<>(DetectedVersion.BUILT_IN.packVersion(PackType.CLIENT_RESOURCES))));
            consumer.accept(simplePackMetadataGenerator);
        });
    }

    public static PackSubDataProvider createDataPack(String modId, String packName, DataGenerator dataGenerator, Component packDisplayName, Consumer<SimplePackMetadataGenerator> consumer) {
        return new PackSubDataProvider(modId, packName, dataGenerator, "data/" + modId + "/datapacks", simplePackMetadataGenerator -> {
            simplePackMetadataGenerator.add(PackMetadataSection.SERVER_TYPE, new PackMetadataSection(packDisplayName, new InclusiveRange<>(DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA))));
            consumer.accept(simplePackMetadataGenerator);
        });
    }

    public static PackSubDataProvider createFeaturePack(String modId, String packName, DataGenerator dataGenerator, Component packDisplayName, FeatureFlagSet featureFlags, Consumer<SimplePackMetadataGenerator> consumer) {
        return PackSubDataProvider.createDataPack(modId, packName, dataGenerator, packDisplayName, simplePackMetadataGenerator -> {
            simplePackMetadataGenerator.add(FeatureFlagsMetadataSection.TYPE, new FeatureFlagsMetadataSection(featureFlags));
            consumer.accept(simplePackMetadataGenerator);
        });
    }

    private PackSubDataProvider(String modId, String packName, DataGenerator dataGenerator, String packType, Consumer<SimplePackMetadataGenerator> consumer) {
        this.modId = modId;
        this.packName = packName;
        this.dataGenerator = dataGenerator;
        this.packOutput = new PackOutput(dataGenerator.getPackOutput().getOutputFolder().resolve(packType).resolve(packName));
        SimplePackMetadataGenerator metadataGenerator = new SimplePackMetadataGenerator(this.packOutput, modId, Optional.of(packName));
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
