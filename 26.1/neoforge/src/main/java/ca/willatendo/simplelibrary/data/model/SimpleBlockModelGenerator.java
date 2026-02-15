package ca.willatendo.simplelibrary.data.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class SimpleBlockModelGenerator {
    public static final Map<BlockFamily.Variant, BiConsumer<SimpleBlockModelGenerator.BlockFamilyProvider, Block>> SHAPE_CONSUMERS = ImmutableMap.<BlockFamily.Variant, BiConsumer<SimpleBlockModelGenerator.BlockFamilyProvider, Block>>builder().put(BlockFamily.Variant.BUTTON, SimpleBlockModelGenerator.BlockFamilyProvider::button).put(BlockFamily.Variant.DOOR, SimpleBlockModelGenerator.BlockFamilyProvider::door).put(BlockFamily.Variant.CHISELED, SimpleBlockModelGenerator.BlockFamilyProvider::fullBlockVariant).put(BlockFamily.Variant.CRACKED, SimpleBlockModelGenerator.BlockFamilyProvider::fullBlockVariant).put(BlockFamily.Variant.CUSTOM_FENCE, SimpleBlockModelGenerator.BlockFamilyProvider::customFence).put(BlockFamily.Variant.FENCE, SimpleBlockModelGenerator.BlockFamilyProvider::fence).put(BlockFamily.Variant.CUSTOM_FENCE_GATE, SimpleBlockModelGenerator.BlockFamilyProvider::customFenceGate).put(BlockFamily.Variant.FENCE_GATE, SimpleBlockModelGenerator.BlockFamilyProvider::fenceGate).put(BlockFamily.Variant.SIGN, SimpleBlockModelGenerator.BlockFamilyProvider::sign).put(BlockFamily.Variant.SLAB, SimpleBlockModelGenerator.BlockFamilyProvider::slab).put(BlockFamily.Variant.STAIRS, SimpleBlockModelGenerator.BlockFamilyProvider::stairs).put(BlockFamily.Variant.PRESSURE_PLATE, SimpleBlockModelGenerator.BlockFamilyProvider::pressurePlate).put(BlockFamily.Variant.TRAPDOOR, SimpleBlockModelGenerator.BlockFamilyProvider::trapdoor).put(BlockFamily.Variant.WALL, SimpleBlockModelGenerator.BlockFamilyProvider::wall).build();
    private final Map<Block, BlockModelGenerators.BlockStateGeneratorSupplier> fullBlockModelCustomGenerators = new HashMap<>();
    protected final BlockModelGenerators blockModelGenerators;
    protected final ItemModelOutput itemModelOutput;
    protected final Consumer<BlockModelDefinitionGenerator> blockStateOutput;
    protected final BiConsumer<Identifier, ModelInstance> modelOutput;
    private final String modId;

    public SimpleBlockModelGenerator(BlockModelGenerators blockModelGenerators, String modId) {
        this.blockModelGenerators = blockModelGenerators;
        this.itemModelOutput = blockModelGenerators.itemModelOutput;
        this.blockStateOutput = blockModelGenerators.blockStateOutput;
        this.modelOutput = blockModelGenerators.modelOutput;
        this.modId = modId;

        BlockModelGenerators.FULL_BLOCK_MODEL_CUSTOM_GENERATORS.clear();
        BlockModelGenerators.FULL_BLOCK_MODEL_CUSTOM_GENERATORS.putAll(this.fullBlockModelCustomGenerators);
    }

    public abstract void run();

    protected Identifier modLocation(String path) {
        return Identifier.fromNamespaceAndPath(this.modId, path);
    }

    protected Identifier mcLocation(String path) {
        return Identifier.withDefaultNamespace(path);
    }

    protected void addFullBlockModelCustom(Block block, BlockModelGenerators.BlockStateGeneratorSupplier blockStateGeneratorSupplier) {
        this.fullBlockModelCustomGenerators.put(block, blockStateGeneratorSupplier);
    }

    // Basic Providers
    protected void createBlockFamilies(Stream<BlockFamily> blockFamilies) {
        blockFamilies.filter(BlockFamily::shouldGenerateModel).forEach(blockFamily -> this.family(blockFamily.getBaseBlock()).generateFor(blockFamily));
    }

    protected SimpleBlockModelGenerator.BlockFamilyProvider family(Block baseBlock) {
        TexturedModel texturedModel = BlockModelGenerators.TEXTURED_MODELS.getOrDefault(baseBlock, TexturedModel.CUBE.get(baseBlock));
        return new SimpleBlockModelGenerator.BlockFamilyProvider(texturedModel.getMapping()).fullBlock(baseBlock, texturedModel.getTemplate());
    }

    public void createTrivialCube(Block block) {
        this.blockModelGenerators.createTrivialBlock(block, TexturedModel.CUBE);
    }

    protected void createPlantWithDefaultItem(Block block, Block pottedBlock, SimpleBlockModelGenerator.PlantType plantType) {
        this.registerSimpleItemModel(block.asItem(), plantType.createItemModel(this.blockModelGenerators, block));
        this.createPlant(block, pottedBlock, plantType);
    }

    protected void createPlant(Block block, Block pottedBlock, SimpleBlockModelGenerator.PlantType plantType) {
        this.createCrossBlock(block, plantType);
        TextureMapping texturemapping = plantType.getPlantTextureMapping(block);
        Identifier identifier = plantType.getCrossPot().create(pottedBlock, texturemapping, blockModelGenerators.modelOutput);
        this.block(BlockModelGenerators.createSimpleBlock(pottedBlock, BlockModelGenerators.plainVariant(identifier)));
    }

    protected void createCrossBlock(Block block, SimpleBlockModelGenerator.PlantType plantType) {
        TextureMapping texturemapping = plantType.getTextureMapping(block);
        this.createCrossBlock(block, plantType, texturemapping);
    }

    protected void createCrossBlock(Block block, SimpleBlockModelGenerator.PlantType plantType, TextureMapping textureMapping) {
        Identifier identifier = plantType.getCross().create(block, textureMapping, blockModelGenerators.modelOutput);
        this.block(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(identifier)));
    }

    public void createDoor(Block doorBlock) {
        TextureMapping textureMapping = TextureMapping.door(doorBlock);
        Identifier bottomLeftModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_BOTTOM_LEFT).create(doorBlock, textureMapping, this.modelOutput);
        Identifier bottomLeftOpenModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN).create(doorBlock, textureMapping, this.modelOutput);
        Identifier bottomRightModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_BOTTOM_RIGHT).create(doorBlock, textureMapping, this.modelOutput);
        Identifier bottomRightOpenModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN).create(doorBlock, textureMapping, this.modelOutput);
        Identifier topLeftModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_TOP_LEFT).create(doorBlock, textureMapping, this.modelOutput);
        Identifier topLeftOpenModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_TOP_LEFT_OPEN).create(doorBlock, textureMapping, this.modelOutput);
        Identifier topRightModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_TOP_RIGHT).create(doorBlock, textureMapping, this.modelOutput);
        Identifier topRightOpenModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_TOP_RIGHT_OPEN).create(doorBlock, textureMapping, this.modelOutput);
        this.registerSimpleFlatItemModel(doorBlock.asItem());
        this.block(BlockModelGenerators.createDoor(doorBlock, BlockModelGenerators.plainVariant(bottomLeftModel), BlockModelGenerators.plainVariant(bottomLeftOpenModel), BlockModelGenerators.plainVariant(bottomRightModel), BlockModelGenerators.plainVariant(bottomRightOpenModel), BlockModelGenerators.plainVariant(topLeftModel), BlockModelGenerators.plainVariant(topLeftOpenModel), BlockModelGenerators.plainVariant(topRightModel), BlockModelGenerators.plainVariant(topRightOpenModel)));
    }

    public void createOrientableTrapdoor(Block orientableTrapdoorBlock) {
        TextureMapping textureMapping = TextureMapping.defaultTexture(orientableTrapdoorBlock);
        Identifier topModel = SimpleModelTemplates.cutout(ModelTemplates.ORIENTABLE_TRAPDOOR_TOP).create(orientableTrapdoorBlock, textureMapping, this.modelOutput);
        Identifier bottomModel = SimpleModelTemplates.cutout(ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM).create(orientableTrapdoorBlock, textureMapping, this.modelOutput);
        Identifier openModel = SimpleModelTemplates.cutout(ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN).create(orientableTrapdoorBlock, textureMapping, this.modelOutput);
        this.block(BlockModelGenerators.createOrientableTrapdoor(orientableTrapdoorBlock, BlockModelGenerators.plainVariant(topModel), BlockModelGenerators.plainVariant(bottomModel), BlockModelGenerators.plainVariant(openModel)));
        this.registerSimpleItemModel(orientableTrapdoorBlock, bottomModel);
    }

    public void createTrapdoor(Block trapdoorBlock) {
        TextureMapping textureMapping = TextureMapping.defaultTexture(trapdoorBlock);
        Identifier topModel = SimpleModelTemplates.cutout(ModelTemplates.TRAPDOOR_TOP).create(trapdoorBlock, textureMapping, this.modelOutput);
        Identifier bottomModel = SimpleModelTemplates.cutout(ModelTemplates.TRAPDOOR_BOTTOM).create(trapdoorBlock, textureMapping, this.modelOutput);
        Identifier openModel = SimpleModelTemplates.cutout(ModelTemplates.TRAPDOOR_OPEN).create(trapdoorBlock, textureMapping, this.modelOutput);
        this.block(BlockModelGenerators.createTrapdoor(trapdoorBlock, BlockModelGenerators.plainVariant(topModel), BlockModelGenerators.plainVariant(bottomModel), BlockModelGenerators.plainVariant(openModel)));
        this.registerSimpleItemModel(trapdoorBlock, bottomModel);
    }

    public void registerSimpleFlatItemModel(Item item) {
        this.registerSimpleItemModel(item, this.blockModelGenerators.createFlatItemModel(item));
    }

    public void registerSimpleItemModel(Block block, Identifier model) {
        this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
    }

    public void registerSimpleItemModel(Item item, Identifier model) {
        this.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
    }

    protected void block(BlockModelDefinitionGenerator blockModelDefinitionGenerator) {
        this.blockStateOutput.accept(blockModelDefinitionGenerator);
    }

    public final class BlockFamilyProvider {
        private final TextureMapping textureMapping;
        private final Map<ModelTemplate, Identifier> models = Maps.newHashMap();
        private BlockFamily family;
        private Variant fullBlock;
        private final Set<Block> skipGeneratingModelsFor = new HashSet<>();

        public BlockFamilyProvider(TextureMapping textureMapping) {
            this.textureMapping = textureMapping;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider fullBlock(Block block, ModelTemplate modelTemplate) {
            this.fullBlock = BlockModelGenerators.plainModel(modelTemplate.create(block, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput));
            if (BlockModelGenerators.FULL_BLOCK_MODEL_CUSTOM_GENERATORS.containsKey(block)) {
                SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.FULL_BLOCK_MODEL_CUSTOM_GENERATORS.get(block).create(block, this.fullBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput));
            } else {
                SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.variant(this.fullBlock)));
            }

            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider donateModelTo(Block sourceBlock, Block block) {
            Identifier identifier = ModelLocationUtils.getModelLocation(sourceBlock);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(identifier)));
            SimpleBlockModelGenerator.this.itemModelOutput.copy(sourceBlock.asItem(), block.asItem());
            this.skipGeneratingModelsFor.add(block);
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider button(Block buttonBlock) {
            Identifier buttonModel = ModelTemplates.BUTTON.create(buttonBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier pressedButtonModel = ModelTemplates.BUTTON_PRESSED.create(buttonBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createButton(buttonBlock, BlockModelGenerators.plainVariant(buttonModel), BlockModelGenerators.plainVariant(pressedButtonModel)));
            Identifier ButtonInventoryModel = ModelTemplates.BUTTON_INVENTORY.create(buttonBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleItemModel(buttonBlock, ButtonInventoryModel);
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider wall(Block wallBlock) {
            Identifier wallPostModel = ModelTemplates.WALL_POST.create(wallBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier lowWallSideModel = ModelTemplates.WALL_LOW_SIDE.create(wallBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier tallWallSideModel = ModelTemplates.WALL_TALL_SIDE.create(wallBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createWall(wallBlock, BlockModelGenerators.plainVariant(wallPostModel), BlockModelGenerators.plainVariant(lowWallSideModel), BlockModelGenerators.plainVariant(tallWallSideModel)));
            Identifier wallInventoryModel = ModelTemplates.WALL_INVENTORY.create(wallBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleItemModel(wallBlock, wallInventoryModel);
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider customFence(Block fenceBlock) {
            TextureMapping customParticleTextureMapping = TextureMapping.customParticle(fenceBlock);
            Identifier fencePostModel = ModelTemplates.CUSTOM_FENCE_POST.create(fenceBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier northFenceSideModel = ModelTemplates.CUSTOM_FENCE_SIDE_NORTH.create(fenceBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier eastFenceSideModel = ModelTemplates.CUSTOM_FENCE_SIDE_EAST.create(fenceBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier southFenceSideModel = ModelTemplates.CUSTOM_FENCE_SIDE_SOUTH.create(fenceBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier westFenceSideModel = ModelTemplates.CUSTOM_FENCE_SIDE_WEST.create(fenceBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createCustomFence(fenceBlock, BlockModelGenerators.plainVariant(fencePostModel), BlockModelGenerators.plainVariant(northFenceSideModel), BlockModelGenerators.plainVariant(eastFenceSideModel), BlockModelGenerators.plainVariant(southFenceSideModel), BlockModelGenerators.plainVariant(westFenceSideModel)));
            Identifier fenceInventoryModel = ModelTemplates.CUSTOM_FENCE_INVENTORY.create(fenceBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleItemModel(fenceBlock, fenceInventoryModel);
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider fence(Block fenceBlock) {
            Identifier fencePostModel = ModelTemplates.FENCE_POST.create(fenceBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier fenceSideModel = ModelTemplates.FENCE_SIDE.create(fenceBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createFence(fenceBlock, BlockModelGenerators.plainVariant(fencePostModel), BlockModelGenerators.plainVariant(fenceSideModel)));
            Identifier fenceInventoryModel = ModelTemplates.FENCE_INVENTORY.create(fenceBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleItemModel(fenceBlock, fenceInventoryModel);
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider customFenceGate(Block customFenceGateBlock) {
            TextureMapping customParticleTextureMapping = TextureMapping.customParticle(customFenceGateBlock);
            Identifier openModel = ModelTemplates.CUSTOM_FENCE_GATE_OPEN.create(customFenceGateBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier closedModel = ModelTemplates.CUSTOM_FENCE_GATE_CLOSED.create(customFenceGateBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier openWallModel = ModelTemplates.CUSTOM_FENCE_GATE_WALL_OPEN.create(customFenceGateBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier closedWallModel = ModelTemplates.CUSTOM_FENCE_GATE_WALL_CLOSED.create(customFenceGateBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(customFenceGateBlock, BlockModelGenerators.plainVariant(openModel), BlockModelGenerators.plainVariant(closedModel), BlockModelGenerators.plainVariant(openWallModel), BlockModelGenerators.plainVariant(closedWallModel), false));
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider fenceGate(Block fenceGateBlock) {
            Identifier openModel = ModelTemplates.FENCE_GATE_OPEN.create(fenceGateBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier closedModel = ModelTemplates.FENCE_GATE_CLOSED.create(fenceGateBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier openWallModel = ModelTemplates.FENCE_GATE_WALL_OPEN.create(fenceGateBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier closedWallModel = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(fenceGateBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(fenceGateBlock, BlockModelGenerators.plainVariant(openModel), BlockModelGenerators.plainVariant(closedModel), BlockModelGenerators.plainVariant(openWallModel), BlockModelGenerators.plainVariant(closedWallModel), true));
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider pressurePlate(Block pressurePlateBlock) {
            Identifier upModel = ModelTemplates.PRESSURE_PLATE_UP.create(pressurePlateBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            Identifier downModel = ModelTemplates.PRESSURE_PLATE_DOWN.create(pressurePlateBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createPressurePlate(pressurePlateBlock, BlockModelGenerators.plainVariant(upModel), BlockModelGenerators.plainVariant(downModel)));
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider sign(Block signBlock) {
            if (this.family == null) {
                throw new IllegalStateException("Family not defined");
            } else {
                Block wallSignBlock = this.family.getVariants().get(BlockFamily.Variant.WALL_SIGN);
                Identifier model = ModelTemplates.PARTICLE_ONLY.create(signBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
                SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(signBlock, BlockModelGenerators.plainVariant(model)));
                SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(wallSignBlock, BlockModelGenerators.plainVariant(model)));
                SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleFlatItemModel(signBlock.asItem());
                return this;
            }
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider slab(Block slabBlock) {
            if (this.fullBlock == null) {
                throw new IllegalStateException("Full block not generated yet");
            } else {
                Identifier bottomModel = this.getOrCreateModel(ModelTemplates.SLAB_BOTTOM, slabBlock);
                Identifier topModel = this.getOrCreateModel(ModelTemplates.SLAB_TOP, slabBlock);
                SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createSlab(slabBlock, BlockModelGenerators.plainVariant(bottomModel), BlockModelGenerators.plainVariant(topModel), BlockModelGenerators.variant(this.fullBlock)));
                SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleItemModel(slabBlock, bottomModel);
                return this;
            }
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider stairs(Block stairsBlock) {
            Identifier innerModel = this.getOrCreateModel(ModelTemplates.STAIRS_INNER, stairsBlock);
            Identifier straightModel = this.getOrCreateModel(ModelTemplates.STAIRS_STRAIGHT, stairsBlock);
            Identifier outerModel = this.getOrCreateModel(ModelTemplates.STAIRS_OUTER, stairsBlock);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createStairs(stairsBlock, BlockModelGenerators.plainVariant(innerModel), BlockModelGenerators.plainVariant(straightModel), BlockModelGenerators.plainVariant(outerModel)));
            SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleItemModel(stairsBlock, straightModel);
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider fullBlockVariant(Block block) {
            TexturedModel texturedModel = BlockModelGenerators.TEXTURED_MODELS.getOrDefault(block, TexturedModel.CUBE.get(block));
            Identifier defaultModel = texturedModel.create(block, SimpleBlockModelGenerator.this.blockModelGenerators.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(defaultModel)));
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider door(Block doorBlock) {
            SimpleBlockModelGenerator.this.createDoor(doorBlock);
            return this;
        }

        public void trapdoor(Block trapdoorBlock) {
            if (BlockModelGenerators.NON_ORIENTABLE_TRAPDOOR.contains(trapdoorBlock)) {
                SimpleBlockModelGenerator.this.createTrapdoor(trapdoorBlock);
            } else {
                SimpleBlockModelGenerator.this.createOrientableTrapdoor(trapdoorBlock);
            }
        }

        public Identifier getOrCreateModel(ModelTemplate modelTemplate, Block block) {
            return this.models.computeIfAbsent(modelTemplate, computeModelTemplate -> computeModelTemplate.create(block, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput));
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider generateFor(BlockFamily blockFamily) {
            this.family = blockFamily;
            blockFamily.getVariants().forEach((variant, block) -> {
                if (!this.skipGeneratingModelsFor.contains(block)) {
                    BiConsumer<SimpleBlockModelGenerator.BlockFamilyProvider, Block> consumer = SimpleBlockModelGenerator.SHAPE_CONSUMERS.get(variant);
                    if (consumer != null) {
                        consumer.accept(this, block);
                    }
                }
            });
            return this;
        }
    }

    public enum PlantType {
        TINTED(SimpleModelTemplates.cutout(ModelTemplates.TINTED_CROSS), SimpleModelTemplates.cutout(ModelTemplates.TINTED_FLOWER_POT_CROSS), false),
        NOT_TINTED(SimpleModelTemplates.cutout(ModelTemplates.CROSS), SimpleModelTemplates.cutout(ModelTemplates.FLOWER_POT_CROSS), false),
        EMISSIVE_NOT_TINTED(SimpleModelTemplates.cutout(ModelTemplates.CROSS_EMISSIVE), SimpleModelTemplates.cutout(ModelTemplates.FLOWER_POT_CROSS_EMISSIVE), true);

        private final ModelTemplate blockTemplate;
        private final ModelTemplate flowerPotTemplate;
        private final boolean isEmissive;

        PlantType(ModelTemplate blockTemplate, ModelTemplate flowerPotTemplate, boolean isEmissive) {
            this.blockTemplate = blockTemplate;
            this.flowerPotTemplate = flowerPotTemplate;
            this.isEmissive = isEmissive;
        }

        public ModelTemplate getCross() {
            return this.blockTemplate;
        }

        public ModelTemplate getCrossPot() {
            return this.flowerPotTemplate;
        }

        public Identifier createItemModel(BlockModelGenerators generator, Block block) {
            Item item = block.asItem();
            return this.isEmissive ? generator.createFlatItemModelWithBlockTextureAndOverlay(item, block, "_emissive") : generator.createFlatItemModelWithBlockTexture(item, block);
        }

        public TextureMapping getTextureMapping(Block block) {
            return this.isEmissive ? TextureMapping.crossEmissive(block) : TextureMapping.cross(block);
        }

        public TextureMapping getPlantTextureMapping(Block block) {
            return this.isEmissive ? TextureMapping.plantEmissive(block) : TextureMapping.plant(block);
        }
    }
}
