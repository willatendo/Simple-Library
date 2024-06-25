package willatendo.simplelibrary.client.event;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class FabricClientEvents {
    private final ClientEventsHolder clientEventsHolder;
    private final Map<Supplier<Block>, RenderType> blocks = new HashMap<>();

    public FabricClientEvents(ClientEventsHolder clientEventsHolder) {
        this.clientEventsHolder = clientEventsHolder;
    }

    public FabricClientEvents addBlockRenderTypes(Supplier<Block> block, RenderType renderType) {
        this.blocks.put(block, renderType);
        return this;
    }


    public void runEvents() {
        this.blocks.forEach((blockSupplier, renderType) -> {
            BlockRenderLayerMap.INSTANCE.putBlock(blockSupplier.get(), renderType);
        });

        this.clientEventsHolder.menuScreens.forEach(menuScreenEntry -> {
            MenuScreens.register(menuScreenEntry.menuType(), menuScreenEntry.screenConstructor());
        });

        this.clientEventsHolder.entityModels.forEach(entityModelEntry -> {
            EntityRendererRegistry.register(entityModelEntry.entityType(), entityModelEntry.entityRendererProvider());
        });
        this.clientEventsHolder.blockModels.forEach(blockModelEntry -> {
            BlockEntityRenderers.register(blockModelEntry.blockEntityType(), blockModelEntry.blockEntityRendererProvider());
        });

        this.clientEventsHolder.modelLayers.forEach(modelLayer -> {
            EntityModelLayerRegistry.registerModelLayer(modelLayer.modelLayerLocation(), modelLayer.texturedModelDataProvider()::createModelData);
        });

        this.clientEventsHolder.keyMappings.forEach(keyMapping -> {
            KeyBindingHelper.registerKeyBinding(keyMapping);
        });
    }
}
