package ca.willatendo.simplelibrary.injects;

import ca.willatendo.simplelibrary.server.data_maps.DataMapType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public interface HolderExtension<T> {
    default @Nullable ResourceKey<T> getKey() {
        return (ResourceKey) ((Holder) this).unwrapKey().orElse((Object) null);
    }

    default <R> R getData(DataMapType<T, R> type) {
        throw new RuntimeException("This has not been registered correctly! " + this.getClass());
    }
}
