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

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
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
import willatendo.simplelibrary.server.item.SuppliedBlockItem;
import willatendo.simplelibrary.server.registry.RegistryHolder;
import willatendo.simplelibrary.server.registry.SimpleRegistry;

public final class SimpleUtils {
	public static final String ID = "simplelibrary";

	public static <T> TagRegister<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
		return new TagRegister<>(resourceKey, modId);
	}

	public static void fillCreativeTab(SimpleRegistry<Item> simpleRegister, CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
		for (RegistryHolder<? extends Item> item : simpleRegister.getEntries()) {
			if (item.get() instanceof FillCreativeTab fillCreativeTab) {
				fillCreativeTab.fillCreativeTab(itemDisplayParameters, output);
			} else {
				output.accept(item.get());
			}
		}
	}

	public static <T extends Block> List<RegistryHolder<T>> registerDyedBlocks(SimpleRegistry<T> simpleRegistry, String baseID, Function<DyeColor, Supplier<T>> baseBlock) {
		List<RegistryHolder<T>> blocks = Lists.newArrayList();
		for (DyeColor dyeColor : DyeColor.values()) {
			RegistryHolder<T> block = simpleRegistry.register(dyeColor.getName() + "_" + baseID, baseBlock.apply(dyeColor));
			blocks.add(block);
		}
		return blocks;
	}

	public static void registerAllItems(SimpleRegistry<Item> deferredRegister, SimpleRegistry<Block> blocks, RegistryHolder<Block>... exceptions) {
		for (RegistryHolder<? extends Block> block : blocks.getEntries().stream().filter(block -> !SimpleUtils.toList(exceptions).contains(block)).toList()) {
			deferredRegister.register(block.getId().getPath(), () -> new SuppliedBlockItem(() -> block.get(), new Item.Properties()));
		}
	}

	public static CreativeModeTab.Builder create(String modId, String id, Supplier<Item> icon, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
		return FabricItemGroup.builder().title(translation(modId, "itemGroup", id)).icon(() -> icon.get().getDefaultInstance()).displayItems(displayItemsGenerator);
	}

	public static Block[] blocksForBlockEntities(List<RegistryHolder<Block>> blocks, RegistryHolder<Block>... extraBlocks) {
		Block[] blockArray = new Block[blocks.size() + extraBlocks.length];
		for (int i = 0; i < blocks.size(); i++) {
			RegistryHolder<Block> block = blocks.get(i);
			blockArray[i] = block.get();
		}
		for (int i = 0; i < extraBlocks.length; i++) {
			RegistryHolder<Block> block = extraBlocks[i];
			blockArray[i] = block.get();
		}
		return blockArray;
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