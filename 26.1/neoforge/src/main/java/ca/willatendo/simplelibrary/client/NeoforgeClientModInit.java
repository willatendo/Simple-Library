package ca.willatendo.simplelibrary.client;

import ca.willatendo.simplelibrary.client.event.RegisterRecipeBookOverlayEvent;
import ca.willatendo.simplelibrary.network.PacketRegistryListener;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;

public record NeoforgeClientModInit(IEventBus iEventBus) implements ClientModInit {
    @Override
    public void clientEventListener(ClientEventListener clientEventListener) {
        IEventBus neoforgeEventBus = NeoForge.EVENT_BUS;

        this.iEventBus.addListener(FMLClientSetupEvent.class, fmlClientSetupEvent -> clientEventListener.clientSetup());

        this.iEventBus.addListener(RegisterColorHandlersEvent.Block.class, block -> clientEventListener.registerBlockColors(block::register));

        this.iEventBus.addListener(AddClientReloadListenersEvent.class, addClientReloadListenersEvent -> clientEventListener.registerClientReloadListener(addClientReloadListenersEvent::addListener));

        this.iEventBus.addListener(RegisterKeyMappingsEvent.class, registerKeyMappingsEvent -> clientEventListener.registerKeyMappings(registerKeyMappingsEvent::register));

        this.iEventBus.addListener(RegisterMenuScreensEvent.class, registerMenuScreensEvent -> clientEventListener.registerMenuScreens(registerMenuScreensEvent::register));

        this.iEventBus.addListener(EntityRenderersEvent.RegisterLayerDefinitions.class, registerLayerDefinitions -> clientEventListener.registerLayerDefinitions(registerLayerDefinitions::registerLayerDefinition));

        this.iEventBus.addListener(EntityRenderersEvent.RegisterRenderers.class, registerRenderers -> {
            clientEventListener.registerRenderers(new ClientEventListener.RendererRegister() {
                @Override
                public <T extends Entity> void apply(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider) {
                    registerRenderers.registerEntityRenderer(entityType, entityRendererProvider);
                }

                @Override
                public <T extends BlockEntity, S extends BlockEntityRenderState> void apply(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T, S> blockEntityRendererProvider) {
                    registerRenderers.registerBlockEntityRenderer(blockEntityType, blockEntityRendererProvider);
                }
            });
        });

        neoforgeEventBus.addListener(RegisterRecipeBookOverlayEvent.class, registerRecipeBookOverlayEvent -> clientEventListener.registerRecipeBookOverlay(registerRecipeBookOverlayEvent::register));

        this.iEventBus.addListener(RegisterClientExtensionsEvent.class, registerClientExtensionsEvent -> clientEventListener.registerParticleColorExemptions(blocks -> registerClientExtensionsEvent.registerBlock(new IClientBlockExtensions() {
            @Override
            public boolean areBreakingParticlesTinted(BlockState blockState, ClientLevel clientLevel, BlockPos blockPos) {
                return false;
            }
        }, blocks)));

        this.iEventBus.addListener(RegisterParticleProvidersEvent.class, registerParticleProvidersEvent -> clientEventListener.registerParticleProviders(new ClientEventListener.ParticleProviderRegister() {
            @Override
            public <T extends ParticleOptions> void apply(ParticleType<T> particleType, ParticleProvider<T> particleFactory) {
                registerParticleProvidersEvent.registerSpecial(particleType, particleFactory);
            }

            @Override
            public <T extends ParticleOptions> void apply(ParticleType<T> particleType, ParticleProvider.Sprite<T> sprite) {
                registerParticleProvidersEvent.registerSpecial(particleType, sprite::createParticle);
            }

            @Override
            public <T extends ParticleOptions> void apply(ParticleType<T> particleType, ParticleResources.SpriteParticleRegistration<T> particleMetaFactory) {
                registerParticleProvidersEvent.registerSpriteSet(particleType, particleMetaFactory);
            }
        }));

        this.iEventBus.addListener(RegisterSpecialModelRendererEvent.class, registerSpecialModelRendererEvent -> clientEventListener.registerSpecialModelRenderers(registerSpecialModelRendererEvent::register));

        this.iEventBus.addListener(RegisterTextureAtlasesEvent.class, registerTextureAtlasesEvent -> clientEventListener.registerTextureAtlases(registerTextureAtlasesEvent::register));

        neoforgeEventBus.addListener(ScreenEvent.Init.Pre.class, pre -> clientEventListener.screenInitPreEvent(pre.getScreen(), pre::addListener));

        neoforgeEventBus.addListener(ScreenEvent.Init.Post.class, post -> clientEventListener.screenInitPostEvent(post.getScreen(), post::addListener));

        neoforgeEventBus.addListener(ScreenEvent.Render.Pre.class, pre -> clientEventListener.screenRenderPreEvent(pre.getScreen(), pre.getGuiGraphics(), pre.getMouseX(), pre.getMouseY()));

        neoforgeEventBus.addListener(ScreenEvent.Render.Post.class, post -> clientEventListener.screenRenderPostEvent(post.getScreen(), post.getGuiGraphics(), post.getMouseX(), post.getMouseY()));

        neoforgeEventBus.addListener(ScreenEvent.Closing.class, closing -> clientEventListener.screenCloseEvent(closing.getScreen()));

        neoforgeEventBus.addListener(ClientPlayerNetworkEvent.LoggingOut.class, loggingOut -> clientEventListener.clientPlayerLoggingOut());
    }

    @Override
    public void packetRegistryListener(PacketRegistryListener packetRegistryListener) {
    }
}
