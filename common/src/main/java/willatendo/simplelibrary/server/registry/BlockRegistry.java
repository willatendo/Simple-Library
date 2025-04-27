package willatendo.simplelibrary.server.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

public final class BlockRegistry extends SimpleRegistry<Block> {
    BlockRegistry(String modId) {
        super(Registries.BLOCK, modId);
    }

    public SimpleHolder<Block> registerBlock(String name, BlockBehaviour.Properties properties) {
        return this.registerBlock(name, () -> properties);
    }

    public SimpleHolder<Block> registerBlock(String name, Supplier<BlockBehaviour.Properties> properties) {
        return this.registerBlock(name, Block::new, properties);
    }

    public <T extends Block> SimpleHolder<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
        ResourceKey<Block> id = ResourceKey.create(this.registryKey, ResourceLocation.fromNamespaceAndPath(this.modId, name));
        return this.register(name, () -> block.apply(properties.get().setId(id)));
    }
}
