package willatendo.simplelibrary.server.util;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.compress.utils.Lists;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import willatendo.simplelibrary.server.item.SuppliedBlockItem;

/*
 * Holds all the little utilities a mod needs for a forge engine.
 * 
 * @author Willatendo
 */
public final class SimpleForgeUtils {
	/*
	 * Used to register all of your {@link DeferredRegister} in a simple way.
	 * 
	 * @param iEventBus The event bus used by your mod
	 * 
	 * @param deferredRegisters All @link DeferredRegister to register
	 */
	public static void registerAll(IEventBus iEventBus, DeferredRegister<?>... deferredRegisters) {
		for (DeferredRegister<?> deferredRegister : deferredRegisters) {
			deferredRegister.register(iEventBus);
		}
	}

	/*
	 * Used to register all colours of a block, like wools
	 * 
	 * @param deferredRegister Your mod's {@link DeferredRegister} for blocks
	 * 
	 * @param baseID The block's "base" id, eg wool's is "wool"
	 * 
	 * @param baseBlock Your block's supplier, which you can use a {@link DyeColor} to set separate {@link MapColor}
	 * 
	 * @return a list of {@link RegistryObject} of 16 blocks
	 */
	public static <T extends Block> List<RegistryObject<T>> registerDyedBlocks(DeferredRegister<T> deferredRegister, String baseID, Function<DyeColor, Supplier<T>> baseBlock) {
		List<RegistryObject<T>> blocks = Lists.newArrayList();
		for (DyeColor dyeColor : DyeColor.values()) {
			RegistryObject<T> block = deferredRegister.register(dyeColor.getName() + "_" + baseID, baseBlock.apply(dyeColor));
			blocks.add(block);
		}
		return blocks;
	}

	/*
	 * Used to register all items for all of you blocks blocks, with exceptions
	 * 
	 * @param deferredRegister Your mod's {@link DeferredRegister} for items
	 * 
	 * @param blocks Your mod's {@link DeferredRegister} for blocks
	 * 
	 * @param exceptions The blocks you don't want to register an item for
	 */
	public static void registerAllItems(DeferredRegister<Item> deferredRegister, DeferredRegister<Block> blocks, RegistryObject<Block>... exceptions) {
		for (RegistryObject<Block> block : blocks.getEntries().stream().filter(block -> !SimpleUtils.toList(exceptions).contains(block)).toList()) {
			deferredRegister.register(block.getId().getPath(), () -> new SuppliedBlockItem(block, new Item.Properties()));
		}
	}
}
