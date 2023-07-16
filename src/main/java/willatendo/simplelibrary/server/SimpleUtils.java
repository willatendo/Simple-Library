package willatendo.simplelibrary.server;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/*
 * Holds all the little utilities a mod needs.
 * 
 * @author Willatendo
 */
public final class SimpleUtils {
	/*
	 * Used to create a {@link TagRegister}, which registers {@link TagKey}
	 * 
	 * @param resourceKey The {@link ResourceKey} you wish to make tags for
	 * 
	 * @param modId Your mod's id
	 * 
	 * @return a new {@link TagRegister}
	 */
	public static <T> TagRegister<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
		return new TagRegister<>(resourceKey, modId);
	}

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
	public static <T extends Block> List<RegistryObject<T>> registerDyedBlocks(DeferredRegister<Block> deferredRegister, String baseID, Function<DyeColor, Supplier<T>> baseBlock) {
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
		for (RegistryObject<Block> block : blocks.getEntries()) {
			if (exceptions.length > 0) {
				for (RegistryObject<Block> exception : exceptions) {
					if (block != exception) {
						deferredRegister.register(block.getId().getPath(), () -> new SuppliedBlockItem(block, new Item.Properties()));
					}
				}
			} else {
				deferredRegister.register(block.getId().getPath(), () -> new SuppliedBlockItem(block, new Item.Properties()));
			}
		}
	}

	/*
	 * Used to fill a creative tab with all your mods items
	 * 
	 * @param deferredRegister Your mod's {@link DeferredRegister} for items
	 * 
	 * @param itemDisplayParameters The (@link CreativeModeTab.ItemDisplayParameters) provided for by the {@link CreativeModeTab.Builder}'s displayItems
	 * 
	 * @param output The (@link CreativeModeTab.Output) provided for by the {@link CreativeModeTab.Builder}'s displayItems
	 */
	public static void fillCreativeTab(DeferredRegister<Item> items, CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
		for (RegistryObject<Item> item : items.getEntries()) {
			if (item.get() instanceof FillCreativeTab fillCreativeTab) {
				fillCreativeTab.fillCreativeTab(itemDisplayParameters, output);
			} else {
				output.accept(item.get());
			}
		}
	}

	/*
	 * Used to create a simple creative tab
	 * 
	 * @param modId Your mod's id
	 * 
	 * @param id The tab's id
	 * 
	 * @param icon The (@link Item) to be used as the icon
	 * 
	 * @param displayItemsGenerator The items to be displayed in the creative mode tab
	 */
	public static CreativeModeTab.Builder create(String modId, String id, Supplier<Item> icon, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
		return CreativeModeTab.builder().title(translation(modId, "itemGroup", id)).icon(() -> icon.get().getDefaultInstance()).displayItems(displayItemsGenerator);
	}

	/*
	 * Used to provide a list for a {@link BlockEntity}
	 * 
	 * @param blocks The {@link List} of blocks to turn into an array for a {@link BlockEntity}
	 * 
	 * @param extraBlocks Extra blocks to add to the array.
	 * 
	 * @return a {@link Block} Array of all the blocks provided
	 */
	public static Block[] blocksForBlockEntities(List<RegistryObject<Block>> blocks, RegistryObject<Block>... extraBlocks) {
		Block[] blockArray = new Block[blocks.size()];
		for (int i = 0; i < blocks.size(); i++) {
			RegistryObject<Block> block = blocks.get(i);
			blockArray[i] = block.get();
		}
		for (int i = 0; i < extraBlocks.length; i++) {
			RegistryObject<Block> block = extraBlocks[i];
			blockArray[i] = block.get();
		}
		return blockArray;
	}

	/*
	 * Used to turn an id to a readable name
	 * 
	 * @param internalName The {@link String} that is an id to turn into a name
	 * 
	 * @return a {@link String} of the name
	 */
	public static final String autoName(String internalName) {
		return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
	}

	/*
	 * Used to turn an id to a readable name
	 * 
	 * @param modId Your mod's id
	 * 
	 * @param path The path
	 * 
	 * @return a {@link ResourceLocation} for the mod
	 */
	public static ResourceLocation resource(String modId, String path) {
		return new ResourceLocation(modId, path);
	}

	/*
	 * Used to turn an id to a readable name
	 * 
	 * @param modId Your mod's id
	 * 
	 * @param type The type of the name
	 * 
	 * @param name The name
	 * 
	 * @return a {@link ResourceLocation} for the mod
	 */
	public static MutableComponent translation(String modId, String type, String name) {
		return Component.translatable(type + "." + modId + "." + name);
	}

	/*
	 * Used to turn an id to a readable name
	 * 
	 * @param modId Your mod's id
	 * 
	 * @param type The type of the name
	 * 
	 * @param name The name
	 * 
	 * @param args Arguments to add
	 * 
	 * @return a {@link ResourceLocation} for the mod
	 */
	public static MutableComponent translation(String modId, String type, String name, Object... args) {
		return Component.translatable(type + "." + modId + "." + name, args);
	}

	// @Link net.minecraft.world.level.block.Blocks functions at the bottom public

	public static ToIntFunction<BlockState> litBlockEmission(int lightLevel) {
		return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightLevel : 0;
	}

	public static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
		return (boolean) false;
	}

	public static Boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
		return (boolean) true;
	}

	public static Boolean ocelotOrParrot(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
		return (boolean) (entityType == EntityType.OCELOT || entityType == EntityType.PARROT);
	}

	public static boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return true;
	}

	public static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return false;
	}
}