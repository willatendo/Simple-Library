package willatendo.simplelibrary.client.event;

import net.minecraft.client.model.geom.ModelLayerLocation;
import willatendo.simplelibrary.client.LayerDefinitionProvider;

public interface ModelLayerRegister {
    void register(ModelLayerLocation modelLayerLocation, LayerDefinitionProvider layerDefinitionProvider);
}
