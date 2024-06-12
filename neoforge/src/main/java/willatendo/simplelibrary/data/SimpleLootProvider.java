package willatendo.simplelibrary.data;

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

public class SimpleLootProvider extends LootTableProvider {
    public SimpleLootProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, List<SubProviderEntry> entries) {
        super(packOutput, Set.of(), entries, registries);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableRegistry, ValidationContext validationContext, ProblemReporter.Collector collector) {
    }
}
