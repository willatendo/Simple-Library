package ca.willatendo.simplelibrary.server;

import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import ca.willatendo.simplelibrary.server.command.CommandRegisterInformation;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface EventListener {
    default void commonSetup() {
    }

    // Registry

    default void registerAttributes(EventListener.AttributeRegister attributeRegister) {
    }

    default void registerBuiltInResourcePacks(EventListener.BuiltInResourcePackRegister builtInResourcePackRegister) {
    }

    default void registerCommands(EventListener.CommandRegister commandRegister) {
    }

    default void registerDynamicRegistries(EventListener.DynamicRegistryRegister dynamicRegistryRegister) {
    }

    default void registerNewRegistries(EventListener.NewRegistryRegister newRegistryRegister) {
    }

    default void registerPOI(EventListener.POIRegister poiRegister) {
    }

    default void registerRecipeBookSearchCategory(EventListener.RecipeBookSearchCategoryRegister recipeBookSearchCategoryRegister) {
    }

    default void registerServerReloadListener(EventListener.ServerReloadListenerRegister serverReloadListenerRegister) {
    }

    default void registerSpawnPlacements(EventListener.SpawnPlacementRegister spawnPlacementRegister) {
    }

    // Modification

    default void modifyCreativeModeTabs(EventListener.CreativeModeTabModification creativeModeTabModification) {
    }

    default void modifyFlammables(FireBlock fireBlock) {
    }

    default void modifyStructurePools(EventListener.StructurePoolModification structurePoolModification) {
    }

    // Events
    default InteractionResult playerEntityInteractEvent(Entity entity, Player player) {
        return null;
    }

    default void preEntityTickEvent(Entity entity) {
    }

    default void postEntityTickEvent(Entity entity) {
    }

    default void entityStruckByLightningBoltEvent(Entity entity, LightningBolt lightningBolt, Consumer<Boolean> cancel) {
    }

    default void dataReloadEvent(MinecraftServer minecraftServer) {
    }

    default void syncDataPackContentsEvent(ServerPlayer serverPlayer) {
    }

    default void serverAboutToStartEvent(MinecraftServer minecraftServer) {
    }

    default void serverStoppedEvent(MinecraftServer minecraftServer) {
    }

    // Registry

    @FunctionalInterface
    interface AttributeRegister {
        <T extends LivingEntity> void apply(EntityType<T> entityType, AttributeSupplier attributeSupplier);
    }

    @FunctionalInterface
    interface BuiltInResourcePackRegister {
        void apply(String modId, String resourcePackName, PackType packType, PackSource packSource, boolean alwaysActive, Pack.Position position);

        default void resourcePack(String modId, String resourcePackName, boolean alwaysActive, Pack.Position position) {
            this.apply(modId, resourcePackName, PackType.CLIENT_RESOURCES, PackSource.BUILT_IN, alwaysActive, position);
        }

        default void featurePack(String modId, String dataPackName, Pack.Position position) {
            this.apply(modId, dataPackName, PackType.SERVER_DATA, PackSource.FEATURE, false, position);
        }

        default void dataPack(String modId, String dataPackName, boolean alwaysActive, Pack.Position position) {
            this.apply(modId, dataPackName, PackType.SERVER_DATA, PackSource.BUILT_IN, alwaysActive, position);
        }
    }

    @FunctionalInterface
    interface CommandRegister {
        void apply(CommandRegisterInformation commandRegisterInformation);
    }

    interface DynamicRegistryRegister {
        <T> void apply(ResourceKey<Registry<T>> resourceKey, Codec<T> codec);

        <T> void apply(ResourceKey<Registry<T>> resourceKey, Codec<T> codec, Codec<T> networkCodec);
    }

    @FunctionalInterface
    interface NewRegistryRegister {
        <T> void apply(Registry<T> registry);
    }

    @FunctionalInterface
    interface POIRegister {
        Supplier<PoiType> apply(String id, Supplier<PoiType> poiType);
    }

    @FunctionalInterface
    interface RecipeBookSearchCategoryRegister {
        void register(ExtendedRecipeBookCategory extendedRecipeBookCategory, RecipeBookCategory[] recipeBookCategories);
    }

    interface ServerReloadListenerRegister {
        <T extends PreparableReloadListener> T apply(Identifier identifier, T preparableReloadListener);

        ReloadableServerResources getServerResources();
    }

    @FunctionalInterface
    interface SpawnPlacementRegister {
        <T extends Mob> void apply(EntityType<T> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate);
    }

    // Modification

    interface CreativeModeTabModification {
        ResourceKey<CreativeModeTab> getCreativeModeTabKey();

        CreativeModeTab.ItemDisplayParameters getItemDisplayParameters();

        FeatureFlagSet getEnabledFeatureFlags();

        boolean hasOperatorPermissions();

        void remove(ItemStack itemStack, CreativeModeTab.TabVisibility tabVisibility);

        void insert(ItemStack itemStack, CreativeModeTab.TabVisibility tabVisibility);

        default void insert(ItemStack itemStack) {
            this.insert(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        default void insert(ItemLike itemLike, CreativeModeTab.TabVisibility tabVisibility) {
            this.insert(new ItemStack(itemLike), tabVisibility);
        }

        default void insert(ItemLike itemLike) {
            this.insert(new ItemStack(itemLike), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        void insertBeginning(ItemStack itemStack, CreativeModeTab.TabVisibility tabVisibility);

        default void insertBeginning(ItemStack itemStack) {
            this.insertBeginning(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        default void insertBeginning(ItemLike itemLike, CreativeModeTab.TabVisibility tabVisibility) {
            this.insertBeginning(new ItemStack(itemLike), tabVisibility);
        }

        default void insertBeginning(ItemLike itemStack) {
            this.insertBeginning(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        void insertAfter(ItemStack referenceItemStack, CreativeModeTab.TabVisibility tabVisibility, ItemStack... itemStacks);

        default void insertAfter(ItemStack referenceItemStack, ItemStack... itemStacks) {
            this.insertAfter(referenceItemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, itemStacks);
        }

        default void insertAfter(ItemStack referenceItemStack, CreativeModeTab.TabVisibility tabVisibility, ItemLike... itemLikes) {
            this.insertAfter(referenceItemStack, tabVisibility, Arrays.asList(itemLikes).stream().map(ItemStack::new).toArray(ItemStack[]::new));
        }

        default void insertAfter(ItemStack referenceItemStack, ItemLike... itemLikes) {
            this.insertAfter(referenceItemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, itemLikes);
        }

        void insertBefore(ItemStack referenceItemStack, CreativeModeTab.TabVisibility tabVisibility, ItemStack... itemStacks);

        default void insertBefore(ItemStack referenceItemStack, ItemStack... itemStacks) {
            this.insertBefore(referenceItemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, itemStacks);
        }

        default void insertBefore(ItemStack referenceItemStack, CreativeModeTab.TabVisibility tabVisibility, ItemLike... itemLikes) {
            this.insertBefore(referenceItemStack, tabVisibility, Arrays.asList(itemLikes).stream().map(ItemStack::new).toArray(ItemStack[]::new));
        }

        default void insertBefore(ItemStack referenceItemStack, ItemLike... itemLikes) {
            this.insertBefore(referenceItemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, itemLikes);
        }
    }

    interface StructurePoolModification {
        ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(Registries.PROCESSOR_LIST, CoreUtils.minecraft("empty"));

        Registry<StructureTemplatePool> getTemplatePoolRegistry();

        Registry<StructureProcessorList> getProcessorListRegistry();

        default Identifier getPlainsPoolLocation() {
            return CoreUtils.minecraft("village/plains/houses");
        }

        default Identifier getDesertPoolLocation() {
            return CoreUtils.minecraft("village/desert/houses");
        }

        default Identifier getSavannaPoolLocation() {
            return CoreUtils.minecraft("village/savanna/houses");
        }

        default Identifier getSnowyPoolLocation() {
            return CoreUtils.minecraft("village/snowy/houses");
        }

        default Identifier getTaigaPoolLocation() {
            return CoreUtils.minecraft("village/taiga/houses");
        }

        default void add(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, Identifier poolRL, String nbtPieceRL, int weight) {
            Holder.Reference<StructureProcessorList> empty = processorListRegistry.getOrThrow(EMPTY_PROCESSOR_LIST_KEY);

            StructureTemplatePool structureTemplatePool = templatePoolRegistry.getValue(poolRL);
            if (structureTemplatePool == null) {
                return;
            }

            SinglePoolElement singlePoolElement = SinglePoolElement.single(nbtPieceRL, empty).apply(StructureTemplatePool.Projection.RIGID);

            for (int i = 0; i < weight; i++) {
                structureTemplatePool.templates.add(singlePoolElement);
            }

            List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = Lists.newArrayList(structureTemplatePool.rawTemplates.iterator());
            listOfPieceEntries.add(new Pair<>(singlePoolElement, weight));
            structureTemplatePool.rawTemplates = listOfPieceEntries;
        }
    }
}
