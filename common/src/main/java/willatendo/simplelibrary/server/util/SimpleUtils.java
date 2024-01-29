package willatendo.simplelibrary.server.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ServiceLoader;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class SimpleUtils {
	public static final String SIMPLE_ID = "simplelibrary";
	public static final Logger LOGGER = LoggerFactory.getLogger(SIMPLE_ID);

	public static ResourceLocation simpleResource(String path) {
		return new ResourceLocation(SIMPLE_ID, path);
	}

	// Multi-Platform Helpers

	public static <T> T loadModloaderHelper(Class<T> clazz) {
		final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
		LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
		return loadedService;
	}

//	public static <T> TagRegister<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
//		return new TagRegister<>(resourceKey, modId);
//	}
//
//	public static <T extends Block> List<SimpleHolder<T>> registerDyedBlocks(SimpleRegistry<T> simpleRegistry, String baseID, Function<DyeColor, Supplier<T>> baseBlock) {
//		List<SimpleHolder<T>> blocks = Lists.newArrayList();
//		for (DyeColor dyeColor : DyeColor.values()) {
//			SimpleHolder<T> block = simpleRegistry.register(dyeColor.getName() + "_" + baseID, baseBlock.apply(dyeColor));
//			blocks.add(block);
//		}
//		return blocks;
//	}
//
//	public static void registerAllItems(SimpleRegistry<Item> deferredRegister, SimpleRegistry<Block> blocks, SimpleHolder<? extends Block>... exceptions) {
//		for (SimpleHolder<? extends Block> block : blocks.getEntries().stream().filter(block -> !SimpleUtils.toList(exceptions).contains(block)).toList()) {
//			deferredRegister.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
//		}
//	}
//
//	public static Block[] blocksForBlockEntities(List<SimpleHolder<Block>> blocks, SimpleHolder<Block>... extraBlocks) {
//		Block[] blockArray = new Block[blocks.size() + extraBlocks.length];
//		for (int i = 0; i < blocks.size(); i++) {
//			SimpleHolder<Block> block = blocks.get(i);
//			blockArray[i] = block.get();
//		}
//		for (int i = 0; i < extraBlocks.length; i++) {
//			SimpleHolder<Block> block = extraBlocks[i];
//			blockArray[i] = block.get();
//		}
//		return blockArray;
//	}
//
//	public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedFactory<T> extendedFactory) {
//		return new ExtendedScreenHandlerType<T>(extendedFactory);
//	}

	// Block Helpers

	public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityTypeAtPos, BlockEntityType<E> blockEntityType, BlockEntityTicker<? super E> blockEntityTicker) {
		return blockEntityType == blockEntityTypeAtPos ? (BlockEntityTicker<A>) blockEntityTicker : null;
	}

	// Generic Utilities

	public static <T> List<T> toList(T[] array) {
		ArrayList<T> list = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	public static List<Float> toList(float[] array) {
		ArrayList<Float> list = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	public static List<Integer> toList(int[] array) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	// Internal Use

	public static String prefixNamespace(ResourceLocation resourceLocation) {
		return resourceLocation.getNamespace().equals("minecraft") ? resourceLocation.getPath() : resourceLocation.getNamespace() + "/" + resourceLocation.getPath();
	}

	public static final String autoName(String internalName) {
		return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
	}

	// Used to setup a basic Utilities class

	public static ResourceLocation resource(String modId, String path) {
		return new ResourceLocation(modId, path);
	}

	public static MutableComponent translation(String modId, String type, String name) {
		return Component.translatable(type + "." + modId + "." + name);
	}

	public static MutableComponent translation(String modId, String type, String name, Object... args) {
		return Component.translatable(type + "." + modId + "." + name, args);
	}

	// {@Link net.minecraft.world.level.block.Blocks} public versions of the various methods in the Blocks class.

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