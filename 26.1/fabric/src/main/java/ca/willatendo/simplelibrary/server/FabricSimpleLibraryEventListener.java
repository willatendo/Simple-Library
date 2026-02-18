package ca.willatendo.simplelibrary.server;

import ca.willatendo.simplelibrary.core.registry.SimpleLibraryRegistries;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import ca.willatendo.simplelibrary.mixin.MappedRegistryAccessor;
import ca.willatendo.simplelibrary.network.clientbound.ClientboundRecipeContentPacket;
import ca.willatendo.simplelibrary.network.utils.NetworkUtils;
import ca.willatendo.simplelibrary.server.biome_modifier.BiomeModifier;
import ca.willatendo.simplelibrary.server.data_maps.DataMapLoader;
import ca.willatendo.simplelibrary.server.event.TagEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;
import java.util.Optional;

public record FabricSimpleLibraryEventListener() implements EventListener {
    private static DataMapLoader DATA_MAP_LOADER;

    @Override
    public void commonSetup() {
        TagEvents.UPDATE_TAGS.register((provider, fromClientPacket, isIntegratedServerConnection) -> {
            if (!fromClientPacket) {
                DATA_MAP_LOADER.apply();
            }
        });
    }

    @Override
    public void registerDynamicRegistries(DynamicRegistryRegister dynamicRegistryRegister) {
        dynamicRegistryRegister.apply(SimpleLibraryRegistries.BIOME_MODIFIERS, BiomeModifier.DIRECT_CODEC);
    }

    @Override
    public void syncDataPackContentsEvent(ServerPlayer serverPlayer) {
        NetworkUtils.sendToClient(serverPlayer, ClientboundRecipeContentPacket.create(BuiltInRegistries.RECIPE_TYPE.stream().toList(), serverPlayer.server.getRecipeManager().recipes));
    }

    @Override
    public void registerServerReloadListener(ServerReloadListenerRegister serverReloadListenerRegister) {
        ReloadableServerResources reloadableServerResources = serverReloadListenerRegister.getServerResources();
        serverReloadListenerRegister.apply(CoreUtils.resource("neoforge", "data_maps"), DATA_MAP_LOADER = new DataMapLoader(reloadableServerResources.getConditionContext(), reloadableServerResources.getConditionContext().registryAccess()));
    }

    @Override
    public void serverAboutToStartEvent(MinecraftServer minecraftServer) {
        ServerLifecycleHooks.handleServerAboutToStart(minecraftServer);

        RegistryAccess registries = minecraftServer.registryAccess();
        List<BiomeModifier> biomeModifiers = registries.lookupOrThrow(SimpleLibraryRegistries.BIOME_MODIFIERS).listElements().map(Holder::value).toList();

        Registry<Biome> registry = registries.lookupOrThrow(Registries.BIOME);
        registry.listElements().forEach(biomeHolder -> {
            Biome biome = biomeHolder.value();

            FabricSimpleLibraryEventListener.ensureProperSync(biome.modifiableBiomeInfo().applyBiomeModifiers(biomeHolder, biomeModifiers, registries), biomeHolder, registry);

            MobSpawnSettings mobSpawnSettings = biome.getMobSettings();
            for (MobCategory mobCategory : mobSpawnSettings.getSpawnerTypes()) {
                for (Weighted<MobSpawnSettings.SpawnerData> spawnerData : mobSpawnSettings.getMobs(mobCategory).unwrap()) {
                    if (spawnerData.value().type().getCategory() != mobCategory) {
                        boolean isVanillaBug = spawnerData.value().type() == EntityType.OCELOT && (biomeHolder.is(Biomes.JUNGLE) || biomeHolder.is(Biomes.BAMBOO_JUNGLE));
                        if (!isVanillaBug) {
                            SimpleCoreUtils.LOGGER.warn("Detected {} that was registered with {} mob category but was added under {} mob category for {} biome! " + "Mobs should be added to biomes under the same mob category that the mob was registered as to prevent mob cap spawning issues.", BuiltInRegistries.ENTITY_TYPE.getKey(spawnerData.value().type()), spawnerData.value().type().getCategory(), mobCategory, biomeHolder.key().identifier());
                        }
                    }
                }
            }
        });
        registries.lookupOrThrow(Registries.LEVEL_STEM).forEach(levelStem -> levelStem.generator().refreshFeaturesPerStep());
    }

    @Override
    public void serverStoppedEvent(MinecraftServer minecraftServer) {
        ServerLifecycleHooks.handleServerStopped(minecraftServer);
    }

    private static <T> void ensureProperSync(boolean modified, Holder.Reference<T> holder, Registry<T> registry) {
        if (modified) {
            Optional<RegistrationInfo> originalInfo = registry.registrationInfo(holder.key());
            originalInfo.ifPresent(registrationInfo -> {
                RegistrationInfo newInfo = new RegistrationInfo(Optional.empty(), registrationInfo.lifecycle());
                ((MappedRegistryAccessor<T>) registry).simpleLibrary$getRegistrationInfos().put(holder.key(), newInfo);
            });
        }
    }
}
