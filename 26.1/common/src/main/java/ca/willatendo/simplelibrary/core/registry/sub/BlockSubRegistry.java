package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import ca.willatendo.simplelibrary.server.utils.BlockUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockSubRegistry extends SimpleRegistry<Block> {
    public BlockSubRegistry(String modId) {
        super(Registries.BLOCK, modId);
    }

    public SimpleHolder<Block> registerPlanks(String name, MapColor mapColor) {
        return this.registerBlock(name, BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava());
    }

    public SimpleHolder<SaplingBlock> registerSapling(String name, TreeGrower treeGrower) {
        return this.registerBlock(name, properties -> new SaplingBlock(treeGrower, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    }

    public SimpleHolder<RotatedPillarBlock> registerLog(String name, MapColor topColor, MapColor sideColor, SoundType soundType) {
        return this.registerBlock(name, RotatedPillarBlock::new, () -> BlockUtils.logProperties(topColor, sideColor, soundType));
    }

    public SimpleHolder<RotatedPillarBlock> registerStrippedLog(String name, MapColor mapColor, SoundType soundType) {
        return this.registerBlock(name, RotatedPillarBlock::new, () -> BlockUtils.logProperties(mapColor, mapColor, soundType));
    }

    public SimpleHolder<RotatedPillarBlock> registerWood(String name, MapColor mapColor) {
        return this.registerBlock(name, RotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava());
    }

    public SimpleHolder<RotatedPillarBlock> registerStrippedWood(String name, MapColor mapColor) {
        return this.registerBlock(name, RotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava());
    }

    public SimpleHolder<LeavesBlock> registerLeaves(String name, float leafParticleChance) {
        return this.registerBlock(name, properties -> new TintedParticleLeavesBlock(leafParticleChance, properties), () -> BlockUtils.leavesProperties(SoundType.GRASS));
    }

    public SimpleHolder<StairBlock> registerWoodStairs(String name, Supplier<Block> planks) {
        SimpleHolder<StairBlock> stairs = this.registerStairs(name, planks);
        return stairs;
    }

    public SimpleHolder<StairBlock> registerStairs(String name, Supplier<Block> block) {
        return this.registerBlock(name, properties -> new StairBlock(block.get().defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(block.get()));
    }

    public <SB extends StandingSignBlock> SimpleHolder<SB> registerSign(String name, BiFunction<WoodType, BlockBehaviour.Properties, SB> block, WoodType woodType, MapColor mapColor) {
        return this.registerBlock(name, properties -> block.apply(woodType, properties), () -> BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava());
    }

    public <WB extends WallSignBlock, SB extends StandingSignBlock> SimpleHolder<WB> registerWallSign(String name, BiFunction<WoodType, BlockBehaviour.Properties, WB> block, Supplier<SB> standingSignBlock) {
        return this.registerBlock(name, properties -> block.apply(standingSignBlock.get().type(), properties), () -> BlockUtils.wallVariant(standingSignBlock.get(), false));
    }

    public SimpleHolder<DoorBlock> registerDoor(String name, BlockSetType blockSetType, Supplier<Block> planks) {
        return this.registerBlock(name, properties -> new DoorBlock(blockSetType, properties), () -> BlockBehaviour.Properties.of().mapColor(planks.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY));
    }

    public <CB extends CeilingHangingSignBlock> SimpleHolder<CB> registerHangingSign(String name, BiFunction<WoodType, BlockBehaviour.Properties, CB> block, WoodType woodType, Supplier<RotatedPillarBlock> logBlock) {
        return this.registerBlock(name, properties -> block.apply(woodType, properties), () -> BlockBehaviour.Properties.of().mapColor(logBlock.get().defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).ignitedByLava());
    }

    public <WB extends WallHangingSignBlock, CB extends CeilingHangingSignBlock> SimpleHolder<WB> registerWallHangingSign(String name, BiFunction<WoodType, BlockBehaviour.Properties, WB> block, Supplier<CB> hangingSign) {
        return this.registerBlock(name, properties -> block.apply(hangingSign.get().type(), properties), () -> BlockUtils.wallVariant(hangingSign.get(), false));
    }

    public SimpleHolder<PressurePlateBlock> registerPressurePlate(String name, BlockSetType blockSetType, Supplier<Block> planks) {
        return this.registerBlock(name, properties -> new PressurePlateBlock(blockSetType, properties), () -> BlockBehaviour.Properties.of().mapColor(planks.get().defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(0.5F).ignitedByLava().pushReaction(PushReaction.DESTROY));
    }

    public SimpleHolder<FenceBlock> registerFence(String name, Supplier<Block> planks) {
        return this.registerBlock(name, FenceBlock::new, () -> BlockBehaviour.Properties.of().mapColor(planks.get().defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava());
    }

    public SimpleHolder<TrapDoorBlock> registerTrapdoor(String name, BlockSetType blockSetType, MapColor mapColor) {
        return this.registerBlock(name, properties -> new TrapDoorBlock(blockSetType, properties), () -> BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().isValidSpawn(BlockUtils::never).ignitedByLava());
    }

    public SimpleHolder<FenceGateBlock> registerFenceGate(String name, WoodType woodType, Supplier<Block> planks) {
        return this.registerBlock(name, properties -> new FenceGateBlock(woodType, properties), () -> BlockBehaviour.Properties.of().mapColor(planks.get().defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).ignitedByLava());
    }

    public SimpleHolder<FlowerPotBlock> registerPottedSapling(String name, Supplier<SaplingBlock> sapling) {
        return this.registerBlock(name, properties -> new FlowerPotBlock(sapling.get(), properties), BlockUtils::flowerPotProperties);
    }

    public SimpleHolder<ButtonBlock> registerButton(String name, BlockSetType blockSetType) {
        return this.registerBlock(name, properties -> new ButtonBlock(blockSetType, 30, properties), BlockUtils::buttonProperties);
    }

    public SimpleHolder<SlabBlock> registerWoodSlab(String name, MapColor mapColor) {
        return this.registerBlock(name, SlabBlock::new, () -> BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava());
    }

    public SimpleHolder<SlabBlock> registerStoneSlab(String name, MapColor mapColor) {
        return this.registerBlock(name, SlabBlock::new, () -> BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F));
    }

    public SimpleHolder<Block> registerBlock(String name, BlockBehaviour.Properties properties) {
        return this.registerBlock(name, () -> properties);
    }

    public SimpleHolder<Block> registerBlock(String name, Supplier<BlockBehaviour.Properties> properties) {
        return this.registerBlock(name, Block::new, properties);
    }

    public <T extends Block> SimpleHolder<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
        ResourceKey<Block> id = ResourceKey.create(this.registryKey, CoreUtils.resource(this.modId, name));
        return this.register(name, () -> block.apply(properties.get().setId(id)));
    }

    public ResourceKey<Block> reference(String name) {
        return ResourceKey.create(this.registryKey, CoreUtils.resource(this.modId, name));
    }
}
