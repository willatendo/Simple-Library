package willatendo.simplelibrary.server.util;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.compress.utils.Lists;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import willatendo.simplelibrary.server.item.SuppliedBlockItem;

/*
 * Holds all the little utilities a mod needs for a fabric engine.
 * 
 * @author Willatendo
 */
public class SimpleFabricUtils {
	/*
	 * Used to register all colours of a block, like wools
	 * 
	 * @param modid Your mod's modid
	 * 
	 * @param baseID The block's "base" id, eg wool's is "wool"
	 * 
	 * @param baseBlock Your block's supplier, which you can use a {@link DyeColor} to set separate {@link MapColor}
	 * 
	 * @return a list of {@link RegistryObject} of 16 blocks
	 */
	public static <T extends Block> List<T> registerDyedBlocks(String modid, String baseID, Function<DyeColor, T> baseBlock) {
		List<T> blocks = Lists.newArrayList();
		for (DyeColor dyeColor : DyeColor.values()) {
			Registry.register(BuiltInRegistries.BLOCK, SimpleUtils.resource(modid, dyeColor.getName() + "_" + baseID), baseBlock.apply(dyeColor));
			blocks.add(baseBlock.apply(dyeColor));
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
	public static void registerAllItems(String modid, Block... exceptions) {
		List<Block> blocks = BuiltInRegistries.BLOCK.stream().filter(block -> modid.equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace())).toList();
		for (Block block : blocks.stream().filter(block -> !SimpleUtils.toList(exceptions).contains(block)).toList()) {
			Registry.register(BuiltInRegistries.ITEM, SimpleUtils.resource(modid, BuiltInRegistries.BLOCK.getKey(block).getPath()), new SuppliedBlockItem(() -> block, new Item.Properties()));
		}
	}
}
