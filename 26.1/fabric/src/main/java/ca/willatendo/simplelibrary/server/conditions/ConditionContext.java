package ca.willatendo.simplelibrary.server.conditions;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class ConditionContext implements ICondition.IContext {
    private final Map<ResourceKey<? extends Registry<?>>, HolderLookup.RegistryLookup<?>> pendingTags = new IdentityHashMap();
    private final FeatureFlagSet enabledFeatures;
    private final RegistryAccess registryAccess;

    public ConditionContext(List<Registry.PendingTags<?>> pendingTags, RegistryAccess registryAccess, FeatureFlagSet enabledFeatures) {
        this.registryAccess = registryAccess;
        this.enabledFeatures = enabledFeatures;

        for (Registry.PendingTags<?> tags : pendingTags) {
            this.pendingTags.put(tags.key(), tags.lookup());
        }
    }

    public void clear() {
        this.pendingTags.clear();
    }

    public <T> boolean isTagLoaded(TagKey<T> key) {
        HolderLookup.RegistryLookup<?> lookup = this.pendingTags.get(key.registry());
        return lookup != null && lookup.get((TagKey) key).isPresent();
    }

    public RegistryAccess registryAccess() {
        return this.registryAccess;
    }

    public FeatureFlagSet enabledFeatures() {
        return this.enabledFeatures;
    }
}
