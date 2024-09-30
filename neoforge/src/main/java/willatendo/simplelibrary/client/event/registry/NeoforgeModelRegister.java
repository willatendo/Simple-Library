package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class NeoforgeModelRegister implements ModelRegistry {
    private final EntityRenderersEvent.RegisterRenderers event;

    public NeoforgeModelRegister(EntityRenderersEvent.RegisterRenderers event) {
        this.event = event;
    }

    @Override
    public <T extends Entity> void register(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider) {
        this.event.registerEntityRenderer(entityType, entityRendererProvider);
    }

    @Override
    public <T extends BlockEntity> void register(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> blockEntityRendererProvider) {
        this.event.registerBlockEntityRenderer(blockEntityType, blockEntityRendererProvider);
    }
}
