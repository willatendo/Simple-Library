package willatendo.simplelibrary.client;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ChestRaftModel;
import net.minecraft.client.model.RaftModel;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import willatendo.simplelibrary.client.event.registry.ModelLayerRegistry;
import willatendo.simplelibrary.client.event.registry.ModelRegistry;
import willatendo.simplelibrary.client.render.SimpleBoatRenderer;
import willatendo.simplelibrary.server.SimpleBuiltInRegistries;
import willatendo.simplelibrary.server.block.entity.SimpleBlockEntityTypes;
import willatendo.simplelibrary.server.entity.SimpleEntityTypes;
import willatendo.simplelibrary.server.entity.variant.BoatType;

public class SimpleLibraryClient {
    public static void init() {
        ClientConfigRegister.registerAll();
    }

    public static void modelEvent(ModelRegistry modelRegistry) {
        modelRegistry.register(SimpleEntityTypes.SIMPLE_BOAT.get(), context -> new SimpleBoatRenderer(context, false));
        modelRegistry.register(SimpleEntityTypes.SIMPLE_CHEST_BOAT.get(), context -> new SimpleBoatRenderer(context, true));

        modelRegistry.register(SimpleBlockEntityTypes.SIMPLE_SIGN.get(), SignRenderer::new);
    }

    public static void modelLayerEvent(ModelLayerRegistry modelLayerRegistry) {
        for (BoatType boatType : SimpleBuiltInRegistries.BOAT_TYPES) {
            modelLayerRegistry.register(SimpleModelLayers.createBoatModelName(boatType), boatType.raft() ? RaftModel::createBodyModel : BoatModel::createBodyModel);
            modelLayerRegistry.register(SimpleModelLayers.createChestBoatModelName(boatType), boatType.raft() ? ChestRaftModel::createBodyModel : ChestBoatModel::createBodyModel);
        }
    }
}
