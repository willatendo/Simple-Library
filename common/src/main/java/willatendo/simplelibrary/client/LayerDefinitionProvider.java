package willatendo.simplelibrary.client;

import net.minecraft.client.model.geom.builders.LayerDefinition;

@FunctionalInterface
public interface LayerDefinitionProvider {
    LayerDefinition layerDefinition();
}
