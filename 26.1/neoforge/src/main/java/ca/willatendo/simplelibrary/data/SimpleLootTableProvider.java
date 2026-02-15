package ca.willatendo.simplelibrary.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class SimpleLootTableProvider extends LootTableProvider {
    public SimpleLootTableProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, SubProviderEntry... subProviderEntries) {
        super(packOutput, Set.of(), List.of(subProviderEntries), registries);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableRegistry, ValidationContext validationContext, ProblemReporter.Collector collector) {
        super.validate(writableRegistry, validationContext, collector);
    }
}