package ca.willatendo.simplelibrary.data;

import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import net.minecraft.DetectedVersion;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = SimpleCoreUtils.ID)
public final class SimpleLibraryData {
    @SubscribeEvent
    public static void gatherDataEvent(GatherDataEvent.Client event) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();

        event.addProvider(new PackMetadataGenerator(packOutput).add(PackMetadataSection.SERVER_TYPE, new PackMetadataSection(Component.literal("SimpleLibrary resources."), new InclusiveRange<>(DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA)))));
    }
}
