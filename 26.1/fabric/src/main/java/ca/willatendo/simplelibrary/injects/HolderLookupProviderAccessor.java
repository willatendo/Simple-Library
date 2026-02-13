package ca.willatendo.simplelibrary.injects;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

public interface HolderLookupProviderAccessor {
    default <T> Optional<Holder.Reference<T>> holder(ResourceKey<T> key) {
        Optional<? extends HolderLookup.RegistryLookup<T>> registry = ((HolderLookup.Provider) (Object) this).lookup(key.registryKey());
        return registry.flatMap(registryLookup -> registryLookup.get(key));
    }
}
