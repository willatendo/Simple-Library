package ca.willatendo.simplelibrary.injects;

import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlag;

public interface FeatureFlagRegistryExtension {
    default FeatureFlag getFlag(Identifier name) {
        throw new RuntimeException("This has not been registered correctly! " + this.getClass());
    }
}
