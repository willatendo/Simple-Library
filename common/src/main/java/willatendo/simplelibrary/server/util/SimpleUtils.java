package willatendo.simplelibrary.server.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import willatendo.simplelibrary.platform.ModloaderHelper;
import willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;

import java.util.Arrays;
import java.util.Locale;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public final class SimpleUtils {
    public static final String SIMPLE_ID = "simplelibrary";
    public static final String MINECRAFT_ID = "minecraft";

    public static final Logger SIMPLE_LOGGER = LoggerFactory.getLogger(SimpleUtils.SIMPLE_ID);

    private SimpleUtils() {
    }

    public static ResourceLocation simple(String path) {
        return SimpleUtils.resource(SIMPLE_ID, path);
    }

    public static ResourceLocation mc(String path) {
        return SimpleUtils.resource(MINECRAFT_ID, path);
    }

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

    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityTypeAtPos, BlockEntityType<E> blockEntityType, BlockEntityTicker<? super E> blockEntityTicker) {
        return blockEntityType == blockEntityTypeAtPos ? (BlockEntityTicker<A>) blockEntityTicker : null;
    }

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

    public static String simpleAutoName(String internalName) {
        return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }

    public static ResourceLocation resource(String modId, String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, path);
    }

    public static MutableComponent translation(String modId, String type, String name) {
        return Component.translatable(type + "." + modId + "." + name);
    }

    public static MutableComponent translation(String modId, String type, String name, Object... args) {
        return Component.translatable(type + "." + modId + "." + name, args);
    }
}
