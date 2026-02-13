package ca.willatendo.simplelibrary.server.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface ExtendedMenuSupplier<T extends AbstractContainerMenu> {
    T create(int windowId, Inventory inventory, FriendlyByteBuf friendlyByteBuf);

    T create(int windowId, Inventory inventory, BlockPos blockPos);
}
