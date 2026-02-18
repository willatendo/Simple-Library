package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.injects.HolderExtension;
import ca.willatendo.simplelibrary.server.data_maps.DataMapType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Holder.Reference.class)
public abstract class HolderReferenceMixin<T> implements HolderExtension<T> {
    @Shadow
    @Final
    private HolderOwner<T> owner;

    @Shadow
    public abstract ResourceKey<T> key();

    public <A> A getData(DataMapType<T, A> type) {
        HolderOwner<T> holderOwner = this.owner;
        if (holderOwner instanceof HolderLookup.RegistryLookup<T> lookup) {
            return (A) lookup.getData(type, this.key());
        } else {
            return null;
        }
    }
}
