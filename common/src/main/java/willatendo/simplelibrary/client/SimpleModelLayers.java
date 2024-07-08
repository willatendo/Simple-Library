package willatendo.simplelibrary.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import willatendo.simplelibrary.server.entity.variant.BoatType;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class SimpleModelLayers {
    public static ModelLayerLocation createBoatModelName(BoatType boatType) {
        return new ModelLayerLocation(SimpleUtils.simple("boat/" + boatType.name()), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(BoatType boatType) {
        return new ModelLayerLocation(SimpleUtils.simple("chest_boat/" + boatType.name()), "main");
    }
}
