package willatendo.simplelibrary.data;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class SimpleAdvancementSubProvider implements AdvancementSubProvider {
    private final CompletableFuture<HolderLookup.Provider> registries;

    public SimpleAdvancementSubProvider(CompletableFuture<HolderLookup.Provider> registries) {
        this.registries = registries;
    }

    public abstract void generateAdvancements(HolderLookup.Provider provider, Consumer<AdvancementHolder> advancementHolderConsumer);

    @Override
    public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer) {
        this.registries.thenAccept(realProvider -> this.generateAdvancements(realProvider, consumer));
    }
}
