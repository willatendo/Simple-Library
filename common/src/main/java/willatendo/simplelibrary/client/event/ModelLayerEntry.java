package willatendo.simplelibrary.client.event;

import net.minecraft.client.model.geom.ModelLayerLocation;

public record ModelLayerEntry(ModelLayerLocation modelLayerLocation, TexturedModelDataProvider texturedModelDataProvider) {
}
