package willatendo.simplelibrary.server.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType.ExtendedFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import willatendo.simplelibrary.server.registry.SimpleHolder;
import willatendo.simplelibrary.server.registry.SimpleRegistry;

public final class SimpleUtils {
	public static final String ID = "simplelibrary";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static <T> TagRegister<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
		return new TagRegister<>(resourceKey, modId);
	}

	public static <T extends Block> List<SimpleHolder<T>> registerDyedBlocks(SimpleRegistry<T> simpleRegistry, String baseID, Function<DyeColor, Supplier<T>> baseBlock) {
		List<SimpleHolder<T>> blocks = Lists.newArrayList();
		for (DyeColor dyeColor : DyeColor.values()) {
			SimpleHolder<T> block = simpleRegistry.register(dyeColor.getName() + "_" + baseID, baseBlock.apply(dyeColor));
			blocks.add(block);
		}
		return blocks;
	}

	public static void registerAllItems(SimpleRegistry<Item> deferredRegister, SimpleRegistry<Block> blocks, SimpleHolder<? extends Block>... exceptions) {
		for (SimpleHolder<? extends Block> block : blocks.getEntries().stream().filter(block -> !SimpleUtils.toList(exceptions).contains(block)).toList()) {
			deferredRegister.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
		}
	}

	public static Block[] blocksForBlockEntities(List<SimpleHolder<Block>> blocks, SimpleHolder<Block>... extraBlocks) {
		Block[] blockArray = new Block[blocks.size() + extraBlocks.length];
		for (int i = 0; i < blocks.size(); i++) {
			SimpleHolder<Block> block = blocks.get(i);
			blockArray[i] = block.get();
		}
		for (int i = 0; i < extraBlocks.length; i++) {
			SimpleHolder<Block> block = extraBlocks[i];
			blockArray[i] = block.get();
		}
		return blockArray;
	}

	public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedFactory<T> extendedFactory) {
		return new ExtendedScreenHandlerType<T>(extendedFactory);
	}

	public static <T> List<T> toList(T[] array) {
		List<T> stuff = Lists.newArrayList();
		for (int i = 0; i < array.length; i++) {
			stuff.add(array[i]);
		}
		return stuff;
	}

	public static List<Float> toList(float[] array) {
		List<Float> stuff = Lists.newArrayList();
		for (int i = 0; i < array.length; i++) {
			stuff.add(array[i]);
		}
		return stuff;
	}

	public static List<Integer> toList(int[] array) {
		List<Integer> stuff = Lists.newArrayList();
		for (int i = 0; i < array.length; i++) {
			stuff.add(array[i]);
		}
		return stuff;
	}

	public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityTypeAtPos, BlockEntityType<E> blockEntityType, BlockEntityTicker<? super E> blockEntityTicker) {
		return blockEntityType == blockEntityTypeAtPos ? (BlockEntityTicker<A>) blockEntityTicker : null;
	}

	public static final String autoName(String internalName) {
		return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
	}

	public static ResourceLocation resource(String modId, String path) {
		return new ResourceLocation(modId, path);
	}

	public static MutableComponent translation(String modId, String type, String name) {
		return Component.translatable(type + "." + modId + "." + name);
	}

	public static MutableComponent translation(String modId, String type, String name, Object... args) {
		return Component.translatable(type + "." + modId + "." + name, args);
	}

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