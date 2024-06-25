package willatendo.simplelibrary.client.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public final class NeoForgeClientEvents {
    private final ClientEventsHolder clientEventsHolder;

    public NeoForgeClientEvents(ClientEventsHolder clientEventsHolder) {
        this.clientEventsHolder = clientEventsHolder;
    }

    @SubscribeEvent
    public void screens(RegisterMenuScreensEvent event) {
        this.clientEventsHolder.menuScreens.forEach(menuScreenEntry -> {
            event.register(menuScreenEntry.menuType(), menuScreenEntry.screenConstructor());
        });
    }

    @SubscribeEvent
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        this.clientEventsHolder.entityModels.forEach(entityModelEntry -> {
            event.registerEntityRenderer(entityModelEntry.entityType(), entityModelEntry.entityRendererProvider());
        });
        this.clientEventsHolder.blockModels.forEach(blockModelEntry -> {
            event.registerBlockEntityRenderer(blockModelEntry.blockEntityType(), blockModelEntry.blockEntityRendererProvider());
        });
    }

    @SubscribeEvent
    public void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        this.clientEventsHolder.modelLayers.forEach(modelLayer -> {
            event.registerLayerDefinition(modelLayer.modelLayerLocation(), modelLayer.texturedModelDataProvider()::createModelData);
        });
    }


    @SubscribeEvent
    public void registerModelLayers(RegisterKeyMappingsEvent event) {
        this.clientEventsHolder.keyMappings.forEach(keyMapping -> {
            event.register(keyMapping);
        });
    }
}
