package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import ca.willatendo.simplelibrary.platform.SimpleLibraryPlatformHelper;
import ca.willatendo.simplelibrary.server.item.SimpleCreativeModeTabBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeModeTabSubRegistry extends SimpleRegistry<CreativeModeTab> {
    public CreativeModeTabSubRegistry(String modId) {
        super(Registries.CREATIVE_MODE_TAB, modId);
    }

    public SimpleHolder<CreativeModeTab> register(String name, Supplier<ItemStack> itemStackIcon, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
        return this.register(name, name, itemStackIcon, displayItemsGenerator, simpleCreativeModeTabBuilder -> {
        });
    }

    public SimpleHolder<CreativeModeTab> register(String name, Supplier<ItemStack> itemStackIcon, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator, Consumer<SimpleCreativeModeTabBuilder> consumer) {
        return this.register(name, name, itemStackIcon, displayItemsGenerator, consumer);
    }

    public SimpleHolder<CreativeModeTab> register(String name, String tabName, Supplier<ItemStack> itemStackIcon, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator, Consumer<SimpleCreativeModeTabBuilder> consumer) {
        SimpleCreativeModeTabBuilder simpleCreativeModeTabBuilder = new SimpleCreativeModeTabBuilder();
        consumer.accept(simpleCreativeModeTabBuilder);
        CreativeModeTab.Builder builder = SimpleLibraryPlatformHelper.INSTANCE.createCreativeModeTab(simpleCreativeModeTabBuilder).title(CoreUtils.translation("itemGroup", this.modId, tabName)).icon(itemStackIcon).displayItems(displayItemsGenerator);
        return this.register(name, builder::build);
    }
}
