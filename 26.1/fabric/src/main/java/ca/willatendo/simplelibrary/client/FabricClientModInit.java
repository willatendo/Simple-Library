package ca.willatendo.simplelibrary.client;

import ca.willatendo.simplelibrary.client.event.RegisterRecipeBookOverlayEvent;
import ca.willatendo.simplelibrary.client.event.SimpleScreenEvents;
import ca.willatendo.simplelibrary.network.PacketRegistryListener;
import ca.willatendo.simplelibrary.network.PacketSupplier;
import ca.willatendo.simplelibrary.server.event.RegisterTextureAtlasesEvent;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;

public record FabricClientModInit() implements ClientModInit {
    @Override
    public void clientEventListener(ClientEventListener clientEventListener) {
        clientEventListener.clientSetup();

        clientEventListener.registerBlockTints(BlockColorRegistry::register);

        ResourceLoader resourceLoader = ResourceLoader.get(PackType.CLIENT_RESOURCES);
        clientEventListener.registerClientReloadListener(resourceLoader::registerReloadListener);

        clientEventListener.registerKeyMappings(KeyMappingHelper::registerKeyMapping);

        clientEventListener.registerMenuScreens(MenuScreens::register);

        clientEventListener.registerLayerDefinitions((modelLayerLocation, layerDefinition) -> ModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinition::get));

        clientEventListener.registerRenderers(new ClientEventListener.RendererRegister() {
            @Override
            public <T extends Entity> void apply(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider) {
                EntityRenderers.register(entityType, entityRendererProvider);
            }

            @Override
            public <T extends BlockEntity, S extends BlockEntityRenderState> void apply(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T, S> blockEntityRendererProvider) {
                BlockEntityRenderers.register(blockEntityType, blockEntityRendererProvider);
            }
        });

        RegisterRecipeBookOverlayEvent.EVENT.register(biConsumer -> clientEventListener.registerRecipeBookOverlay(biConsumer::accept));

        clientEventListener.registerParticleProviders(new ClientEventListener.ParticleProviderRegister() {
            @Override
            public <T extends ParticleOptions> void apply(ParticleType<T> particleType, ParticleProvider<T> particleFactory) {
                ParticleProviderRegistry.getInstance().register(particleType, particleFactory);
            }

            @Override
            public <T extends ParticleOptions> void apply(ParticleType<T> particleType, ParticleProvider.Sprite<T> sprite) {
                ParticleProviderRegistry.getInstance().register(particleType, sprite::createParticle);
            }

            @Override
            public <T extends ParticleOptions> void apply(ParticleType<T> particleType, ParticleResources.SpriteParticleRegistration<T> particleMetaFactory) {
                ParticleProviderRegistry.getInstance().register(particleType, particleMetaFactory::create);
            }
        });

        clientEventListener.registerSpecialModelRenderers(SpecialModelRenderers.ID_MAPPER::put);

        clientEventListener.registerTextureAtlases(atlasConfig -> RegisterTextureAtlasesEvent.EVENT.register(consumer -> consumer.accept(atlasConfig)));

        ClientTickEvents.START_CLIENT_TICK.register(clientEventListener::clientTickPreEvent);

        ClientTickEvents.END_CLIENT_TICK.register(clientEventListener::clientTickPostEvent);

        ScreenEvents.BEFORE_INIT.register((minecraft, screen, scaledWidth, scaledHeight) -> {
            ScreenEvents.beforeExtract(screen).register((screenIn, guiGraphics, mouseX, mouseY, partialTick) -> clientEventListener.screenRenderPreEvent(screenIn, guiGraphics, mouseX, mouseY));
            List<AbstractWidget> widgets = Screens.getWidgets(screen);
            clientEventListener.screenInitPreEvent(screen, widgets::add);
            ScreenEvents.afterExtract(screen).register((screenIn, guiGraphics, mouseX, mouseY, partialTick) -> clientEventListener.screenRenderPostEvent(screenIn, guiGraphics, mouseX, mouseY));
        });

        ScreenEvents.AFTER_INIT.register((minecraft, screen, scaledWidth, scaledHeight) -> {
            List<AbstractWidget> widgets = Screens.getWidgets(screen);
            clientEventListener.screenInitPostEvent(screen, widgets::add);
        });

        SimpleScreenEvents.CLOSING.register(clientEventListener::screenCloseEvent);

        ClientPlayConnectionEvents.DISCONNECT.register((clientPacketListener, minecraft) -> clientEventListener.clientPlayerLoggingOut());
    }

    @Override
    public void packetRegistryListener(PacketRegistryListener packetRegistryListener) {
        packetRegistryListener.registerClientboundPackets(new PacketRegistryListener.ClientboundPacketRegister() {
            @Override
            public <T extends CustomPacketPayload> void apply(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, PacketSupplier<T> packetSupplier) {
                PayloadTypeRegistry.clientboundPlay().register(type, codec);

                ClientPlayNetworking.registerGlobalReceiver(type, (customPacketPayload, context) -> packetSupplier.handle(customPacketPayload, context.player()));
            }
        });
    }
}
