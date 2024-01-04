package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
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
import willatendo.simplelibrary.server.util.SimpleUtils;

public abstract class SimpleLanguageProvider implements DataProvider {
	private final Map<String, String> translationData = new TreeMap<>();
	private final FabricDataOutput fabricDataOutput;
	private final String modid;
	private final String locale;

	public SimpleLanguageProvider(FabricDataOutput fabricDataOutput, String modid, String locale) {
		this.fabricDataOutput = fabricDataOutput;
		this.modid = modid;
		this.locale = locale;
	}

	protected abstract void addTranslations();

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		this.addTranslations();

		if (!this.translationData.isEmpty()) {
			return save(cache, this.fabricDataOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modid).resolve("lang").resolve(this.locale + ".json"));
		}

		return CompletableFuture.allOf();
	}

	private CompletableFuture<?> save(CachedOutput cache, Path target) {
		JsonObject jsonObject = new JsonObject();
		this.translationData.forEach(jsonObject::addProperty);

		return DataProvider.saveStable(cache, jsonObject, target);
	}

	public Map<String, String> getTranslationData() {
		return this.translationData;
	}

	public void add(Block key, String name) {
		this.add(key.getDescriptionId(), name);
	}

	public void add(Item key, String name) {
		this.add(key.getDescriptionId(), name);
	}

	public void add(ItemStack key, String name) {
		this.add(key.getDescriptionId(), name);
	}

	public void add(Enchantment key, String name) {
		this.add(key.getDescriptionId(), name);
	}

	public void add(MobEffect key, String name) {
		this.add(key.getDescriptionId(), name);
	}

	public void add(EntityType<?> key, String name) {
		this.add(key.getDescriptionId(), name);
	}

	public void add(Item item) {
		this.add(item, SimpleUtils.autoName(BuiltInRegistries.ITEM.getKey(item).getPath()));
	}

	public void add(Block block) {
		this.add(block, SimpleUtils.autoName(BuiltInRegistries.BLOCK.getKey(block).getPath()));
	}

	public void add(Enchantment enchantment) {
		this.add(enchantment, SimpleUtils.autoName(BuiltInRegistries.ENCHANTMENT.getKey(enchantment).getPath()));
	}

	public void add(MobEffect mobEffect) {
		this.add(mobEffect, SimpleUtils.autoName(BuiltInRegistries.MOB_EFFECT.getKey(mobEffect).getPath()));
	}

	public void add(EntityType<?> entityType) {
		this.add(entityType, SimpleUtils.autoName(BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath()));
	}

	public void add(SoundEvent soundEvent) {
		this.add(soundEvent, SimpleUtils.autoName(BuiltInRegistries.SOUND_EVENT.getKey(soundEvent).getPath()));
	}

	public void add(MenuType menuType) {
		this.add(menuType, SimpleUtils.autoName(BuiltInRegistries.MENU.getKey(menuType).getPath()));
	}

	public void add(String category, String advancement, String title, String desc) {
		this.add("advancements." + this.modid + "." + category + "." + advancement + ".title", title);
		this.add("advancements." + this.modid + "." + category + "." + advancement + ".desc", desc);
	}

	public void add(SoundEvent soundEvent, String name) {
		this.add("sound." + BuiltInRegistries.SOUND_EVENT.getKey(soundEvent).getNamespace() + BuiltInRegistries.SOUND_EVENT.getKey(soundEvent).getPath(), name);
	}

	public void add(MenuType menuType, String name) {
		this.add("menu." + BuiltInRegistries.MENU.getKey(menuType).getNamespace() + BuiltInRegistries.MENU.getKey(menuType).getPath(), name);
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

	public void addDesc(Block block, String... descs) {
		for (int i = 0; i < descs.length; i++) {
			this.add(block.getDescriptionId() + ".desc" + i, descs[i]);
		}
	}

	public void addDesc(Block block, String name) {
		this.add(block.getDescriptionId() + ".desc", name);
	}

	public void add(String key, String value) {
		if (this.translationData.put(key, value) != null) {
			throw new IllegalStateException("Duplicate translation key " + key);
		}
	}

	@Override
	public String getName() {
		return this.modid + ": Language Provider (" + this.locale + ")";
	}
}
