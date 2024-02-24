package willatendo.simplelibrary.server.flag;

import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class FlagItemProperties extends Item.Properties {
	public Properties requiredFeatures(FeatureFlagRegistry featureFlagRegistry, FeatureFlag... featureFlags) {
		this.requiredFeatures = featureFlagRegistry.subset(featureFlags);
		return this;
	}
}
