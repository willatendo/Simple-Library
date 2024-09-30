package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.model.geom.ModelLayerLocation;
import willatendo.simplelibrary.client.LayerDefinitionProvider;

public interface ModelLayerRegistry {
    void register(ModelLayerLocation modelLayerLocation, LayerDefinitionProvider layerDefinitionProvider);
}
