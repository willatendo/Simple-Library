package ca.willatendo.simplelibrary.data.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public abstract class SimpleItemTagsProvider extends SimpleIntrinsicHolderTagsProvider<Item> {
    public SimpleItemTagsProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, modId, Registries.ITEM, lookupProvider, item -> item.builtInRegistryHolder().key());
    }
}
