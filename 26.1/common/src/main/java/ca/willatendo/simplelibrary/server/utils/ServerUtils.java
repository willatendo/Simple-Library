package ca.willatendo.simplelibrary.server.utils;

import ca.willatendo.simplelibrary.core.registry.SimpleRegistryBuilder;
import ca.willatendo.simplelibrary.platform.SimpleLibraryPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class ServerUtils {
    private ServerUtils() {
    }

    public static RecipeBookType getRecipeBookType(String modId, String name) {
        return SimpleLibraryPlatformHelper.INSTANCE.getRecipeBookType(modId, name);
    }

    public static <T extends Entity> EntityType.Builder<T> simpleEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory mobCategory, float width, float height) {
        return EntityType.Builder.of(entityFactory, mobCategory).sized(width, height);
    }

    public static <T> Registry<T> createRegistry(ResourceKey<Registry<T>> resourceKey, SimpleRegistryBuilder simpleRegistryBuilder) {
        return SimpleLibraryPlatformHelper.INSTANCE.createRegistry(resourceKey, simpleRegistryBuilder);
    }

    public static void openContainer(MenuProvider menuProvider, BlockPos blockPos, ServerPlayer serverPlayer) {
        SimpleLibraryPlatformHelper.INSTANCE.openContainer(menuProvider, blockPos, serverPlayer);
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
}
