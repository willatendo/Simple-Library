package ca.willatendo.simplelibrary.data;

import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import ca.willatendo.simplelibrary.data.providers.SimplePackMetadataProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.DetectedVersion;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;

public final class FabricSimpleLibraryData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider((fabricPackOutput, registries) -> new SimplePackMetadataProvider(fabricPackOutput, SimpleCoreUtils.ID).add(PackMetadataSection.SERVER_TYPE, new PackMetadataSection(Component.literal("SimpleLibrary resources."), new InclusiveRange<>(DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA)))));
    }
}
