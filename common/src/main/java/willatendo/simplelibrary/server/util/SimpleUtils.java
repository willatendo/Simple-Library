package willatendo.simplelibrary.server.util;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import willatendo.simplelibrary.platform.ModloaderHelper;
import willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import willatendo.simplelibrary.server.registry.SimpleHolder;
import willatendo.simplelibrary.server.registry.SimpleRegistry;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public final class SimpleUtils {
    public static final String SIMPLE_ID = "simplelibrary";
    public static final String MINECRAFT_ID = "minecraft";

    public static final Logger SIMPLE_LOGGER = LoggerFactory.getLogger(SimpleUtils.SIMPLE_ID);

    public static ResourceLocation simple(String path) {
        return SimpleUtils.resource(SIMPLE_ID, path);
    }

    public static ResourceLocation mc(String path) {
        return SimpleUtils.resource(MINECRAFT_ID, path);
    }

    // Multi-Platform Helpers

    public static <T> T loadModloaderHelper(Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }

    public static boolean isDevEnviroment() {
        return ModloaderHelper.INSTANCE.isDevEnviroment();
    }

    public static boolean isModLoaded(String modId) {
        return ModloaderHelper.INSTANCE.isModLoaded(modId);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedMenuSupplier<T> extendedMenuSupplier) {
        return ModloaderHelper.INSTANCE.createMenuType(extendedMenuSupplier);
    }

    public static <T> Registry<T> createRegistry(ResourceKey<Registry<T>> resourceKey, SimpleRegistryBuilder simpleRegistryBuilder) {
        return ModloaderHelper.INSTANCE.createRegistry(resourceKey, simpleRegistryBuilder);
    }

    public static SimpleParticleType createParticleType(boolean serverPlayer) {
        return ModloaderHelper.INSTANCE.createParticleType(serverPlayer);
    }

    public static void openContainer(MenuProvider menuProvider, BlockPos blockPos, ServerPlayer serverPlayer) {
        ModloaderHelper.INSTANCE.openContainer(menuProvider, blockPos, serverPlayer);
    }

    // Creative Mode Tab Helpers

    public static CreativeModeTab.Builder create(String modId, String id, Supplier<Item> icon, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
        return ModloaderHelper.INSTANCE.createCreativeModeTab().title(SimpleUtils.translation(modId, "itemGroup", id)).icon(() -> icon.get().getDefaultInstance()).displayItems(displayItemsGenerator);
    }

    // Registry Helpers

    public static <T extends Block> List<SimpleHolder<T>> registerDyedBlocks(SimpleRegistry<T> simpleRegistry, String baseID, Function<DyeColor, Supplier<T>> baseBlock) {
        List<SimpleHolder<T>> blocks = Lists.newArrayList();
        for (DyeColor dyeColor : DyeColor.values()) {
            SimpleHolder<T> block = simpleRegistry.register(dyeColor.getName() + "_" + baseID, baseBlock.apply(dyeColor));
            blocks.add(block);
        }
        return blocks;
    }

    public static void registerAllItems(SimpleRegistry<Item> deferredRegister, SimpleRegistry<Block> blocks, SimpleHolder<? extends Block>... exceptions) {
        for (SimpleHolder<? extends Block> block : blocks.getEntriesView().stream().filter(block -> !SimpleUtils.toList(exceptions).contains(block)).toList()) {
            deferredRegister.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
        }
    }

    public static Block[] blocksForBlockEntities(List<SimpleHolder<Block>> blocks, SimpleHolder<Block>... extraBlocks) {
        Block[] blockArray = new Block[blocks.size() + extraBlocks.length];
        for (int i = 0; i < blocks.size(); i++) {
            SimpleHolder<Block> block = blocks.get(i);
            blockArray[i] = block.get();
        }
        for (int i = 0; i < extraBlocks.length; i++) {
            SimpleHolder<Block> block = extraBlocks[i];
            blockArray[i] = block.get();
        }
        return blockArray;
    }

    // Block Helpers

    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityTypeAtPos, BlockEntityType<E> blockEntityType, BlockEntityTicker<? super E> blockEntityTicker) {
        return blockEntityType == blockEntityTypeAtPos ? (BlockEntityTicker<A>) blockEntityTicker : null;
    }

    // Menu Helpers

    public static ItemStack quickMoveItemStack(AbstractContainerMenu abstractContainerMenu, Player player, int slotIndex, int containerSize) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = abstractContainerMenu.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            if (slotIndex < containerSize) {
                if (!abstractContainerMenu.moveItemStackTo(slotStack, containerSize, containerSize + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!abstractContainerMenu.moveItemStackTo(slotStack, 0, containerSize, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return itemstack;
    }

    // Generic Helpers

    public static <T> List<T> toList(T[] array) {
        return List.of(array);
    }

    public static List<Float> toList(float[] array) {
        ArrayList<Float> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        return list;
    }

    public static List<Integer> toList(int[] array) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        return list;
    }

    // Internal Use

    public static String prefixNamespace(ResourceLocation resourceLocation) {
        return resourceLocation.getNamespace().equals("minecraft") ? resourceLocation.getPath() : resourceLocation.getNamespace() + "/" + resourceLocation.getPath();
    }

    public static final String autoName(String internalName) {
        return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }

    // Used to setup a basic Utilities class

    public static ResourceLocation resource(String modId, String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, path);
    }

    public static MutableComponent translation(String modId, String type, String name) {
        return Component.translatable(type + "." + modId + "." + name);
    }

    public static MutableComponent translation(String modId, String type, String name, Object... args) {
        return Component.translatable(type + "." + modId + "." + name, args);
    }

    // {@Link net.minecraft.world.level.block.Blocks} public versions of the various methods in the Blocks class.

    public static ToIntFunction<BlockState> litBlockEmission(int lightLevel) {
        return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightLevel : 0;
    }

    public static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    public static Boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return true;
    }

    public static Boolean ocelotOrParrot(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return (entityType == EntityType.OCELOT || entityType == EntityType.PARROT);
    }

    public static boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    public static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }
}
