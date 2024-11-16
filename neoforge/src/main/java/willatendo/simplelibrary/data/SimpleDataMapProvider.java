package willatendo.simplelibrary.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class SimpleDataMapProvider extends DataMapProvider {
    private final DataMapEntry[] dataMapEntries;

    public SimpleDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, DataMapEntry... dataMapEntries) {
        super(packOutput, registries);
        this.dataMapEntries = dataMapEntries;
    }

    @Override
    protected void gather() {
        for (DataMapEntry dataMapEntry : this.dataMapEntries) {
            dataMapEntry.addAll(this.builder(dataMapEntry.getDataMapType()));
        }
    }
}
