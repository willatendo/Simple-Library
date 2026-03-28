package ca.willatendo.simplelibrary.data.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

public abstract class SimpleBiomeTagsProvider extends SimpleKeyTagProvider<Biome> {
    public SimpleBiomeTagsProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, modId, Registries.BIOME, lookupProvider);
    }
}
