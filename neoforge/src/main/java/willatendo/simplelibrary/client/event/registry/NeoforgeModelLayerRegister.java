package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import willatendo.simplelibrary.client.LayerDefinitionProvider;

public final class NeoforgeModelLayerRegister implements ModelLayerRegistry {
    private final EntityRenderersEvent.RegisterLayerDefinitions event;

    public NeoforgeModelLayerRegister(EntityRenderersEvent.RegisterLayerDefinitions event) {
        this.event = event;
    }

    @Override
    public void register(ModelLayerLocation modelLayerLocation, LayerDefinitionProvider layerDefinitionProvider) {
        this.event.registerLayerDefinition(modelLayerLocation, layerDefinitionProvider::layerDefinition);
    }
}
