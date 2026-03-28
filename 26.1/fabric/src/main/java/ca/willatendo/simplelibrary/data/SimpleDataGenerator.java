package ca.willatendo.simplelibrary.data;

import ca.willatendo.simplelibrary.data.providers.SimpleDatapackBuiltinEntriesProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public final class SimpleDataGenerator {
    private final List<BiFunction<PackOutput, String, ? extends DataProvider>> resources = Lists.newArrayList();
    private final List<DataProviderSupplier<? extends DataProvider>> data = Lists.newArrayList();
    private final FabricDataGenerator fabricDataGenerator;
    private final String modId;
    private final Component displayName;
    private RegistrySetBuilder registrySetBuilder;

    public SimpleDataGenerator(FabricDataGenerator fabricDataGenerator, String modId, Component displayName) {
        this.fabricDataGenerator = fabricDataGenerator;
        this.modId = modId;
        this.displayName = displayName;
    }

    public <T extends DataProvider> void addResource(BiFunction<PackOutput, String, T> supplier) {
        this.resources.add(supplier);
    }

    public void addRegistries(RegistrySetBuilder registrySetBuilder) {
        this.registrySetBuilder = registrySetBuilder;
    }

    public <T extends DataProvider> void addData(DataProviderSupplier<T> supplier) {
        this.data.add(supplier);
    }

    public void generate() {
        FabricDataGenerator.Pack pack = this.fabricDataGenerator.createPack();

        pack.addProvider((fabricPackOutput, registries) -> new PackMetadataGenerator(fabricPackOutput).add(PackMetadataSection.SERVER_TYPE, new PackMetadataSection(this.displayName, new InclusiveRange<>(DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA)))));
        this.resources.forEach(provider -> pack.addProvider((packOutput, registries) -> provider.apply(packOutput, this.modId)));

        CompletableFuture<HolderLookup.Provider> allRegistries = this.registrySetBuilder != null ? pack.addProvider((fabricPackOutput, registries) -> new SimpleDatapackBuiltinEntriesProvider(fabricPackOutput, this.modId, registries, this.registrySetBuilder, Set.of(this.modId))).getRegistryProvider() : null;
        this.data.forEach(dataProviderSupplier -> {
            pack.addProvider((fabricPackOutput, registries) -> dataProviderSupplier.create(fabricPackOutput, this.modId, allRegistries != null ? allRegistries : registries));
        });
    }

    public interface DataProviderSupplier<T extends DataProvider> {
        T create(PackOutput packOutput, String modId, CompletableFuture<HolderLookup.Provider> registries);
    }
}
