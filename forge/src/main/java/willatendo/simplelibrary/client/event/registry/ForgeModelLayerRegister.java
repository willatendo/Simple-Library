package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import willatendo.simplelibrary.client.LayerDefinitionProvider;

public final class ForgeModelLayerRegister implements ModelLayerRegistry {
    private final EntityRenderersEvent.RegisterLayerDefinitions event;

    public ForgeModelLayerRegister(EntityRenderersEvent.RegisterLayerDefinitions event) {
        this.event = event;
    }

    @Override
    public void register(ModelLayerLocation modelLayerLocation, LayerDefinitionProvider layerDefinitionProvider) {
        this.event.registerLayerDefinition(modelLayerLocation, layerDefinitionProvider::layerDefinition);
    }
}
