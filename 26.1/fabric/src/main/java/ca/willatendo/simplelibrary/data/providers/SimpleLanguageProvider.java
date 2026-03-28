package ca.willatendo.simplelibrary.data.providers;

import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gamerules.GameRule;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public abstract class SimpleLanguageProvider implements DataProvider {
    private static final Codec<Map<String, Component>> CODEC = Codec.unboundedMap(Codec.STRING, ComponentSerialization.CODEC);

    private final Map<String, Component> data = new TreeMap<>();
    private final PackOutput packOutput;
    private final String modId;
    private final String locale;

    public SimpleLanguageProvider(PackOutput packOutput, String modId, String locale) {
        this.packOutput = packOutput;
        this.modId = modId;
        this.locale = locale;
    }

    protected abstract void addTranslations();

    public void add(String key, Component value) {
        if (this.data.put(key, value) != null) {
            throw new IllegalStateException("Duplicate translation key " + key);
        }
    }

    public void add(String key, String value) {
        this.add(key, Component.literal(value));
    }

    public void add(Item key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(Item item) {
        this.add(item, CoreUtils.simpleAutoName(BuiltInRegistries.ITEM.getKey(item).getPath()));
    }

    public void add(Block key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(Block block) {
        this.add(block, CoreUtils.simpleAutoName(BuiltInRegistries.BLOCK.getKey(block).getPath()));
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


    public void add(MobEffect key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(MobEffect mobEffect) {
        this.add(mobEffect, CoreUtils.simpleAutoName(BuiltInRegistries.MOB_EFFECT.getKey(mobEffect).getPath()));
    }

    public void add(EntityType<?> key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(EntityType<?> entityType) {
        this.add(entityType, CoreUtils.simpleAutoName(BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath()));
    }

    public void add(SoundEvent soundEvent, String name) {
        this.add("sound." + BuiltInRegistries.SOUND_EVENT.getKey(soundEvent).getNamespace() + BuiltInRegistries.SOUND_EVENT.getKey(soundEvent).getPath(), name);
    }

    public void add(SoundEvent soundEvent) {
        this.add(soundEvent, CoreUtils.simpleAutoName(BuiltInRegistries.SOUND_EVENT.getKey(soundEvent).getPath()));
    }

    public void add(MenuType<?> menuType, String name) {
        this.add("menu." + BuiltInRegistries.MENU.getKey(menuType).getNamespace() + BuiltInRegistries.MENU.getKey(menuType).getPath(), name);
    }

    public void add(MenuType<?> menuType) {
        this.add(menuType, CoreUtils.simpleAutoName(BuiltInRegistries.MENU.getKey(menuType).getPath()));
    }

    public void add(String category, String advancement, String title, String desc) {
        this.add("advancements." + this.modId + "." + category + "." + advancement + ".title", title);
        this.add("advancements." + this.modId + "." + category + "." + advancement + ".desc", desc);
    }

    public void add(CreativeModeTab creativeModeTab, String name) {
        this.add(creativeModeTab.getDisplayName().getString(), name);
    }

    public void add(GameRule<?> gameRule, String translation) {
        this.add("gamerule." + gameRule.getDescriptionId(), translation);
    }

    public void addStat(Identifier stat, String name) {
        this.add("stat." + stat.getNamespace() + "." + stat.getPath(), name);
    }

    public void addBiome(ResourceKey<Biome> biome, String value) {
        this.add(biome.identifier().toLanguageKey("biome"), value);
    }

    public void addDimension(ResourceKey<Level> dimension, String value) {
        this.add(dimension.identifier().toLanguageKey("dimension"), value);
    }

    public void add(TagKey<?> tagKey, String name) {
        this.add(SimpleLanguageProvider.getTagTranslationKey(tagKey), name);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.addTranslations();

        if (!this.data.isEmpty()) {
            return DataProvider.saveStable(cache, CODEC.encode(this.data, JsonOps.INSTANCE, new JsonObject()).getOrThrow(), this.packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modId).resolve("lang").resolve(this.locale + ".json"));
        }

        return CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "SimpleLibrary: Language Provider for " + this.modId + "/" + this.locale;
    }

    public static String getTagTranslationKey(TagKey<?> tagKey) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("tag.");

        Identifier registryIdentifier = tagKey.registry().identifier();
        Identifier tagIdentifier = tagKey.location();

        stringBuilder.append(registryIdentifier.toShortLanguageKey().replace("/", ".")).append(".").append(tagIdentifier.getNamespace()).append(".").append(tagIdentifier.getPath().replace("/", "."));

        return stringBuilder.toString();
    }
}
