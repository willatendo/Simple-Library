package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.injects.RegistryExtension;
import ca.willatendo.simplelibrary.server.data_maps.DataMapType;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> implements RegistryExtension<T> {
    private final Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> dataMaps = new IdentityHashMap<>();

    @Override
    public Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> getDataMaps() {
        return this.dataMaps;
    }
}
