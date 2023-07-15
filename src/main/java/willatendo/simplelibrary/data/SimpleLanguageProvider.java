package willatendo.simplelibrary.data;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import willatendo.simplelibrary.server.SimpleUtils;

public abstract class SimpleLanguageProvider extends LanguageProvider {
	private final String id;
	private final String locale;

	public SimpleLanguageProvider(PackOutput packOutput, String modid, String locale) {
		super(packOutput, modid, locale);
		this.id = modid;
		this.locale = locale;
	}

	public void add(Item item) {
		this.add(item, SimpleUtils.autoName(ForgeRegistries.ITEMS.getKey(item).getPath()));
	}

	public void add(Block block) {
		this.add(block, SimpleUtils.autoName(ForgeRegistries.BLOCKS.getKey(block).getPath()));
	}

	public void add(Enchantment enchantment) {
		this.add(enchantment, SimpleUtils.autoName(ForgeRegistries.ENCHANTMENTS.getKey(enchantment).getPath()));
	}

	public void add(MobEffect mobEffect) {
		this.add(mobEffect, SimpleUtils.autoName(ForgeRegistries.MOB_EFFECTS.getKey(mobEffect).getPath()));
	}

	public void add(EntityType<?> entityType) {
		this.add(entityType, SimpleUtils.autoName(ForgeRegistries.ENTITY_TYPES.getKey(entityType).getPath()));
	}

	public void add(SoundEvent soundEvent) {
		this.add(soundEvent, SimpleUtils.autoName(ForgeRegistries.SOUND_EVENTS.getKey(soundEvent).getPath()));
	}

	public void add(MenuType menuType) {
		this.add(menuType, SimpleUtils.autoName(ForgeRegistries.MENU_TYPES.getKey(menuType).getPath()));
	}

	public void add(String category, String advancement, String title, String desc) {
		this.add("advancements." + this.id + "." + category + "." + advancement + ".title", title);
		this.add("advancements." + this.id + "." + category + "." + advancement + ".desc", desc);
	}

	public void add(SoundEvent soundEvent, String name) {
		this.add("sound." + ForgeRegistries.SOUND_EVENTS.getKey(soundEvent).getNamespace() + ForgeRegistries.SOUND_EVENTS.getKey(soundEvent).getPath(), name);
	}

	public void add(MenuType menuType, String name) {
		this.add("menu." + ForgeRegistries.MENU_TYPES.getKey(menuType).getNamespace() + ForgeRegistries.MENU_TYPES.getKey(menuType).getPath(), name);
	}

	public void add(CreativeModeTab creativeModeTab, String name) {
		this.add(creativeModeTab.getDisplayName().getString(), name);
	}

	public void addDesc(Item item, String... descs) {
		for (int i = 0; i < descs.length; i++) {
			this.add(item.getDescriptionId() + ".desc" + i, descs[i]);
		}
	}

	public void addDesc(Item item, String desc) {
		this.add(item.getDescriptionId() + ".desc", desc);
	}

	@Override
	public String getName() {
		return this.id + ": Language Provider (" + this.locale + ")";
	}
}
