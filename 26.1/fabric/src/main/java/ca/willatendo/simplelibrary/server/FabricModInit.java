package ca.willatendo.simplelibrary.server;

import ca.willatendo.simplelibrary.core.registry.RegisterFunction;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import ca.willatendo.simplelibrary.network.PacketRegistryListener;
import ca.willatendo.simplelibrary.network.PacketSupplier;
import ca.willatendo.simplelibrary.server.event.LightingBoltEvent;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public record FabricModInit(String modId) implements ModInit {
    @Override
    public void register(SimpleRegistry<?>... simpleRegistries) {
        Arrays.stream(simpleRegistries).forEach(simpleRegistry -> simpleRegistry.addEntries(new RegisterFunction() {
            @Override
            public <T> void register(ResourceKey<? extends Registry<T>> registryKey, Identifier identifier, Supplier<T> value) {
                Registry.register((Registry<T>) BuiltInRegistries.REGISTRY.getValue(registryKey.identifier()), identifier, value.get());
            }
        }));
    }

    @Override
    public void eventListener(EventListener eventListener) {
        eventListener.commonSetup();

        // Registry

        eventListener.registerAttributes(FabricDefaultAttributeRegistry::register);

        Event<CommandRegistrationCallback> event = CommandRegistrationCallback.EVENT;
        event.register((commandDispatcher, commandBuildContext, commandSelection) -> eventListener.registerCommands(commandRegisterInformation -> commandRegisterInformation.register(commandDispatcher, commandBuildContext, commandSelection)));

        eventListener.registerDataSerializers(new EventListener.DataSerializerRegister() {
            @Override
            public <T> Supplier<EntityDataSerializer<T>> apply(String id, Supplier<EntityDataSerializer<T>> entityDataSerializer) {
                EntityDataSerializers.registerSerializer(entityDataSerializer.get());
                return entityDataSerializer;
            }
        });

        eventListener.registerDynamicRegistries(new EventListener.DynamicRegistryRegister() {
            @Override
            public <T> void apply(ResourceKey<Registry<T>> resourceKey, Codec<T> codec) {
                DynamicRegistries.register(resourceKey, codec);
            }

            @Override
            public <T> void apply(ResourceKey<Registry<T>> resourceKey, Codec<T> codec, Codec<T> networkCodec) {
                DynamicRegistries.register(resourceKey, codec);
            }
        });

        eventListener.registerNewRegistries(new EventListener.NewRegistryRegister() {
            @Override
            public <T> void apply(Registry<T> registry) {
            }
        });

        eventListener.registerPOI((id, poiType) -> {
            PointOfInterestHelper.register(CoreUtils.resource(this.modId, id), poiType.get().maxTickets(), poiType.get().validRange(), poiType.get().matchingStates());
            return poiType;
        });

        eventListener.registerBuiltInResourcePacks((modId, resourcePackName) -> {
            Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modId);
            ResourceManagerHelper.registerBuiltinResourcePack(CoreUtils.resource(modId, resourcePackName), modContainer.get(), CoreUtils.translation(modId, "resourcePack", resourcePackName + ".name"), ResourcePackActivationType.NORMAL);
        });

        eventListener.registerSpawnPlacements(SpawnPlacements::register);

        // Modification

        eventListener.modifyFlammables((FireBlock) Blocks.FIRE);

        eventListener.modifyVillagerTrades((villagerProfession, level1Trades, level2Trades, level3Trades, level4Trades, level5Trades) -> {
            TradeOfferHelper.registerVillagerOffers(villagerProfession, 1, itemListings -> itemListings.addAll(level1Trades));
            TradeOfferHelper.registerVillagerOffers(villagerProfession, 2, itemListings -> itemListings.addAll(level2Trades));
            TradeOfferHelper.registerVillagerOffers(villagerProfession, 3, itemListings -> itemListings.addAll(level3Trades));
            TradeOfferHelper.registerVillagerOffers(villagerProfession, 4, itemListings -> itemListings.addAll(level4Trades));
            TradeOfferHelper.registerVillagerOffers(villagerProfession, 5, itemListings -> itemListings.addAll(level5Trades));
        });

        // Events

        LightingBoltEvent.ENTITY_STRUCK_BY_LIGHTING.register((entity, lightningBolt) -> {
            AtomicBoolean cancel = new AtomicBoolean(false);
            eventListener.entityStruckByLightningBoltEvent(entity, lightningBolt, cancel::set);
            return cancel.get();
        });

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((serverPlayer, joined) -> eventListener.syncDataPackContentsEvent(serverPlayer));

        ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> {
            eventListener.serverAboutToStartEvent(minecraftServer);

            eventListener.modifyStructurePools(new EventListener.StructurePoolModification() {
                @Override
                public Registry<StructureTemplatePool> getTemplatePoolRegistry() {
                    return minecraftServer.registryAccess().lookupOrThrow(Registries.TEMPLATE_POOL);
                }

                @Override
                public Registry<StructureProcessorList> getProcessorListRegistry() {
                    return minecraftServer.registryAccess().lookupOrThrow(Registries.PROCESSOR_LIST);
                }
            });
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(eventListener::serverStoppedEvent);
    }

    @Override
    public void packetRegistryListener(PacketRegistryListener packetRegistryListener) {
        packetRegistryListener.registerServerboundPackets(new PacketRegistryListener.ServerboundPacketRegister() {
            @Override
            public <T extends CustomPacketPayload> void apply(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, PacketSupplier<T> packetSupplier) {
                PayloadTypeRegistry.playC2S().register(type, codec);

                ServerPlayNetworking.registerGlobalReceiver(type, (customPacketPayload, context) -> packetSupplier.handle(customPacketPayload, context.player()));
            }
        });
    }
}
