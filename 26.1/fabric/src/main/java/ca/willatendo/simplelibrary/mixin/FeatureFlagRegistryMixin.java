package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.injects.FeatureFlagRegistryExtension;
import com.google.common.base.Preconditions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(FeatureFlagRegistry.class)
public class FeatureFlagRegistryMixin implements FeatureFlagRegistryExtension {
    @Shadow
    @Final
    private Map<Identifier, FeatureFlag> names;

    @Override
    public FeatureFlag getFlag(Identifier identifier) {
        return Preconditions.checkNotNull(this.names.get(identifier), "Flag %s was not registered", identifier);
    }
}
