package willatendo.simplelibrary.data;

import java.util.Map;
import java.util.TreeMap;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import willatendo.simplelibrary.server.util.SimpleUtils;

public abstract class SimpleLanguageProvider extends LanguageProvider {
	private final Map<String, String> translationData = new TreeMap<>();
	private final String modid;
	private final String locale;

	public SimpleLanguageProvider(PackOutput packOutput, String modid, String locale) {
		super(packOutput, modid, locale);
		this.modid = modid;
		this.locale = locale;
	}

	public Map<String, String> getTranslationData() {
		return this.translationData;
	}

	public void add(Block key, String name) {
		this.addToTranslations(key.getDescriptionId(), name);
	}

	public void add(Item key, String name) {
		this.addToTranslations(key.getDescriptionId(), name);
	}

	public void add(ItemStack key, String name) {
		this.addToTranslations(key.getDescriptionId(), name);
	}

	public void add(Enchantment key, String name) {
		this.addToTranslations(key.getDescriptionId(), name);
	}

	public void add(MobEffect key, String name) {
		this.addToTranslations(key.getDescriptionId(), name);
	}

	public void add(EntityType<?> key, String name) {
		this.addToTranslations(key.getDescriptionId(), name);
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
		this.addToTranslations("advancements." + this.modid + "." + category + "." + advancement + ".title", title);
		this.addToTranslations("advancements." + this.modid + "." + category + "." + advancement + ".desc", desc);
	}

	public void add(SoundEvent soundEvent, String name) {
		this.addToTranslations("sound." + ForgeRegistries.SOUND_EVENTS.getKey(soundEvent).getNamespace() + ForgeRegistries.SOUND_EVENTS.getKey(soundEvent).getPath(), name);
	}

	public void add(MenuType menuType, String name) {
		this.addToTranslations("menu." + ForgeRegistries.MENU_TYPES.getKey(menuType).getNamespace() + ForgeRegistries.MENU_TYPES.getKey(menuType).getPath(), name);
	}

	public void add(CreativeModeTab creativeModeTab, String name) {
		this.addToTranslations(creativeModeTab.getDisplayName().getString(), name);
	}

	public void addDesc(Item item, String... descs) {
		for (int i = 0; i < descs.length; i++) {
			this.addToTranslations(item.getDescriptionId() + ".desc" + i, descs[i]);
		}
	}

	public void addDesc(Item item, String desc) {
		this.addToTranslations(item.getDescriptionId() + ".desc", desc);
	}

	public void addToTranslations(String key, String value) {
		this.add(key, value);
		this.translationData.put(key, value);
	}

	@Override
	public String getName() {
		return this.modid + ": Language Provider (" + this.locale + ")";
	}
}
