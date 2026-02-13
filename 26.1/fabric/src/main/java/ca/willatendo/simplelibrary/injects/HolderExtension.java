package ca.willatendo.simplelibrary.injects;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public interface HolderExtension<T> {
    default @Nullable ResourceKey<T> getKey() {
        return (ResourceKey) ((Holder) this).unwrapKey().orElse((Object) null);
    }
}
