package willatendo.simplelibrary.client.event;

import net.minecraft.client.model.geom.builders.LayerDefinition;

@FunctionalInterface
public interface TexturedModelDataProvider {
	LayerDefinition createModelData();
}
