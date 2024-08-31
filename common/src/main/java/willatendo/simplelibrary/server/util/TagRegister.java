package willatendo.simplelibrary.server.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class TagRegister<T> {
    private final ResourceKey<? extends Registry<T>> resourceKey;
    private final String modId;

    public static <T> TagRegister<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        return new TagRegister<>(resourceKey, modId);
    }

    public TagRegister(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        this.resourceKey = resourceKey;
        this.modId = modId;
    }

    public TagKey<T> register(String id) {
        return TagKey.create(this.resourceKey, ResourceLocation.fromNamespaceAndPath(this.modId, id));
    }
}
