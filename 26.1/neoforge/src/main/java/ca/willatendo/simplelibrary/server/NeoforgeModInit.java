package ca.willatendo.simplelibrary.server;

import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.AttachmentTypesSubRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.EntityDataSerializerSubRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.NeoforgeAttachmentTypesSubRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.NeoforgeEntityDataSerializerSubRegistry;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import ca.willatendo.simplelibrary.network.PacketRegistryListener;
import ca.willatendo.simplelibrary.network.PacketSupplier;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookSearchCategoriesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Arrays;

public record NeoforgeModInit(String modId, String packetVersion, IEventBus iEventBus) implements ModInit {
    public NeoforgeModInit(IEventBus iEventBus) {
        this(null, null, iEventBus);
    }

    @Override
    public void register(SimpleRegistry<?>... simpleRegistries) {
        this.iEventBus.addListener(RegisterEvent.class, registryEvent -> Arrays.stream(simpleRegistries).forEach(simpleRegistry -> {
            if (simpleRegistry.getRegistryKey().equals(registryEvent.getRegistryKey())) {
                simpleRegistry.addEntries(registryEvent::register);
            }
        }));
    }

    @Override
    public void register(EntityDataSerializerSubRegistry entityDataSerializerSubRegistry) {
        if (entityDataSerializerSubRegistry instanceof NeoforgeEntityDataSerializerSubRegistry neoforgeEntityDataSerializerSubRegistry) {
            this.register(neoforgeEntityDataSerializerSubRegistry.getSimpleRegistry());
        }
    }

    @Override
    public void register(AttachmentTypesSubRegistry attachmentTypesSubRegistry) {
        if (attachmentTypesSubRegistry instanceof NeoforgeAttachmentTypesSubRegistry neoforgeAttachmentTypesSubRegistry) {
            this.register(neoforgeAttachmentTypesSubRegistry.getSimpleRegistry());
        }
    }

    @Override
    public void eventListener(EventListener eventListener) {
        IEventBus iEventBus = NeoForge.EVENT_BUS;
        this.iEventBus.addListener(FMLCommonSetupEvent.class, fmlCommonSetupEvent -> {
            eventListener.commonSetup();
            eventListener.modifyFlammables((FireBlock) Blocks.FIRE);
        });

        // Registry

        this.iEventBus.addListener(EntityAttributeCreationEvent.class, entityAttributeCreationEvent -> eventListener.registerAttributes(entityAttributeCreationEvent::put));

        iEventBus.addListener(RegisterCommandsEvent.class, registerCommandsEvent -> eventListener.registerCommands(commandRegisterInformation -> commandRegisterInformation.register(registerCommandsEvent.getDispatcher(), registerCommandsEvent.getBuildContext(), registerCommandsEvent.getCommandSelection())));

        this.iEventBus.addListener(RegisterEvent.class, registerEvent -> {
            registerEvent.register(Registries.POINT_OF_INTEREST_TYPE, poiTypeRegisterHelper -> eventListener.registerPOI((id, poiType) -> {
                poiTypeRegisterHelper.register(CoreUtils.resource(NeoforgeModInit.this.modId(), id), poiType.get());
                return poiType;
            }));
        });

        this.iEventBus.addListener(DataPackRegistryEvent.NewRegistry.class, newRegistry -> eventListener.registerDynamicRegistries(new EventListener.DynamicRegistryRegister() {
            @Override
            public <T> void apply(ResourceKey<Registry<T>> resourceKey, Codec<T> codec) {
                newRegistry.<T>dataPackRegistry(resourceKey, codec);
            }

            @Override
            public <T> void apply(ResourceKey<Registry<T>> resourceKey, Codec<T> codec, Codec<T> networkCodec) {
                newRegistry.<T>dataPackRegistry(resourceKey, codec, networkCodec);
            }
        }));

        this.iEventBus.addListener(NewRegistryEvent.class, newRegistryEvent -> eventListener.registerNewRegistries(newRegistryEvent::register));

        this.iEventBus.addListener(AddPackFindersEvent.class, addPackFindersEvent -> eventListener.registerBuiltInResourcePacks((modId, resourcePackName, packType, packSource, alwaysActive, position) -> {
            boolean datapack = packType == PackType.SERVER_DATA;
            addPackFindersEvent.addPackFinders(CoreUtils.resource(modId, (datapack ? "data/" + modId + "/datapacks/" : "resourcepacks/") + resourcePackName), packType, CoreUtils.translation((datapack ? "dataPack" : "resourcePack"), modId, resourcePackName + ".name"), packSource, alwaysActive, position);
        }));

        iEventBus.addListener(AddServerReloadListenersEvent.class, addServerReloadListenersEvent -> eventListener.registerServerReloadListener(new EventListener.ServerReloadListenerRegister() {
            @Override
            public <T extends PreparableReloadListener> T apply(Identifier identifier, T preparableReloadListener) {
                addServerReloadListenersEvent.addListener(identifier, preparableReloadListener);
                return preparableReloadListener;
            }

            @Override
            public ReloadableServerResources getServerResources() {
                return addServerReloadListenersEvent.getServerResources();
            }
        }));

        this.iEventBus.addListener(RegisterRecipeBookSearchCategoriesEvent.class, registerRecipeBookSearchCategoryEvent -> {
            eventListener.registerRecipeBookSearchCategory(registerRecipeBookSearchCategoryEvent::register);
        });

        this.iEventBus.addListener(RegisterSpawnPlacementsEvent.class, registerSpawnPlacementsEvent -> eventListener.registerSpawnPlacements(new EventListener.SpawnPlacementRegister() {
            @Override
            public <T extends Mob> void apply(EntityType<T> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
                registerSpawnPlacementsEvent.register(entityType, spawnPlacementType, types, spawnPredicate, RegisterSpawnPlacementsEvent.Operation.OR);
            }
        }));

        // Modification

        // Events
        iEventBus.addListener(PlayerInteractEvent.EntityInteract.class, entityInteract -> {
            InteractionResult interactionResult = eventListener.playerEntityInteractEvent(entityInteract.getTarget(), entityInteract.getEntity());
            if (interactionResult != null) {
                entityInteract.setCancellationResult(interactionResult);
            }
        });

        iEventBus.addListener(EntityTickEvent.Pre.class, pre -> eventListener.preEntityTickEvent(pre.getEntity()));

        iEventBus.addListener(EntityTickEvent.Post.class, post -> eventListener.postEntityTickEvent(post.getEntity()));

        iEventBus.addListener(EntityStruckByLightningEvent.class, entityStruckByLightningEvent -> eventListener.entityStruckByLightningBoltEvent(entityStruckByLightningEvent.getEntity(), entityStruckByLightningEvent.getLightning(), entityStruckByLightningEvent::setCanceled));

        iEventBus.addListener(OnDatapackSyncEvent.class, onDatapackSyncEvent -> eventListener.dataReloadEvent(onDatapackSyncEvent.getPlayerList().getServer()));

        iEventBus.addListener(OnDatapackSyncEvent.class, onDatapackSyncEvent -> eventListener.syncDataPackContentsEvent(onDatapackSyncEvent.getPlayer()));

        iEventBus.addListener(ServerAboutToStartEvent.class, serverAboutToStartEvent -> {
            MinecraftServer minecraftServer = serverAboutToStartEvent.getServer();
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

        iEventBus.addListener(ServerStoppedEvent.class, serverStoppedEvent -> eventListener.serverStoppedEvent(serverStoppedEvent.getServer()));
    }

    @Override
    public void packetRegistryListener(PacketRegistryListener packetRegistryListener) {
        if (this.modId != null && this.packetVersion != null) {
            this.iEventBus.addListener(RegisterPayloadHandlersEvent.class, registerPayloadHandlersEvent -> {
                PayloadRegistrar payloadRegistrar = registerPayloadHandlersEvent.registrar(this.modId).versioned(this.packetVersion).optional();

                packetRegistryListener.registerClientboundPackets(new PacketRegistryListener.ClientboundPacketRegister() {
                    @Override
                    public <T extends CustomPacketPayload> void apply(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, PacketSupplier<T> packetSupplier) {
                        payloadRegistrar.playToClient(type, codec, (customPacketPayload, iPayloadContext) -> iPayloadContext.enqueueWork(() -> packetSupplier.handle(customPacketPayload, iPayloadContext.player())));
                    }
                });

                packetRegistryListener.registerServerboundPackets(new PacketRegistryListener.ServerboundPacketRegister() {
                    @Override
                    public <T extends CustomPacketPayload> void apply(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, PacketSupplier<T> packetSupplier) {
                        payloadRegistrar.playToServer(type, codec, (customPacketPayload, iPayloadContext) -> iPayloadContext.enqueueWork(() -> packetSupplier.handle(customPacketPayload, iPayloadContext.player())));
                    }
                });
            });
        } else {
            SimpleCoreUtils.LOGGER.error("Cannot register packets: did not supply mod id and packet versions!");
        }
    }
}
