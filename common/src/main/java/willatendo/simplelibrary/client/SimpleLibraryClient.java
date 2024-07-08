package willatendo.simplelibrary.client;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ChestRaftModel;
import net.minecraft.client.model.RaftModel;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import willatendo.simplelibrary.client.event.ModelLayerRegister;
import willatendo.simplelibrary.client.event.ModelRegister;
import willatendo.simplelibrary.client.render.SimpleBoatRenderer;
import willatendo.simplelibrary.server.SimpleBuiltInRegistries;
import willatendo.simplelibrary.server.block.entity.SimpleBlockEntityTypes;
import willatendo.simplelibrary.server.entity.SimpleEntityTypes;
import willatendo.simplelibrary.server.entity.variant.BoatType;

public class SimpleLibraryClient {
    public static void modelEvent(ModelRegister modelRegister) {
        modelRegister.register(SimpleEntityTypes.SIMPLE_BOAT.get(), context -> new SimpleBoatRenderer(context, true));
        modelRegister.register(SimpleEntityTypes.SIMPLE_CHEST_BOAT.get(), context -> new SimpleBoatRenderer(context, true));

        modelRegister.register(SimpleBlockEntityTypes.SIMPLE_SIGN.get(), SignRenderer::new);
    }

    public static void modelLayerEvent(ModelLayerRegister modelLayerRegister) {
        for (BoatType boatType : SimpleBuiltInRegistries.BOAT_TYPES) {
            modelLayerRegister.register(SimpleModelLayers.createBoatModelName(boatType), boatType.raft() ? RaftModel::createBodyModel : BoatModel::createBodyModel);
            modelLayerRegister.register(SimpleModelLayers.createChestBoatModelName(boatType), boatType.raft() ? ChestRaftModel::createBodyModel : ChestBoatModel::createBodyModel);
        }
    }
}
