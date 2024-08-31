package willatendo.simplelibrary.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import willatendo.simplelibrary.client.LayerDefinitionProvider;

public final class FabricModelLayerRegister implements ModelLayerRegister {
    @Override
    public void register(ModelLayerLocation modelLayerLocation, LayerDefinitionProvider layerDefinitionProvider) {
        EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinitionProvider::layerDefinition);
    }
}
