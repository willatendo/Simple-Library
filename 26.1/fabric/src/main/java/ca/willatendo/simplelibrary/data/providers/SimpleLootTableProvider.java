package ca.willatendo.simplelibrary.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class SimpleLootTableProvider extends LootTableProvider {
    private final String modId;

    public SimpleLootTableProvider(PackOutput packOutput, String modId, CompletableFuture<HolderLookup.Provider> registries, SubProviderEntry... subProviderEntries) {
        super(packOutput, Set.of(), List.of(subProviderEntries), registries);
        this.modId = modId;
    }

    @Override
    public String getName() {
        return "SimpleLibrary: Recipe Provider for " + this.modId;
    }
}