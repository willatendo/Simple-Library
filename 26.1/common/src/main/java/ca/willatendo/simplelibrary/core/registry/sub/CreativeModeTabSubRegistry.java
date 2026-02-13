package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import ca.willatendo.simplelibrary.platform.SimpleLibraryPlatformHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class CreativeModeTabSubRegistry extends SimpleRegistry<CreativeModeTab> {
    public CreativeModeTabSubRegistry(String modId) {
        super(Registries.CREATIVE_MODE_TAB, modId);
    }

    public SimpleHolder<CreativeModeTab> register(String name, Supplier<ItemStack> itemStackIcon, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
        return this.register(name, name, itemStackIcon, displayItemsGenerator);
    }

    public SimpleHolder<CreativeModeTab> register(String name, String tabName, Supplier<ItemStack> itemStackIcon, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
        return this.register(name, () -> SimpleLibraryPlatformHelper.INSTANCE.createCreativeModeTab().title(CoreUtils.translation("itemGroup", this.modId, tabName)).icon(itemStackIcon).displayItems(displayItemsGenerator).build());
    }
}
