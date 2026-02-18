package ca.willatendo.simplelibrary.server.utils;

import ca.willatendo.simplelibrary.platform.SimpleLibraryPlatformHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlag;

public final class FeatureFlagUtils {
    public static FeatureFlag getFeatureFlag(Identifier identifier) {
        return SimpleLibraryPlatformHelper.INSTANCE.getFeatureFlag(identifier);
    }
}
