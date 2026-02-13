package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemSubRegistry extends SimpleRegistry<Item> {
    public ItemSubRegistry(String modId) {
        super(Registries.ITEM, modId);
    }

    public <B extends Block> SimpleHolder<BlockItem> registerBlock(SimpleHolder<B> block) {
        return this.registerBlock(block, BlockItem::new);
    }

    public <B extends Block> SimpleHolder<BlockItem> registerBlock(SimpleHolder<B> block, Supplier<Item.Properties> properties) {
        return this.registerBlock(block, BlockItem::new, properties);
    }

    public <I extends Item, B extends Block> SimpleHolder<I> registerBlock(SimpleHolder<B> block, BiFunction<B, Item.Properties, I> factory) {
        return this.registerBlock(block, factory, Item.Properties::new);
    }

    public <T extends Item, B extends Block> SimpleHolder<T> registerBlock(SimpleHolder<B> block, BiFunction<B, Item.Properties, T> factory, Supplier<Item.Properties> properties) {
        return this.registerItem(block.getIdentifier().getPath(), propertiesIn -> factory.apply(block.get(), propertiesIn), properties.get()::useBlockDescriptionPrefix);
    }

    public <SB extends StandingSignBlock, WB extends WallSignBlock> SimpleHolder<SignItem> registerSign(SimpleHolder<SB> signBlock, SimpleHolder<WB> wallSign) {
        return this.registerBlock(signBlock, (block, properties) -> new SignItem(block, wallSign.get(), properties), () -> new Item.Properties().stacksTo(16));
    }

    public <CB extends CeilingHangingSignBlock, WB extends WallHangingSignBlock> SimpleHolder<HangingSignItem> registerHangingSign(SimpleHolder<CB> hangingSignBlock, SimpleHolder<WB> hangingWallSign) {
        return this.registerBlock(hangingSignBlock, (block, properties) -> new HangingSignItem(block, hangingWallSign.get(), properties), () -> new Item.Properties().stacksTo(16));
    }

    public SimpleHolder<BoatItem> registerBoat(String name, Function<Item.Properties, BoatItem> boatItem) {
        return this.registerItem(name, boatItem, () -> new Item.Properties().stacksTo(1));
    }

    public SimpleHolder<BoatItem> registerChestBoat(String name, Function<Item.Properties, BoatItem> boatItem) {
        return this.registerItem(name, boatItem, new Item.Properties().stacksTo(1));
    }

    public SimpleHolder<Item> registerItem(String name) {
        return this.registerItem(name, new Item.Properties());
    }

    public SimpleHolder<Item> registerItem(String name, Item.Properties properties) {
        return this.registerItem(name, () -> properties);
    }

    public SimpleHolder<Item> registerItem(String name, Supplier<Item.Properties> properties) {
        return this.registerItem(name, Item::new, properties);
    }

    public <T extends Item> SimpleHolder<T> registerItem(String name, Function<Item.Properties, T> item) {
        return this.registerItem(name, item, new Item.Properties());
    }

    public <T extends Item> SimpleHolder<T> registerItem(String name, Function<Item.Properties, T> item, Item.Properties properties) {
        return this.registerItem(name, item, () -> properties);
    }

    public <T extends Item> SimpleHolder<T> registerItem(String name, Function<Item.Properties, T> item, Supplier<Item.Properties> properties) {
        ResourceKey<Item> id = ResourceKey.create(this.registryKey, CoreUtils.resource(this.modId, name));
        return this.register(name, () -> item.apply(properties.get().setId(id)));
    }

    public ResourceKey<Item> reference(String name) {
        return ResourceKey.create(this.registryKey, CoreUtils.resource(this.modId, name));
    }
}
