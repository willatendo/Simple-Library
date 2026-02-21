package ca.willatendo.simplelibrary.client;

import ca.willatendo.simplelibrary.client.event.RegisterRecipeBookOverlayEvent;
import ca.willatendo.simplelibrary.client.event.SimpleScreenEvents;
import ca.willatendo.simplelibrary.network.PacketRegistryListener;
import ca.willatendo.simplelibrary.network.PacketSupplier;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;
import java.util.List;

public record FabricClientModInit() implements ClientModInit {
    @Override
    public void clientEventListener(ClientEventListener clientEventListener) {
        clientEventListener.clientSetup();

        clientEventListener.registerBlockColors(ColorProviderRegistry.BLOCK::register);

        clientEventListener.registerKeyMappings(KeyBindingHelper::registerKeyBinding);

        clientEventListener.registerMenuScreens(MenuScreens::register);

        clientEventListener.registerLayerDefinitions((modelLayerLocation, layerDefinition) -> EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinition::get));

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

        RegisterRecipeBookOverlayEvent.EVENT.register(map -> clientEventListener.registerRecipeBookOverlay(map::put));

        clientEventListener.registerParticleColorExemptions(blocks -> Arrays.stream(blocks).forEach(block -> ParticleRenderEvents.ALLOW_BLOCK_DUST_TINT.register((blockState, clientLevel, blockPos) -> !blockState.is(block))));

        clientEventListener.registerParticleProviders(new ClientEventListener.ParticleProviderRegister() {
            @Override
            public <T extends ParticleOptions> void apply(ParticleType<T> particleType, ParticleProvider<T> particleFactory) {
                ParticleFactoryRegistry.getInstance().register(particleType, particleFactory);
            }

            @Override
            public <T extends ParticleOptions> void apply(ParticleType<T> particleType, ParticleProvider.Sprite<T> sprite) {
                ParticleFactoryRegistry.getInstance().register(particleType, sprite::createParticle);
            }

            @Override
            public <T extends ParticleOptions> void apply(ParticleType<T> particleType, ParticleResources.SpriteParticleRegistration<T> particleMetaFactory) {
                ParticleFactoryRegistry.getInstance().register(particleType, particleMetaFactory::create);
            }
        });

        clientEventListener.registerSpecialModelRenderers(SpecialModelRenderers.ID_MAPPER::put);

        ScreenEvents.BEFORE_INIT.register((minecraft, screen, scaledWidth, scaledHeight) -> {
            ScreenEvents.beforeRender(screen).register((screenIn, guiGraphics, mouseX, mouseY, partialTick) -> clientEventListener.screenRenderPreEvent(screenIn, guiGraphics, mouseX, mouseY));
            List<AbstractWidget> widgets = Screens.getButtons(screen);
            clientEventListener.screenInitPreEvent(screen, widgets::add);
            ScreenEvents.afterRender(screen).register((screenIn, guiGraphics, mouseX, mouseY, partialTick) -> clientEventListener.screenRenderPostEvent(screenIn, guiGraphics, mouseX, mouseY));
        });

        ScreenEvents.AFTER_INIT.register((minecraft, screen, scaledWidth, scaledHeight) -> {
            List<AbstractWidget> widgets = Screens.getButtons(screen);
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
                PayloadTypeRegistry.playS2C().register(type, codec);

                ClientPlayNetworking.registerGlobalReceiver(type, (customPacketPayload, context) -> packetSupplier.handle(customPacketPayload, context.player()));
            }
        });
    }
}
