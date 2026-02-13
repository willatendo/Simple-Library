package ca.willatendo.simplelibrary;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.sub.BlockSubRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.CreativeModeTabSubRegistry;
import ca.willatendo.simplelibrary.core.registry.sub.ItemSubRegistry;
import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import ca.willatendo.simplelibrary.platform.utils.PlatformUtils;
import ca.willatendo.simplelibrary.server.ModInit;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class SimpleLibraryMod {
    public static void modInit(ModInit modInit) {
        if (PlatformUtils.isDevelopmentalEnvironment()) {
            BlockSubRegistry blockSubRegistry = new BlockSubRegistry(SimpleCoreUtils.ID);
            SimpleHolder<Block> simpleBlock = blockSubRegistry.registerBlock("simple_block", BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK));
            ItemSubRegistry itemSubRegistry = new ItemSubRegistry(SimpleCoreUtils.ID);
            SimpleHolder<Item> simpleItem = itemSubRegistry.registerItem("simple_item");
            SimpleHolder<BlockItem> simpleBlockItem = itemSubRegistry.registerBlock(simpleBlock);
            CreativeModeTabSubRegistry creativeModeTabSubRegistry = new CreativeModeTabSubRegistry(SimpleCoreUtils.ID);
            creativeModeTabSubRegistry.register("simple_creative_mode_tab", () -> new ItemStack(Items.FURNACE), (itemDisplayParameters, output) -> {
                output.accept(simpleItem.get());
                output.accept(simpleBlockItem.get());
            });
            modInit.register(blockSubRegistry, itemSubRegistry, creativeModeTabSubRegistry);
        }
    }
}
