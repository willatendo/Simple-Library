package willatendo.simplelibrary.server.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;
import java.util.function.ToIntFunction;

public final class BlockUtils {
    public static final BlockBehaviour.StatePredicate NOT_CLOSED_SHULKER = (blockState, blockGetter, blockPos) -> !(blockGetter.getBlockEntity(blockPos) instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) || shulkerBoxBlockEntity.isClosed();
    public static final BlockBehaviour.StatePredicate NOT_EXTENDED_PISTON = (blockState, blockGetter, blockPos) -> !blockState.getValue(PistonBaseBlock.EXTENDED);

    private BlockUtils() {
    }

    public static ToIntFunction<BlockState> litBlockEmission(int lightLevel) {
        return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightLevel : 0;
    }

    private static Function<BlockState, MapColor> waterloggedMapColor(MapColor unwaterloggedMapColor) {
        return blockState -> blockState.getValue(BlockStateProperties.WATERLOGGED) ? MapColor.WATER : unwaterloggedMapColor;
    }

    private static BlockBehaviour.Properties logProperties(MapColor sideColor, MapColor topColor, SoundType sound) {
        return BlockBehaviour.Properties.of().mapColor(blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? sideColor : topColor).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(sound).ignitedByLava();
    }

    private static BlockBehaviour.Properties netherStemProperties(MapColor mapColor) {
        return BlockBehaviour.Properties.of().mapColor(blockState -> mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.STEM);
    }

    private static BlockBehaviour.Properties leavesProperties(SoundType sound) {
        return BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(0.2F).randomTicks().sound(sound).noOcclusion().isValidSpawn(BlockUtils::ocelotOrParrot).isSuffocating(BlockUtils::never).isViewBlocking(BlockUtils::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(BlockUtils::never);
    }


    private static BlockBehaviour.Properties shulkerBoxProperties(MapColor mapColor) {
        return BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().strength(2.0F).dynamicShape().noOcclusion().isSuffocating(NOT_CLOSED_SHULKER).isViewBlocking(NOT_CLOSED_SHULKER).pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties pistonProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F).isRedstoneConductor(BlockUtils::never).isSuffocating(NOT_EXTENDED_PISTON).isViewBlocking(NOT_EXTENDED_PISTON).pushReaction(PushReaction.BLOCK);
    }

    private static BlockBehaviour.Properties buttonProperties() {
        return BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties flowerPotProperties() {
        return BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties candleProperties(MapColor mapColor) {
        return BlockBehaviour.Properties.of().mapColor(mapColor).noOcclusion().strength(0.1F).sound(SoundType.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION).pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties wallVariant(Block baseBlock, boolean overrideDescription) {
        BlockBehaviour.Properties basicProperties = baseBlock.properties();
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().overrideLootTable(baseBlock.getLootTable());
        if (overrideDescription) {
            properties = properties.overrideDescription(baseBlock.getDescriptionId());
        }

        return properties;
    }

    public static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    public static Boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return true;
    }

    public static Boolean ocelotOrParrot(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return (entityType == EntityType.OCELOT || entityType == EntityType.PARROT);
    }

    public static boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    public static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }
}
