package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.injects.RegistryLookupExtension;
import ca.willatendo.simplelibrary.server.data_maps.DataMapType;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HolderLookup.RegistryLookup.Delegate.class)
public interface HolderLookupRegistryLookupDelegateMixin<T> extends RegistryLookupExtension<T> {
    @Shadow
    HolderLookup.RegistryLookup<T> parent();

    @Override
    default <A> A getData(DataMapType<T, A> attachment, ResourceKey<T> key) {
        return (A) this.parent().getData(attachment, key);
    }
}
