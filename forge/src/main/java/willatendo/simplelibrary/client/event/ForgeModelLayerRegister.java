package willatendo.simplelibrary.client.event;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import willatendo.simplelibrary.client.LayerDefinitionProvider;

public class ForgeModelLayerRegister implements ModelLayerRegister {
    private final EntityRenderersEvent.RegisterLayerDefinitions event;

    public ForgeModelLayerRegister(EntityRenderersEvent.RegisterLayerDefinitions event) {
        this.event = event;
    }

    @Override
    public void register(ModelLayerLocation modelLayerLocation, LayerDefinitionProvider layerDefinitionProvider) {
        this.event.registerLayerDefinition(modelLayerLocation, layerDefinitionProvider::layerDefinition);
    }
}
