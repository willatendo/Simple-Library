package willatendo.simplelibrary.data.impl;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import willatendo.simplelibrary.data.tags.SimpleBlockTagsProvider;
import willatendo.simplelibrary.data.util.ExistingFileHelper;
import willatendo.simplelibrary.server.block.ForgeBlockTags;

public class InternalBlockTagsProvider extends SimpleBlockTagsProvider {
	public InternalBlockTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, provider, modId, existingFileHelper);
	}

	@Override
	protected void addTags(Provider provider) {
		this.tag(ForgeBlockTags.BARRELS).addTag(ForgeBlockTags.BARRELS_WOODEN);
		this.tag(ForgeBlockTags.BARRELS_WOODEN).add(Blocks.BARREL);
		this.tag(ForgeBlockTags.BOOKSHELVES).add(Blocks.BOOKSHELF);
		this.tag(ForgeBlockTags.CHESTS).addTags(ForgeBlockTags.CHESTS_ENDER, ForgeBlockTags.CHESTS_TRAPPED, ForgeBlockTags.CHESTS_WOODEN);
		this.tag(ForgeBlockTags.CHESTS_ENDER).add(Blocks.ENDER_CHEST);
		this.tag(ForgeBlockTags.CHESTS_TRAPPED).add(Blocks.TRAPPED_CHEST);
		this.tag(ForgeBlockTags.CHESTS_WOODEN).add(Blocks.CHEST, Blocks.TRAPPED_CHEST);
		this.tag(ForgeBlockTags.COBBLESTONE).addTags(ForgeBlockTags.COBBLESTONE_NORMAL, ForgeBlockTags.COBBLESTONE_INFESTED, ForgeBlockTags.COBBLESTONE_MOSSY, ForgeBlockTags.COBBLESTONE_DEEPSLATE);
		this.tag(ForgeBlockTags.COBBLESTONE_NORMAL).add(Blocks.COBBLESTONE);
		this.tag(ForgeBlockTags.COBBLESTONE_INFESTED).add(Blocks.INFESTED_COBBLESTONE);
		this.tag(ForgeBlockTags.COBBLESTONE_MOSSY).add(Blocks.MOSSY_COBBLESTONE);
		this.tag(ForgeBlockTags.COBBLESTONE_DEEPSLATE).add(Blocks.COBBLED_DEEPSLATE);
		this.tag(ForgeBlockTags.END_STONES).add(Blocks.END_STONE);
		this.tag(ForgeBlockTags.ENDERMAN_PLACE_ON_BLACKLIST);
		this.tag(ForgeBlockTags.FENCE_GATES).addTags(ForgeBlockTags.FENCE_GATES_WOODEN);
		this.tag(ForgeBlockTags.FENCE_GATES_WOODEN).add(Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE, Blocks.WARPED_FENCE_GATE, Blocks.MANGROVE_FENCE_GATE, Blocks.BAMBOO_FENCE_GATE, Blocks.CHERRY_FENCE_GATE);
		this.tag(ForgeBlockTags.FENCES).addTags(ForgeBlockTags.FENCES_NETHER_BRICK, ForgeBlockTags.FENCES_WOODEN);
		this.tag(ForgeBlockTags.FENCES_NETHER_BRICK).add(Blocks.NETHER_BRICK_FENCE);
		this.tag(ForgeBlockTags.FENCES_WOODEN).addTag(BlockTags.WOODEN_FENCES);
		this.tag(ForgeBlockTags.GLASS).addTags(ForgeBlockTags.GLASS_COLORLESS, ForgeBlockTags.STAINED_GLASS, ForgeBlockTags.GLASS_TINTED);
		this.tag(ForgeBlockTags.GLASS_COLORLESS).add(Blocks.GLASS);
		this.tag(ForgeBlockTags.GLASS_SILICA).add(Blocks.GLASS, Blocks.BLACK_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.PINK_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.WHITE_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS);
		this.tag(ForgeBlockTags.GLASS_TINTED).add(Blocks.TINTED_GLASS);
		this.addColored(this.tag(ForgeBlockTags.STAINED_GLASS)::add, ForgeBlockTags.GLASS, "{color}_stained_glass");
		this.tag(ForgeBlockTags.GLASS_PANES).addTags(ForgeBlockTags.GLASS_PANES_COLORLESS, ForgeBlockTags.STAINED_GLASS_PANES);
		this.tag(ForgeBlockTags.GLASS_PANES_COLORLESS).add(Blocks.GLASS_PANE);
		this.addColored(this.tag(ForgeBlockTags.STAINED_GLASS_PANES)::add, ForgeBlockTags.GLASS_PANES, "{color}_stained_glass_pane");
		this.tag(ForgeBlockTags.GRAVEL).add(Blocks.GRAVEL);
		this.tag(ForgeBlockTags.NETHERRACK).add(Blocks.NETHERRACK);
		this.tag(ForgeBlockTags.OBSIDIAN).add(Blocks.OBSIDIAN);
		this.tag(ForgeBlockTags.ORE_BEARING_GROUND_DEEPSLATE).add(Blocks.DEEPSLATE);
		this.tag(ForgeBlockTags.ORE_BEARING_GROUND_NETHERRACK).add(Blocks.NETHERRACK);
		this.tag(ForgeBlockTags.ORE_BEARING_GROUND_STONE).add(Blocks.STONE);
		this.tag(ForgeBlockTags.ORE_RATES_DENSE).add(Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE);
		this.tag(ForgeBlockTags.ORE_RATES_SINGULAR).add(Blocks.ANCIENT_DEBRIS, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.IRON_ORE, Blocks.NETHER_QUARTZ_ORE);
		this.tag(ForgeBlockTags.ORE_RATES_SPARSE).add(Blocks.NETHER_GOLD_ORE);
		this.tag(ForgeBlockTags.ORES).addTags(ForgeBlockTags.ORES_COAL, ForgeBlockTags.ORES_COPPER, ForgeBlockTags.ORES_DIAMOND, ForgeBlockTags.ORES_EMERALD, ForgeBlockTags.ORES_GOLD, ForgeBlockTags.ORES_IRON, ForgeBlockTags.ORES_LAPIS, ForgeBlockTags.ORES_REDSTONE, ForgeBlockTags.ORES_QUARTZ, ForgeBlockTags.ORES_NETHERITE_SCRAP);
		this.tag(ForgeBlockTags.ORES_COAL).addTag(BlockTags.COAL_ORES);
		this.tag(ForgeBlockTags.ORES_COPPER).addTag(BlockTags.COPPER_ORES);
		this.tag(ForgeBlockTags.ORES_DIAMOND).addTag(BlockTags.DIAMOND_ORES);
		this.tag(ForgeBlockTags.ORES_EMERALD).addTag(BlockTags.EMERALD_ORES);
		this.tag(ForgeBlockTags.ORES_GOLD).addTag(BlockTags.GOLD_ORES);
		this.tag(ForgeBlockTags.ORES_IRON).addTag(BlockTags.IRON_ORES);
		this.tag(ForgeBlockTags.ORES_LAPIS).addTag(BlockTags.LAPIS_ORES);
		this.tag(ForgeBlockTags.ORES_QUARTZ).add(Blocks.NETHER_QUARTZ_ORE);
		this.tag(ForgeBlockTags.ORES_REDSTONE).addTag(BlockTags.REDSTONE_ORES);
		this.tag(ForgeBlockTags.ORES_NETHERITE_SCRAP).add(Blocks.ANCIENT_DEBRIS);
		this.tag(ForgeBlockTags.ORES_IN_GROUND_DEEPSLATE).add(Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE);
		this.tag(ForgeBlockTags.ORES_IN_GROUND_NETHERRACK).add(Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE);
		this.tag(ForgeBlockTags.ORES_IN_GROUND_STONE).add(Blocks.COAL_ORE, Blocks.COPPER_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.IRON_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE);
		this.tag(ForgeBlockTags.SAND).addTags(ForgeBlockTags.SAND_COLORLESS, ForgeBlockTags.SAND_RED);
		this.tag(ForgeBlockTags.SAND_COLORLESS).add(Blocks.SAND);
		this.tag(ForgeBlockTags.SAND_RED).add(Blocks.RED_SAND);
		this.tag(ForgeBlockTags.SANDSTONE).add(Blocks.SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.SMOOTH_SANDSTONE, Blocks.RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE);
		this.tag(ForgeBlockTags.STONE).add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.INFESTED_STONE, Blocks.STONE, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_DIORITE, Blocks.POLISHED_GRANITE, Blocks.DEEPSLATE, Blocks.POLISHED_DEEPSLATE, Blocks.INFESTED_DEEPSLATE, Blocks.TUFF);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS).addTags(ForgeBlockTags.STORAGE_BLOCKS_AMETHYST, ForgeBlockTags.STORAGE_BLOCKS_COAL, ForgeBlockTags.STORAGE_BLOCKS_COPPER, ForgeBlockTags.STORAGE_BLOCKS_DIAMOND, ForgeBlockTags.STORAGE_BLOCKS_EMERALD, ForgeBlockTags.STORAGE_BLOCKS_GOLD, ForgeBlockTags.STORAGE_BLOCKS_IRON, ForgeBlockTags.STORAGE_BLOCKS_LAPIS, ForgeBlockTags.STORAGE_BLOCKS_QUARTZ, ForgeBlockTags.STORAGE_BLOCKS_RAW_COPPER, ForgeBlockTags.STORAGE_BLOCKS_RAW_GOLD, ForgeBlockTags.STORAGE_BLOCKS_RAW_IRON, ForgeBlockTags.STORAGE_BLOCKS_REDSTONE, ForgeBlockTags.STORAGE_BLOCKS_NETHERITE);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_AMETHYST).add(Blocks.AMETHYST_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_COAL).add(Blocks.COAL_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_COPPER).add(Blocks.COPPER_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_DIAMOND).add(Blocks.DIAMOND_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_EMERALD).add(Blocks.EMERALD_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_GOLD).add(Blocks.GOLD_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_IRON).add(Blocks.IRON_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_LAPIS).add(Blocks.LAPIS_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_QUARTZ).add(Blocks.QUARTZ_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_RAW_COPPER).add(Blocks.RAW_COPPER_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_RAW_GOLD).add(Blocks.RAW_GOLD_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_RAW_IRON).add(Blocks.RAW_IRON_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_REDSTONE).add(Blocks.REDSTONE_BLOCK);
		this.tag(ForgeBlockTags.STORAGE_BLOCKS_NETHERITE).add(Blocks.NETHERITE_BLOCK);
	}

	private void addColored(Consumer<Block> consumer, TagKey<Block> group, String pattern) {
		String prefix = group.location().getPath().toUpperCase(Locale.ENGLISH) + '_';
		for (DyeColor color : DyeColor.values()) {
			ResourceLocation key = new ResourceLocation("minecraft", pattern.replace("{color}", color.getName()));
			TagKey<Block> tag = this.getSimpleTag(prefix + color.getName());
			Block block = BuiltInRegistries.BLOCK.get(key);
			if (block == null || block == Blocks.AIR) {
				throw new IllegalStateException("Unknown vanilla block: " + key.toString());
			}
			this.tag(tag).add(block);
			consumer.accept(block);
		}
	}

	private TagKey<Block> getSimpleTag(String name) {
		try {
			name = name.toUpperCase(Locale.ENGLISH);
			return (TagKey<Block>) ForgeBlockTags.class.getDeclaredField(name).get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new IllegalStateException(ForgeBlockTags.class.getName() + " is missing this.tag name: " + name);
		}
	}
}
