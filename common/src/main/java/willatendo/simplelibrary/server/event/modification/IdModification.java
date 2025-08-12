package willatendo.simplelibrary.server.event.modification;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface IdModification {
    <T> void updateId(Registry<T> registry, ResourceLocation oldId, ResourceLocation newId);

    default <T> void updateId(Registry<T> registry, ResourceLocation oldId, ResourceKey<T> newId) {
        this.updateId(registry, oldId, newId.location());
    }

    default <T> void updateId(Registry<T> registry, ResourceLocation oldId, Supplier<T> remap) {
        this.updateId(registry, oldId, this.getId(registry, remap.get()));
    }

    ResourceLocation resource(String id);

    default void updateItemId(ResourceLocation oldId, Supplier<Item> item) {
        this.updateId(BuiltInRegistries.ITEM, oldId, item);
    }

    default void updateItemId(String oldId, Supplier<Item> item) {
        this.updateItemId(this.resource(oldId), item);
    }

    default void updateBlockId(ResourceLocation oldId, Supplier<Block> block) {
        this.updateId(BuiltInRegistries.BLOCK, oldId, block);
    }

    default void updateBlockId(String oldId, Supplier<Block> block) {
        this.updateBlockId(this.resource(oldId), block);
    }

    default <T> ResourceLocation getId(Registry<T> registry, T object) {
        return registry.getKey(object);
    }
}
