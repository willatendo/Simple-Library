package willatendo.simplelibrary.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import willatendo.simplelibrary.server.util.SimpleUtils;

public abstract class SimpleLanguageProvider extends LanguageProvider {
    private final String modId;

    public SimpleLanguageProvider(PackOutput packOutput, String modId, String local) {
        super(packOutput, modId, local);
        this.modId = modId;
    }

    public void add(Block key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(Item key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(MobEffect key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(EntityType<?> key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(Item item) {
        this.add(item, SimpleUtils.simpleAutoName(BuiltInRegistries.ITEM.getKey(item).getPath()));
    }

    public void add(Block block) {
        this.add(block, SimpleUtils.simpleAutoName(BuiltInRegistries.BLOCK.getKey(block).getPath()));
    }

    public void add(MobEffect mobEffect) {
        this.add(mobEffect, SimpleUtils.simpleAutoName(BuiltInRegistries.MOB_EFFECT.getKey(mobEffect).getPath()));
    }

    public void add(EntityType<?> entityType) {
        this.add(entityType, SimpleUtils.simpleAutoName(BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath()));
    }

    public void add(SoundEvent soundEvent) {
        this.add(soundEvent, SimpleUtils.simpleAutoName(BuiltInRegistries.SOUND_EVENT.getKey(soundEvent).getPath()));
    }

    public void add(MenuType menuType) {
        this.add(menuType, SimpleUtils.simpleAutoName(BuiltInRegistries.MENU.getKey(menuType).getPath()));
    }

    public void add(String category, String advancement, String title, String desc) {
        this.add("advancements." + this.modId + "." + category + "." + advancement + ".title", title);
        this.add("advancements." + this.modId + "." + category + "." + advancement + ".desc", desc);
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

    public void add(GameRules.Key<?> key, String translation) {
        this.add("gamerule." + key.getId(), translation);
    }

    public void addStat(ResourceLocation stat, String name) {
        this.add("stat." + stat.getNamespace() + "." + stat.getPath(), name);
    }
}
