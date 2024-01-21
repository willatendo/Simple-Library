package willatendo.simplelibrary.data.impl;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import willatendo.simplelibrary.data.tags.SimpleItemTagsProvider;
import willatendo.simplelibrary.data.util.ExistingFileHelper;
import willatendo.simplelibrary.server.block.ForgeBlockTags;
import willatendo.simplelibrary.server.item.ForgeItemTags;

public class InternalItemTagsProvider extends SimpleItemTagsProvider {
	public InternalItemTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<Provider> provider, CompletableFuture<TagLookup<Block>> blockTags, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, provider, blockTags, modId, existingFileHelper);
	}

	@Override
	protected void addTags(Provider provider) {
		this.copy(ForgeBlockTags.BARRELS, ForgeItemTags.BARRELS);
		this.copy(ForgeBlockTags.BARRELS_WOODEN, ForgeItemTags.BARRELS_WOODEN);
		this.tag(ForgeItemTags.BONES).add(Items.BONE);
		this.copy(ForgeBlockTags.BOOKSHELVES, ForgeItemTags.BOOKSHELVES);
		this.copy(ForgeBlockTags.CHESTS, ForgeItemTags.CHESTS);
		this.copy(ForgeBlockTags.CHESTS_ENDER, ForgeItemTags.CHESTS_ENDER);
		this.copy(ForgeBlockTags.CHESTS_TRAPPED, ForgeItemTags.CHESTS_TRAPPED);
		this.copy(ForgeBlockTags.CHESTS_WOODEN, ForgeItemTags.CHESTS_WOODEN);
		this.copy(ForgeBlockTags.COBBLESTONE, ForgeItemTags.COBBLESTONE);
		this.copy(ForgeBlockTags.COBBLESTONE_NORMAL, ForgeItemTags.COBBLESTONE_NORMAL);
		this.copy(ForgeBlockTags.COBBLESTONE_INFESTED, ForgeItemTags.COBBLESTONE_INFESTED);
		this.copy(ForgeBlockTags.COBBLESTONE_MOSSY, ForgeItemTags.COBBLESTONE_MOSSY);
		this.copy(ForgeBlockTags.COBBLESTONE_DEEPSLATE, ForgeItemTags.COBBLESTONE_DEEPSLATE);
		this.tag(ForgeItemTags.CROPS).addTags(ForgeItemTags.CROPS_BEETROOT, ForgeItemTags.CROPS_CARROT, ForgeItemTags.CROPS_NETHER_WART, ForgeItemTags.CROPS_POTATO, ForgeItemTags.CROPS_WHEAT);
		this.tag(ForgeItemTags.CROPS_BEETROOT).add(Items.BEETROOT);
		this.tag(ForgeItemTags.CROPS_CARROT).add(Items.CARROT);
		this.tag(ForgeItemTags.CROPS_NETHER_WART).add(Items.NETHER_WART);
		this.tag(ForgeItemTags.CROPS_POTATO).add(Items.POTATO);
		this.tag(ForgeItemTags.CROPS_WHEAT).add(Items.WHEAT);
		this.tag(ForgeItemTags.DUSTS).addTags(ForgeItemTags.DUSTS_GLOWSTONE, ForgeItemTags.DUSTS_PRISMARINE, ForgeItemTags.DUSTS_REDSTONE);
		this.tag(ForgeItemTags.DUSTS_GLOWSTONE).add(Items.GLOWSTONE_DUST);
		this.tag(ForgeItemTags.DUSTS_PRISMARINE).add(Items.PRISMARINE_SHARD);
		this.tag(ForgeItemTags.DUSTS_REDSTONE).add(Items.REDSTONE);
		this.addColored(this.tag(ForgeItemTags.DYES)::addTags, ForgeItemTags.DYES, "{color}_dye");
		this.tag(ForgeItemTags.EGGS).add(Items.EGG);
		this.tag(ForgeItemTags.ENCHANTING_FUELS).addTag(ForgeItemTags.GEMS_LAPIS);
		this.copy(ForgeBlockTags.END_STONES, ForgeItemTags.END_STONES);
		this.tag(ForgeItemTags.ENDER_PEARLS).add(Items.ENDER_PEARL);
		this.tag(ForgeItemTags.FEATHERS).add(Items.FEATHER);
		this.copy(ForgeBlockTags.FENCE_GATES, ForgeItemTags.FENCE_GATES);
		this.copy(ForgeBlockTags.FENCE_GATES_WOODEN, ForgeItemTags.FENCE_GATES_WOODEN);
		this.copy(ForgeBlockTags.FENCES, ForgeItemTags.FENCES);
		this.copy(ForgeBlockTags.FENCES_NETHER_BRICK, ForgeItemTags.FENCES_NETHER_BRICK);
		this.copy(ForgeBlockTags.FENCES_WOODEN, ForgeItemTags.FENCES_WOODEN);
		this.tag(ForgeItemTags.GEMS).addTags(ForgeItemTags.GEMS_AMETHYST, ForgeItemTags.GEMS_DIAMOND, ForgeItemTags.GEMS_EMERALD, ForgeItemTags.GEMS_LAPIS, ForgeItemTags.GEMS_PRISMARINE, ForgeItemTags.GEMS_QUARTZ);
		this.tag(ForgeItemTags.GEMS_AMETHYST).add(Items.AMETHYST_SHARD);
		this.tag(ForgeItemTags.GEMS_DIAMOND).add(Items.DIAMOND);
		this.tag(ForgeItemTags.GEMS_EMERALD).add(Items.EMERALD);
		this.tag(ForgeItemTags.GEMS_LAPIS).add(Items.LAPIS_LAZULI);
		this.tag(ForgeItemTags.GEMS_PRISMARINE).add(Items.PRISMARINE_CRYSTALS);
		this.tag(ForgeItemTags.GEMS_QUARTZ).add(Items.QUARTZ);
		this.copy(ForgeBlockTags.GLASS, ForgeItemTags.GLASS);
		this.copy(ForgeBlockTags.GLASS_TINTED, ForgeItemTags.GLASS_TINTED);
		this.copy(ForgeBlockTags.GLASS_SILICA, ForgeItemTags.GLASS_SILICA);
		this.copyColored(ForgeBlockTags.GLASS, ForgeItemTags.GLASS);
		this.copy(ForgeBlockTags.GLASS_PANES, ForgeItemTags.GLASS_PANES);
		this.copyColored(ForgeBlockTags.GLASS_PANES, ForgeItemTags.GLASS_PANES);
		this.copy(ForgeBlockTags.GRAVEL, ForgeItemTags.GRAVEL);
		this.tag(ForgeItemTags.GUNPOWDER).add(Items.GUNPOWDER);
		this.tag(ForgeItemTags.HEADS).add(Items.CREEPER_HEAD, Items.DRAGON_HEAD, Items.PLAYER_HEAD, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.ZOMBIE_HEAD);
		this.tag(ForgeItemTags.INGOTS).addTags(ForgeItemTags.INGOTS_BRICK, ForgeItemTags.INGOTS_COPPER, ForgeItemTags.INGOTS_GOLD, ForgeItemTags.INGOTS_IRON, ForgeItemTags.INGOTS_NETHERITE, ForgeItemTags.INGOTS_NETHER_BRICK);
		this.tag(ForgeItemTags.INGOTS_BRICK).add(Items.BRICK);
		this.tag(ForgeItemTags.INGOTS_COPPER).add(Items.COPPER_INGOT);
		this.tag(ForgeItemTags.INGOTS_GOLD).add(Items.GOLD_INGOT);
		this.tag(ForgeItemTags.INGOTS_IRON).add(Items.IRON_INGOT);
		this.tag(ForgeItemTags.INGOTS_NETHERITE).add(Items.NETHERITE_INGOT);
		this.tag(ForgeItemTags.INGOTS_NETHER_BRICK).add(Items.NETHER_BRICK);
		this.tag(ForgeItemTags.LEATHER).add(Items.LEATHER);
		this.tag(ForgeItemTags.MUSHROOMS).add(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM);
		this.tag(ForgeItemTags.NETHER_STARS).add(Items.NETHER_STAR);
		this.copy(ForgeBlockTags.NETHERRACK, ForgeItemTags.NETHERRACK);
		this.tag(ForgeItemTags.NUGGETS).addTags(ForgeItemTags.NUGGETS_IRON, ForgeItemTags.NUGGETS_GOLD);
		this.tag(ForgeItemTags.NUGGETS_IRON).add(Items.IRON_NUGGET);
		this.tag(ForgeItemTags.NUGGETS_GOLD).add(Items.GOLD_NUGGET);
		this.copy(ForgeBlockTags.OBSIDIAN, ForgeItemTags.OBSIDIAN);
		this.copy(ForgeBlockTags.ORE_BEARING_GROUND_DEEPSLATE, ForgeItemTags.ORE_BEARING_GROUND_DEEPSLATE);
		this.copy(ForgeBlockTags.ORE_BEARING_GROUND_NETHERRACK, ForgeItemTags.ORE_BEARING_GROUND_NETHERRACK);
		this.copy(ForgeBlockTags.ORE_BEARING_GROUND_STONE, ForgeItemTags.ORE_BEARING_GROUND_STONE);
		this.copy(ForgeBlockTags.ORE_RATES_DENSE, ForgeItemTags.ORE_RATES_DENSE);
		this.copy(ForgeBlockTags.ORE_RATES_SINGULAR, ForgeItemTags.ORE_RATES_SINGULAR);
		this.copy(ForgeBlockTags.ORE_RATES_SPARSE, ForgeItemTags.ORE_RATES_SPARSE);
		this.copy(ForgeBlockTags.ORES, ForgeItemTags.ORES);
		this.copy(ForgeBlockTags.ORES_COAL, ForgeItemTags.ORES_COAL);
		this.copy(ForgeBlockTags.ORES_COPPER, ForgeItemTags.ORES_COPPER);
		this.copy(ForgeBlockTags.ORES_DIAMOND, ForgeItemTags.ORES_DIAMOND);
		this.copy(ForgeBlockTags.ORES_EMERALD, ForgeItemTags.ORES_EMERALD);
		this.copy(ForgeBlockTags.ORES_GOLD, ForgeItemTags.ORES_GOLD);
		this.copy(ForgeBlockTags.ORES_IRON, ForgeItemTags.ORES_IRON);
		this.copy(ForgeBlockTags.ORES_LAPIS, ForgeItemTags.ORES_LAPIS);
		this.copy(ForgeBlockTags.ORES_QUARTZ, ForgeItemTags.ORES_QUARTZ);
		this.copy(ForgeBlockTags.ORES_REDSTONE, ForgeItemTags.ORES_REDSTONE);
		this.copy(ForgeBlockTags.ORES_NETHERITE_SCRAP, ForgeItemTags.ORES_NETHERITE_SCRAP);
		this.copy(ForgeBlockTags.ORES_IN_GROUND_DEEPSLATE, ForgeItemTags.ORES_IN_GROUND_DEEPSLATE);
		this.copy(ForgeBlockTags.ORES_IN_GROUND_NETHERRACK, ForgeItemTags.ORES_IN_GROUND_NETHERRACK);
		this.copy(ForgeBlockTags.ORES_IN_GROUND_STONE, ForgeItemTags.ORES_IN_GROUND_STONE);
		this.tag(ForgeItemTags.RAW_MATERIALS).addTags(ForgeItemTags.RAW_MATERIALS_COPPER, ForgeItemTags.RAW_MATERIALS_GOLD, ForgeItemTags.RAW_MATERIALS_IRON);
		this.tag(ForgeItemTags.RAW_MATERIALS_COPPER).add(Items.RAW_COPPER);
		this.tag(ForgeItemTags.RAW_MATERIALS_GOLD).add(Items.RAW_GOLD);
		this.tag(ForgeItemTags.RAW_MATERIALS_IRON).add(Items.RAW_IRON);
		this.tag(ForgeItemTags.RODS).addTags(ForgeItemTags.RODS_BLAZE, ForgeItemTags.RODS_WOODEN);
		this.tag(ForgeItemTags.RODS_BLAZE).add(Items.BLAZE_ROD);
		this.tag(ForgeItemTags.RODS_WOODEN).add(Items.STICK);
		this.copy(ForgeBlockTags.SAND, ForgeItemTags.SAND);
		this.copy(ForgeBlockTags.SAND_COLORLESS, ForgeItemTags.SAND_COLORLESS);
		this.copy(ForgeBlockTags.SAND_RED, ForgeItemTags.SAND_RED);
		this.copy(ForgeBlockTags.SANDSTONE, ForgeItemTags.SANDSTONE);
		this.tag(ForgeItemTags.SEEDS).addTags(ForgeItemTags.SEEDS_BEETROOT, ForgeItemTags.SEEDS_MELON, ForgeItemTags.SEEDS_PUMPKIN, ForgeItemTags.SEEDS_WHEAT);
		this.tag(ForgeItemTags.SEEDS_BEETROOT).add(Items.BEETROOT_SEEDS);
		this.tag(ForgeItemTags.SEEDS_MELON).add(Items.MELON_SEEDS);
		this.tag(ForgeItemTags.SEEDS_PUMPKIN).add(Items.PUMPKIN_SEEDS);
		this.tag(ForgeItemTags.SEEDS_WHEAT).add(Items.WHEAT_SEEDS);
		this.tag(ForgeItemTags.SHEARS).add(Items.SHEARS);
		this.tag(ForgeItemTags.SLIMEBALLS).add(Items.SLIME_BALL);
		this.copy(ForgeBlockTags.STAINED_GLASS, ForgeItemTags.STAINED_GLASS);
		this.copy(ForgeBlockTags.STAINED_GLASS_PANES, ForgeItemTags.STAINED_GLASS_PANES);
		this.copy(ForgeBlockTags.STONE, ForgeItemTags.STONE);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS, ForgeItemTags.STORAGE_BLOCKS);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_AMETHYST, ForgeItemTags.STORAGE_BLOCKS_AMETHYST);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_COAL, ForgeItemTags.STORAGE_BLOCKS_COAL);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_COPPER, ForgeItemTags.STORAGE_BLOCKS_COPPER);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_DIAMOND, ForgeItemTags.STORAGE_BLOCKS_DIAMOND);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_EMERALD, ForgeItemTags.STORAGE_BLOCKS_EMERALD);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_GOLD, ForgeItemTags.STORAGE_BLOCKS_GOLD);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_IRON, ForgeItemTags.STORAGE_BLOCKS_IRON);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_LAPIS, ForgeItemTags.STORAGE_BLOCKS_LAPIS);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_QUARTZ, ForgeItemTags.STORAGE_BLOCKS_QUARTZ);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_REDSTONE, ForgeItemTags.STORAGE_BLOCKS_REDSTONE);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_RAW_COPPER, ForgeItemTags.STORAGE_BLOCKS_RAW_COPPER);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_RAW_GOLD, ForgeItemTags.STORAGE_BLOCKS_RAW_GOLD);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_RAW_IRON, ForgeItemTags.STORAGE_BLOCKS_RAW_IRON);
		this.copy(ForgeBlockTags.STORAGE_BLOCKS_NETHERITE, ForgeItemTags.STORAGE_BLOCKS_NETHERITE);
		this.tag(ForgeItemTags.STRING).add(Items.STRING);
		this.tag(ForgeItemTags.TOOLS_SHIELDS).add(Items.SHIELD);
		this.tag(ForgeItemTags.TOOLS_BOWS).add(Items.BOW);
		this.tag(ForgeItemTags.TOOLS_CROSSBOWS).add(Items.CROSSBOW);
		this.tag(ForgeItemTags.TOOLS_FISHING_RODS).add(Items.FISHING_ROD);
		this.tag(ForgeItemTags.TOOLS_TRIDENTS).add(Items.TRIDENT);
		this.tag(ForgeItemTags.TOOLS).addTags(ItemTags.SWORDS, ItemTags.AXES, ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.HOES).addTags(ForgeItemTags.TOOLS_SHIELDS, ForgeItemTags.TOOLS_BOWS, ForgeItemTags.TOOLS_CROSSBOWS, ForgeItemTags.TOOLS_FISHING_RODS, ForgeItemTags.TOOLS_TRIDENTS);
		this.tag(ForgeItemTags.ARMORS_HELMETS).add(Items.LEATHER_HELMET, Items.TURTLE_HELMET, Items.CHAINMAIL_HELMET, Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET);
		this.tag(ForgeItemTags.ARMORS_CHESTPLATES).add(Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE);
		this.tag(ForgeItemTags.ARMORS_LEGGINGS).add(Items.LEATHER_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS);
		this.tag(ForgeItemTags.ARMORS_BOOTS).add(Items.LEATHER_BOOTS, Items.CHAINMAIL_BOOTS, Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS);
		this.tag(ForgeItemTags.ARMORS).addTags(ForgeItemTags.ARMORS_HELMETS, ForgeItemTags.ARMORS_CHESTPLATES, ForgeItemTags.ARMORS_LEGGINGS, ForgeItemTags.ARMORS_BOOTS);
	}

	private void addColored(Consumer<TagKey<Item>> consumer, TagKey<Item> group, String pattern) {
		String prefix = group.location().getPath().toUpperCase(Locale.ENGLISH) + '_';
		for (DyeColor color : DyeColor.values()) {
			ResourceLocation key = new ResourceLocation("minecraft", pattern.replace("{color}", color.getName()));
			TagKey<Item> tag = this.getSimpleItemTag(prefix + color.getName());
			Item item = BuiltInRegistries.ITEM.get(key);
			if (item == null || item == Items.AIR) {
				throw new IllegalStateException("Unknown vanilla item: " + key.toString());
			}
			this.tag(tag).add(item);
			consumer.accept(tag);
		}
	}

	private void copyColored(TagKey<Block> blockGroup, TagKey<Item> itemGroup) {
		String blockPre = blockGroup.location().getPath().toUpperCase(Locale.ENGLISH) + '_';
		String itemPre = itemGroup.location().getPath().toUpperCase(Locale.ENGLISH) + '_';
		for (DyeColor color : DyeColor.values()) {
			TagKey<Block> from = this.getSimpleBlockTag(blockPre + color.getName());
			TagKey<Item> to = this.getSimpleItemTag(itemPre + color.getName());
			this.copy(from, to);
		}
		this.copy(this.getSimpleBlockTag(blockPre + "colorless"), this.getSimpleItemTag(itemPre + "colorless"));
	}

	private TagKey<Block> getSimpleBlockTag(String name) {
		try {
			name = name.toUpperCase(Locale.ENGLISH);
			return (TagKey<Block>) ForgeBlockTags.class.getDeclaredField(name).get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new IllegalStateException(ForgeBlockTags.class.getName() + " is missing tag name: " + name);
		}
	}

	private TagKey<Item> getSimpleItemTag(String name) {
		try {
			name = name.toUpperCase(Locale.ENGLISH);
			return (TagKey<Item>) ForgeItemTags.class.getDeclaredField(name).get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new IllegalStateException(ForgeItemTags.class.getName() + " is missing tag name: " + name);
		}
	}
}
