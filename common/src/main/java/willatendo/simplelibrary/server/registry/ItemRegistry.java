package willatendo.simplelibrary.server.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ItemRegistry extends SimpleRegistry<Item> {
    ItemRegistry(String modId) {
        super(Registries.ITEM, modId);
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
        ResourceKey<Item> id = ResourceKey.create(this.registryKey, ResourceLocation.fromNamespaceAndPath(this.modId, name));
        return this.register(name, () -> item.apply(properties.get().setId(id)));
    }

    public ResourceKey<Item> reference(String id) {
        return ResourceKey.create(this.registryKey, ResourceLocation.fromNamespaceAndPath(this.modId, id));
    }
}
