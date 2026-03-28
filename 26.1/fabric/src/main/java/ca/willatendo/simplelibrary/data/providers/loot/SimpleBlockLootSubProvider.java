package ca.willatendo.simplelibrary.data.providers.loot;

import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class SimpleBlockLootSubProvider implements LootTableSubProvider {
    protected static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
    private static final float[] NORMAL_LEAVES_STICK_CHANCES = new float[]{0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F};
    private final Map<ResourceKey<LootTable>, LootTable.Builder> map = new HashMap<>();
    protected final HolderLookup.Provider registries;
    private final String modId;
    private final Set<Item> explosionResistant = Set.of();
    private final FeatureFlagSet enabledFeatures = FeatureFlags.REGISTRY.allFlags();

    public SimpleBlockLootSubProvider(HolderLookup.Provider registries, String modId) {
        this.registries = registries;
        this.modId = modId;
    }

    protected LootItemCondition.Builder hasSilkTouch() {
        return MatchTool.toolMatches(ItemPredicate.Builder.item().withComponents(net.minecraft.advancements.criterion.DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))).build()));
    }

    protected LootItemCondition.Builder doesNotHaveSilkTouch() {
        return this.hasSilkTouch().invert();
    }

    protected LootItemCondition.Builder hasShears() {
        return MatchTool.toolMatches(ItemPredicate.Builder.item().of(this.registries.lookupOrThrow(Registries.ITEM), new ItemLike[]{Items.SHEARS}));
    }

    private LootItemCondition.Builder hasShearsOrSilkTouch() {
        return this.hasShears().or(this.hasSilkTouch());
    }

    private LootItemCondition.Builder doesNotHaveShearsOrSilkTouch() {
        return this.hasShearsOrSilkTouch().invert();
    }


    protected <T extends FunctionUserBuilder<T>> T applyExplosionDecay(ItemLike type, FunctionUserBuilder<T> builder) {
        return !this.explosionResistant.contains(type.asItem()) ? builder.apply(ApplyExplosionDecay.explosionDecay()) : builder.unwrap();
    }

    protected <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike type, ConditionUserBuilder<T> builder) {
        return !this.explosionResistant.contains(type.asItem()) ? builder.when(ExplosionCondition.survivesExplosion()) : builder.unwrap();
    }

    public LootTable.Builder createSingleItemTable(ItemLike drop) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(drop, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(drop))));
    }

    protected static LootTable.Builder createSelfDropDispatchTable(Block original, LootItemCondition.Builder condition, LootPoolEntryContainer.Builder<?> entry) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(original).when(condition).otherwise(entry)));
    }

    protected LootTable.Builder createSilkTouchDispatchTable(Block original, LootPoolEntryContainer.Builder<?> entry) {
        return SimpleBlockLootSubProvider.createSelfDropDispatchTable(original, this.hasSilkTouch(), entry);
    }

    protected LootTable.Builder createShearsDispatchTable(Block original, LootPoolEntryContainer.Builder<?> entry) {
        return SimpleBlockLootSubProvider.createSelfDropDispatchTable(original, this.hasShears(), entry);
    }

    protected LootTable.Builder createSilkTouchOrShearsDispatchTable(Block original, LootPoolEntryContainer.Builder<?> entry) {
        return SimpleBlockLootSubProvider.createSelfDropDispatchTable(original, this.hasShearsOrSilkTouch(), entry);
    }

    protected LootTable.Builder createSingleItemTableWithSilkTouch(Block original, ItemLike drop) {
        return this.createSilkTouchDispatchTable(original, this.applyExplosionCondition(original, LootItem.lootTableItem(drop)));
    }

    protected LootTable.Builder createSingleItemTable(ItemLike drop, NumberProvider count) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(drop, LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(count)))));
    }

    protected LootTable.Builder createSingleItemTableWithSilkTouch(Block original, ItemLike drop, NumberProvider count) {
        return this.createSilkTouchDispatchTable(original, this.applyExplosionDecay(original, LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(count))));
    }

    protected LootTable.Builder createSilkTouchOnlyTable(ItemLike drop) {
        return LootTable.lootTable().withPool(LootPool.lootPool().when(this.hasSilkTouch()).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(drop)));
    }

    protected LootTable.Builder createPotFlowerItemTable(ItemLike flower) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(Blocks.FLOWER_POT, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Blocks.FLOWER_POT)))).withPool(this.applyExplosionCondition(flower, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(flower))));
    }

    protected LootTable.Builder createSlabItemTable(Block slab) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(slab, LootItem.lootTableItem(slab).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab).setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))))));
    }

    protected <T extends Comparable<T> & StringRepresentable> LootTable.Builder createSinglePropConditionTable(Block drop, Property<T> property, T value) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(drop, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(drop).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop).setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(property, value))))));
    }

    protected LootTable.Builder createNameableBlockEntityTable(Block drop) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(drop, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(drop).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME)))));
    }

    protected LootTable.Builder createShulkerBoxDrop(Block shulkerBox) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(shulkerBox, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(shulkerBox).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME).include(DataComponents.CONTAINER).include(DataComponents.LOCK).include(DataComponents.CONTAINER_LOOT)))));
    }

    protected LootTable.Builder createCopperOreDrops(Block block) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(Items.RAW_COPPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }

    protected LootTable.Builder createLapisOreDrops(Block block) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(Items.LAPIS_LAZULI).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))).apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }

    protected LootTable.Builder createRedstoneOreDrops(Block block) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F))).apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }

    protected LootTable.Builder createBannerDrop(Block original) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(original, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(original).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME).include(DataComponents.ITEM_NAME).include(DataComponents.TOOLTIP_DISPLAY).include(DataComponents.BANNER_PATTERNS).include(DataComponents.RARITY)))));
    }

    protected LootTable.Builder createBeeNestDrop(Block original) {
        return LootTable.lootTable().withPool(LootPool.lootPool().when(this.hasSilkTouch()).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(original).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.BEES)).apply(CopyBlockState.copyState(original).copy(BeehiveBlock.HONEY_LEVEL))));
    }

    protected LootTable.Builder createBeeHiveDrop(Block original) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add((LootItem.lootTableItem(original).when(this.hasSilkTouch())).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.BEES)).apply(CopyBlockState.copyState(original).copy(BeehiveBlock.HONEY_LEVEL)).otherwise(LootItem.lootTableItem(original))));
    }

    protected LootTable.Builder createCaveVinesDrop(Block original) {
        return LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.GLOW_BERRIES)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(original).setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(CaveVines.BERRIES, true))));
    }

    protected LootTable.Builder createCopperGolemStatueBlock(Block block) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME)).apply(CopyBlockState.copyState(block).copy(CopperGolemStatueBlock.POSE)))));
    }

    protected LootTable.Builder createOreDrop(Block original, Item drop) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(original, this.applyExplosionDecay(original, LootItem.lootTableItem(drop).apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }

    protected LootTable.Builder createMushroomBlockDrop(Block original, ItemLike drop) {
        return this.createSilkTouchDispatchTable(original, this.applyExplosionDecay(original, LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(UniformGenerator.between(-6.0F, 2.0F))).apply(LimitCount.limitCount(IntRange.lowerBound(0)))));
    }

    protected LootTable.Builder createGrassDrops(Block original) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createShearsDispatchTable(original, this.applyExplosionDecay(original, LootItem.lootTableItem(Items.WHEAT_SEEDS).when(LootItemRandomChanceCondition.randomChance(0.125F))).apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE), 2)));
    }

    public LootTable.Builder createStemDrops(Block block, Item drop) {
        return LootTable.lootTable().withPool(this.applyExplosionDecay(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(drop).apply(StemBlock.AGE.getPossibleValues(), (age) -> SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, (float) (age + 1) / 15.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(StemBlock.AGE, age)))))));
    }

    public LootTable.Builder createAttachedStemDrops(Block block, Item drop) {
        return LootTable.lootTable().withPool(this.applyExplosionDecay(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.53333336F))))));
    }

    protected LootTable.Builder createShearsOnlyDrop(ItemLike drop) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(this.hasShears()).add(LootItem.lootTableItem(drop)));
    }

    protected LootTable.Builder createShearsOrSilkTouchOnlyDrop(ItemLike drop) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(this.hasShearsOrSilkTouch()).add(LootItem.lootTableItem(drop)));
    }

    protected LootTable.Builder createMultifaceBlockDrops(Block block, LootItemCondition.Builder condition) {
        return LootTable.lootTable().withPool(LootPool.lootPool().add(this.applyExplosionDecay(block, ((LootItem.lootTableItem(block).when(condition)).apply(Direction.values(), (dir) -> SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(MultifaceBlock.getFaceProperty(dir), true))))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true)))));
    }

    protected LootTable.Builder createMultifaceBlockDrops(Block block) {
        return LootTable.lootTable().withPool(LootPool.lootPool().add(this.applyExplosionDecay(block, (LootItem.lootTableItem(block).apply(Direction.values(), (dir) -> SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(MultifaceBlock.getFaceProperty(dir), true))))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true)))));
    }

    protected LootTable.Builder createMossyCarpetBlockDrops(Block block) {
        return LootTable.lootTable().withPool(LootPool.lootPool().add(this.applyExplosionDecay(block, LootItem.lootTableItem(block).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MossyCarpetBlock.BASE, true))))));
    }

    protected LootTable.Builder createLeavesDrops(Block original, Block sapling, float... saplingChances) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchOrShearsDispatchTable(original, this.applyExplosionCondition(original, LootItem.lootTableItem(sapling)).when(BonusLevelTableCondition.bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), saplingChances))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(this.doesNotHaveShearsOrSilkTouch()).add((this.applyExplosionDecay(original, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))).when(BonusLevelTableCondition.bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), NORMAL_LEAVES_STICK_CHANCES))));
    }

    protected LootTable.Builder createOakLeavesDrops(Block original, Block sapling, float... saplingChances) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createLeavesDrops(original, sapling, saplingChances).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(this.doesNotHaveShearsOrSilkTouch()).add((this.applyExplosionCondition(original, LootItem.lootTableItem(Items.APPLE))).when(BonusLevelTableCondition.bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), new float[]{0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F}))));
    }

    protected LootTable.Builder createMangroveLeavesDrops(Block block) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchOrShearsDispatchTable(block, this.applyExplosionDecay(Blocks.MANGROVE_LEAVES, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).when(BonusLevelTableCondition.bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), NORMAL_LEAVES_STICK_CHANCES)));
    }

    protected LootTable.Builder createCropDrops(Block original, Item cropDrop, Item seedDrop, LootItemCondition.Builder isMaxAge) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.applyExplosionDecay(original, LootTable.lootTable().withPool(LootPool.lootPool().add((LootItem.lootTableItem(cropDrop).when(isMaxAge)).otherwise(LootItem.lootTableItem(seedDrop)))).withPool(LootPool.lootPool().when(isMaxAge).add(LootItem.lootTableItem(seedDrop).apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))));
    }

    protected LootTable.Builder createDoublePlantShearsDrop(Block block) {
        return LootTable.lootTable().withPool(LootPool.lootPool().when(this.hasShears()).add(LootItem.lootTableItem(block).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))));
    }

    protected LootTable.Builder createDoublePlantWithSeedDrops(Block block, Block drop) {
        HolderLookup.RegistryLookup<Block> blocks = this.registries.lookupOrThrow(Registries.BLOCK);
        LootPoolEntryContainer.Builder<?> dropEntry = LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))).when(this.hasShears()).otherwise(this.applyExplosionCondition(block, LootItem.lootTableItem(Items.WHEAT_SEEDS)).when(LootItemRandomChanceCondition.randomChance(0.125F)));
        return LootTable.lootTable().withPool(LootPool.lootPool().add(dropEntry).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER))).when(LocationCheck.checkLocation(net.minecraft.advancements.criterion.LocationPredicate.Builder.location().setBlock(net.minecraft.advancements.criterion.BlockPredicate.Builder.block().of(blocks, new Block[]{block}).setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))), new BlockPos(0, 1, 0)))).withPool(LootPool.lootPool().add(dropEntry).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))).when(LocationCheck.checkLocation(net.minecraft.advancements.criterion.LocationPredicate.Builder.location().setBlock(net.minecraft.advancements.criterion.BlockPredicate.Builder.block().of(blocks, new Block[]{block}).setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER))), new BlockPos(0, -1, 0))));
    }

    protected LootTable.Builder createCandleDrops(Block block) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(block, LootItem.lootTableItem(block).apply(List.of(2, 3, 4), (count) -> SetItemCountFunction.setCount(ConstantValue.exactly((float) count)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(CandleBlock.CANDLES, count)))))));
    }

    public LootTable.Builder createSegmentedBlockDrops(Block block) {
        LootTable.Builder builder;
        if (block instanceof SegmentableBlock segmentableBlock) {
            builder = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(block, LootItem.lootTableItem(block).apply(IntStream.rangeClosed(1, 4).boxed().toList(), (count) -> SetItemCountFunction.setCount(ConstantValue.exactly((float) count)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(segmentableBlock.getSegmentAmountProperty(), count)))))));
        } else {
            builder = noDrop();
        }

        return builder;
    }

    protected static LootTable.Builder createCandleCakeDrops(Block candle) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(candle)));
    }

    public static LootTable.Builder noDrop() {
        return LootTable.lootTable();
    }

    protected abstract void generate();

    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.stream().filter(block -> this.modId.equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace())).collect(Collectors.toSet());
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        this.generate();
        Set<ResourceKey<LootTable>> seen = new HashSet<>();

        for (Block block : this.getKnownBlocks()) {
            if (block.isEnabled(this.enabledFeatures)) {
                block.getLootTable().ifPresent((lootTable) -> {
                    if (seen.add(lootTable)) {
                        LootTable.Builder builder = this.map.remove(lootTable);
                        if (builder == null) {
                            throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", lootTable.identifier(), BuiltInRegistries.BLOCK.getKey(block)));
                        }

                        output.accept(lootTable, builder);
                    }
                });
            }
        }

        if (!this.map.isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-blocks: " + String.valueOf(this.map.keySet()));
        }
    }

    protected void addNetherVinesDropTable(Block vineBlock, Block plantBlock) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        LootTable.Builder builder = this.createSilkTouchOrShearsDispatchTable(vineBlock, LootItem.lootTableItem(vineBlock).when(BonusLevelTableCondition.bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), 0.33F, 0.55F, 0.77F, 1.0F)));
        this.add(vineBlock, builder);
        this.add(plantBlock, builder);
    }

    protected LootTable.Builder createDoorTable(Block block) {
        return this.createSinglePropConditionTable(block, DoorBlock.HALF, DoubleBlockHalf.LOWER);
    }

    protected void dropPottedContents(Block potted) {
        this.add(potted, block -> this.createPotFlowerItemTable(((FlowerPotBlock) block).getPotted()));
    }

    protected void otherWhenSilkTouch(Block block, Block other) {
        this.add(block, this.createSilkTouchOnlyTable(other));
    }

    protected void dropOther(Block block, ItemLike drop) {
        this.add(block, this.createSingleItemTable(drop));
    }

    protected void dropWhenSilkTouch(Block block) {
        this.otherWhenSilkTouch(block, block);
    }

    protected void dropSelf(Block block) {
        this.dropOther(block, block);
    }

    public void dropNone(Block block) {
        this.add(block, BlockLootSubProvider.noDrop());
    }

    public void dropSelfSlab(Block block) {
        this.add(block, this::createSlabItemTable);
    }

    protected void add(Block block, Function<Block, LootTable.Builder> builder) {
        this.add(block, builder.apply(block));
    }

    protected void add(Block block, LootTable.Builder builder) {
        this.map.put(block.getLootTable().orElseThrow(() -> new IllegalStateException("Block " + block + " does not have loot table")), builder);
    }
}
