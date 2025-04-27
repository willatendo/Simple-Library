package willatendo.simplelibrary.server.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import willatendo.simplelibrary.server.util.SimpleUtils;

public final class MenuTypeRegistry extends SimpleRegistry<MenuType<?>> {
    MenuTypeRegistry(String modId) {
        super(Registries.MENU, modId);
    }

    public <T extends AbstractContainerMenu> SimpleHolder<MenuType<T>> registerSimple(String id, CreateSimple<T> createSimple) {
        return this.register(id, () -> SimpleUtils.createMenuType(new ExtendedMenuSupplier<T>() {
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

    public <T extends AbstractContainerMenu> SimpleHolder<MenuType<T>> registerSimple(String id, CreateFromServer<T> createFromServer, CreateFromBlockPos<T> createFromBlockPos) {
        return this.register(id, () -> SimpleUtils.createMenuType(new ExtendedMenuSupplier<T>() {
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
