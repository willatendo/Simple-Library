package willatendo.simplelibrary.server.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import willatendo.simplelibrary.platform.ModloaderHelper;
import willatendo.simplelibrary.server.util.SimpleUtils;

import java.util.function.Supplier;

public final class CreativeModeTabRegistry extends SimpleRegistry<CreativeModeTab> {
    CreativeModeTabRegistry(String modId) {
        super(Registries.CREATIVE_MODE_TAB, modId);
    }

    public SimpleHolder<CreativeModeTab> register(String id, Supplier<ItemStack> itemStackIcon, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
        return this.register(id, id, itemStackIcon, displayItemsGenerator);
    }

    public SimpleHolder<CreativeModeTab> register(String id, String tabName, Supplier<ItemStack> itemStackIcon, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
        return this.register(id, () -> ModloaderHelper.INSTANCE.createCreativeModeTab().title(SimpleUtils.translation(this.modId, "itemGroup", tabName)).icon(itemStackIcon).displayItems(displayItemsGenerator).build());
    }
}
