package willatendo.simplelibrary.server.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import willatendo.simplelibrary.server.registry.SimpleHolder;
import willatendo.simplelibrary.server.registry.SimpleRegistry;
import willatendo.simplelibrary.server.util.SimpleUtils;

import java.util.List;

public class SimpleBlockEntityTypes {
    public static final SimpleRegistry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = SimpleRegistry.create(Registries.BLOCK_ENTITY_TYPE, SimpleUtils.SIMPLE_ID);

    public static final SimpleHolder<BlockEntityType<SimpleSignBlockEntity>> SIMPLE_SIGN = BLOCK_ENTITY_TYPES.register("simple_sign", () -> BlockEntityType.Builder.<SimpleSignBlockEntity>of(SimpleSignBlockEntity::new).build(null));

    public static void init(List<SimpleRegistry<?>> simpleRegistries) {
        simpleRegistries.add(BLOCK_ENTITY_TYPES);
    }
}
