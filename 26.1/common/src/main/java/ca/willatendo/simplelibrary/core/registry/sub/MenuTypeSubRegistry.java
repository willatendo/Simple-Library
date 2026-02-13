package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.platform.SimpleLibraryPlatformHelper;
import ca.willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class MenuTypeSubRegistry extends SimpleRegistry<MenuType<?>> {
    public MenuTypeSubRegistry(String modId) {
        super(Registries.MENU, modId);
    }

    public <T extends AbstractContainerMenu> SimpleHolder<MenuType<T>> registerSimple(String name, CreateSimple<T> createSimple) {
        return this.register(name, () -> SimpleLibraryPlatformHelper.INSTANCE.createMenuType(new ExtendedMenuSupplier<T>() {
            @Override
            public T create(int windowId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
                return createSimple.create(windowId, inventory);
            }

            @Override
            public T create(int windowId, Inventory inventory, BlockPos blockPos) {
                return createSimple.create(windowId, inventory);
            }
        }));
    }

    public <T extends AbstractContainerMenu> SimpleHolder<MenuType<T>> registerSimple(String name, CreateFromServer<T> createFromServer, CreateFromBlockPos<T> createFromBlockPos) {
        return this.register(name, () -> SimpleLibraryPlatformHelper.INSTANCE.createMenuType(new ExtendedMenuSupplier<T>() {
            @Override
            public T create(int windowId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
                return createFromServer.create(windowId, inventory, friendlyByteBuf);
            }

            @Override
            public T create(int windowId, Inventory inventory, BlockPos blockPos) {
                return createFromBlockPos.create(windowId, inventory, blockPos);
            }
        }));
    }

    public interface CreateSimple<T extends AbstractContainerMenu> {
        T create(int windowId, Inventory inventory);
    }

    public interface CreateFromServer<T extends AbstractContainerMenu> {
        T create(int windowId, Inventory inventory, FriendlyByteBuf friendlyByteBuf);
    }

    public interface CreateFromBlockPos<T extends AbstractContainerMenu> {
        T create(int windowId, Inventory inventory, BlockPos blockPos);
    }
}
