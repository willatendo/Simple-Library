package willatendo.simplelibrary.data.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockStateGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import willatendo.simplelibrary.data.model.SimpleModelTemplates;

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
    protected final Consumer<BlockStateGenerator> blockStateOutput;
    protected final BiConsumer<ResourceLocation, ModelInstance> modelOutput;
    private final String modId;

    public SimpleBlockModelGenerator(BlockModelGenerators blockModelGenerators, String modId) {
        this.blockModelGenerators = blockModelGenerators;
        this.itemModelOutput = blockModelGenerators.itemModelOutput;
        this.blockStateOutput = blockModelGenerators.blockStateOutput;
        this.modelOutput = blockModelGenerators.modelOutput;
        this.modId = modId;

        this.blockModelGenerators.fullBlockModelCustomGenerators = this.fullBlockModelCustomGenerators;
    }

    public abstract void run();

    protected ResourceLocation modLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(this.modId, path);
    }

    protected ResourceLocation mcLocation(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    protected void addFullBlockModelCustom(Block block, BlockModelGenerators.BlockStateGeneratorSupplier blockStateGeneratorSupplier) {
        this.fullBlockModelCustomGenerators.put(block, blockStateGeneratorSupplier);
    }

    // Basic Providers
    protected void createBlockFamilies(Stream<BlockFamily> blockFamilies) {
        blockFamilies.filter(BlockFamily::shouldGenerateModel).forEach(blockFamily -> this.family(blockFamily.getBaseBlock()).generateFor(blockFamily));
    }

    protected SimpleBlockModelGenerator.BlockFamilyProvider family(Block baseBlock) {
        TexturedModel texturedModel = this.blockModelGenerators.texturedModels.getOrDefault(baseBlock, TexturedModel.CUBE.get(baseBlock));
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
        ResourceLocation resourcelocation = plantType.getCrossPot().create(pottedBlock, texturemapping, blockModelGenerators.modelOutput);
        this.block(BlockModelGenerators.createSimpleBlock(pottedBlock, resourcelocation));
    }

    protected void createCrossBlock(Block block, SimpleBlockModelGenerator.PlantType plantType) {
        TextureMapping texturemapping = plantType.getTextureMapping(block);
        this.createCrossBlock(block, plantType, texturemapping);
    }

    protected void createCrossBlock(Block block, SimpleBlockModelGenerator.PlantType plantType, TextureMapping textureMapping) {
        ResourceLocation resourcelocation = plantType.getCross().create(block, textureMapping, blockModelGenerators.modelOutput);
        this.block(BlockModelGenerators.createSimpleBlock(block, resourcelocation));
    }

    public void createDoor(Block doorBlock) {
        TextureMapping textureMapping = TextureMapping.door(doorBlock);
        ResourceLocation bottomLeftModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_BOTTOM_LEFT).create(doorBlock, textureMapping, this.modelOutput);
        ResourceLocation bottomLeftOpenModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN).create(doorBlock, textureMapping, this.modelOutput);
        ResourceLocation bottomRightModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_BOTTOM_RIGHT).create(doorBlock, textureMapping, this.modelOutput);
        ResourceLocation bottomRightOpenModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN).create(doorBlock, textureMapping, this.modelOutput);
        ResourceLocation topLeftModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_TOP_LEFT).create(doorBlock, textureMapping, this.modelOutput);
        ResourceLocation topLeftOpenModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_TOP_LEFT_OPEN).create(doorBlock, textureMapping, this.modelOutput);
        ResourceLocation topRightModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_TOP_RIGHT).create(doorBlock, textureMapping, this.modelOutput);
        ResourceLocation topRightOpenModel = SimpleModelTemplates.cutout(ModelTemplates.DOOR_TOP_RIGHT_OPEN).create(doorBlock, textureMapping, this.modelOutput);
        this.registerSimpleFlatItemModel(doorBlock.asItem());
        this.block(BlockModelGenerators.createDoor(doorBlock, bottomLeftModel, bottomLeftOpenModel, bottomRightModel, bottomRightOpenModel, topLeftModel, topLeftOpenModel, topRightModel, topRightOpenModel));
    }

    public void createOrientableTrapdoor(Block orientableTrapdoorBlock) {
        TextureMapping textureMapping = TextureMapping.defaultTexture(orientableTrapdoorBlock);
        ResourceLocation topModel = SimpleModelTemplates.cutout(ModelTemplates.ORIENTABLE_TRAPDOOR_TOP).create(orientableTrapdoorBlock, textureMapping, this.modelOutput);
        ResourceLocation bottomModel = SimpleModelTemplates.cutout(ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM).create(orientableTrapdoorBlock, textureMapping, this.modelOutput);
        ResourceLocation openModel = SimpleModelTemplates.cutout(ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN).create(orientableTrapdoorBlock, textureMapping, this.modelOutput);
        this.block(BlockModelGenerators.createOrientableTrapdoor(orientableTrapdoorBlock, topModel, bottomModel, openModel));
        this.registerSimpleItemModel(orientableTrapdoorBlock, bottomModel);
    }

    public void createTrapdoor(Block trapdoorBlock) {
        TextureMapping textureMapping = TextureMapping.defaultTexture(trapdoorBlock);
        ResourceLocation topModel = SimpleModelTemplates.cutout(ModelTemplates.TRAPDOOR_TOP).create(trapdoorBlock, textureMapping, this.modelOutput);
        ResourceLocation bottomModel = SimpleModelTemplates.cutout(ModelTemplates.TRAPDOOR_BOTTOM).create(trapdoorBlock, textureMapping, this.modelOutput);
        ResourceLocation openModel = SimpleModelTemplates.cutout(ModelTemplates.TRAPDOOR_OPEN).create(trapdoorBlock, textureMapping, this.modelOutput);
        this.block(BlockModelGenerators.createTrapdoor(trapdoorBlock, topModel, bottomModel, openModel));
        this.registerSimpleItemModel(trapdoorBlock, bottomModel);
    }

    public void registerSimpleFlatItemModel(Item item) {
        this.registerSimpleItemModel(item, this.blockModelGenerators.createFlatItemModel(item));
    }

    public void registerSimpleItemModel(Block block, ResourceLocation model) {
        this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
    }

    public void registerSimpleItemModel(Item item, ResourceLocation model) {
        this.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
    }

    protected void block(BlockStateGenerator blockStateGenerator) {
        this.blockStateOutput.accept(blockStateGenerator);
    }

    public final class BlockFamilyProvider {
        private final TextureMapping textureMapping;
        private final Map<ModelTemplate, ResourceLocation> models = Maps.newHashMap();
        private BlockFamily family;
        private ResourceLocation fullBlock;
        private final Set<Block> skipGeneratingModelsFor = new HashSet<>();

        public BlockFamilyProvider(TextureMapping textureMapping) {
            this.textureMapping = textureMapping;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider fullBlock(Block block, ModelTemplate modelTemplate) {
            this.fullBlock = modelTemplate.create(block, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            if (SimpleBlockModelGenerator.this.blockModelGenerators.fullBlockModelCustomGenerators.containsKey(block)) {
                SimpleBlockModelGenerator.this.blockStateOutput.accept((SimpleBlockModelGenerator.this.blockModelGenerators.fullBlockModelCustomGenerators.get(block)).create(block, this.fullBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput));
            } else {
                SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, this.fullBlock));
            }

            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider donateModelTo(Block sourceBlock, Block block) {
            ResourceLocation model = ModelLocationUtils.getModelLocation(sourceBlock);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
            SimpleBlockModelGenerator.this.blockModelGenerators.itemModelOutput.copy(sourceBlock.asItem(), block.asItem());
            this.skipGeneratingModelsFor.add(block);
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider button(Block buttonBlock) {
            ResourceLocation buttonModel = ModelTemplates.BUTTON.create(buttonBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation pressedButtonModel = ModelTemplates.BUTTON_PRESSED.create(buttonBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createButton(buttonBlock, buttonModel, pressedButtonModel));
            ResourceLocation ButtonInventoryModel = ModelTemplates.BUTTON_INVENTORY.create(buttonBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleItemModel(buttonBlock, ButtonInventoryModel);
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider wall(Block wallBlock) {
            ResourceLocation wallPostModel = ModelTemplates.WALL_POST.create(wallBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation lowWallSideModel = ModelTemplates.WALL_LOW_SIDE.create(wallBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation tallWallSideModel = ModelTemplates.WALL_TALL_SIDE.create(wallBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createWall(wallBlock, wallPostModel, lowWallSideModel, tallWallSideModel));
            ResourceLocation wallInventoryModel = ModelTemplates.WALL_INVENTORY.create(wallBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleItemModel(wallBlock, wallInventoryModel);
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider customFence(Block fenceBlock) {
            TextureMapping customParticleTextureMapping = TextureMapping.customParticle(fenceBlock);
            ResourceLocation fencePostModel = ModelTemplates.CUSTOM_FENCE_POST.create(fenceBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation northFenceSideModel = ModelTemplates.CUSTOM_FENCE_SIDE_NORTH.create(fenceBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation eastFenceSideModel = ModelTemplates.CUSTOM_FENCE_SIDE_EAST.create(fenceBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation southFenceSideModel = ModelTemplates.CUSTOM_FENCE_SIDE_SOUTH.create(fenceBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation westFenceSideModel = ModelTemplates.CUSTOM_FENCE_SIDE_WEST.create(fenceBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createCustomFence(fenceBlock, fencePostModel, northFenceSideModel, eastFenceSideModel, southFenceSideModel, westFenceSideModel));
            ResourceLocation fenceInventoryModel = ModelTemplates.CUSTOM_FENCE_INVENTORY.create(fenceBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleItemModel(fenceBlock, fenceInventoryModel);
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider fence(Block fenceBlock) {
            ResourceLocation fencePostModel = ModelTemplates.FENCE_POST.create(fenceBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation fenceSideModel = ModelTemplates.FENCE_SIDE.create(fenceBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createFence(fenceBlock, fencePostModel, fenceSideModel));
            ResourceLocation fenceInventoryModel = ModelTemplates.FENCE_INVENTORY.create(fenceBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleItemModel(fenceBlock, fenceInventoryModel);
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider customFenceGate(Block customFenceGateBlock) {
            TextureMapping customParticleTextureMapping = TextureMapping.customParticle(customFenceGateBlock);
            ResourceLocation openModel = ModelTemplates.CUSTOM_FENCE_GATE_OPEN.create(customFenceGateBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation closedModel = ModelTemplates.CUSTOM_FENCE_GATE_CLOSED.create(customFenceGateBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation openWallModel = ModelTemplates.CUSTOM_FENCE_GATE_WALL_OPEN.create(customFenceGateBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation closedWallModel = ModelTemplates.CUSTOM_FENCE_GATE_WALL_CLOSED.create(customFenceGateBlock, customParticleTextureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(customFenceGateBlock, openModel, closedModel, openWallModel, closedWallModel, false));
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider fenceGate(Block fenceGateBlock) {
            ResourceLocation openModel = ModelTemplates.FENCE_GATE_OPEN.create(fenceGateBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation closedModel = ModelTemplates.FENCE_GATE_CLOSED.create(fenceGateBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation openWallModel = ModelTemplates.FENCE_GATE_WALL_OPEN.create(fenceGateBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation closedWallModel = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(fenceGateBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(fenceGateBlock, openModel, closedModel, openWallModel, closedWallModel, true));
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider pressurePlate(Block pressurePlateBlock) {
            ResourceLocation upModel = ModelTemplates.PRESSURE_PLATE_UP.create(pressurePlateBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            ResourceLocation downModel = ModelTemplates.PRESSURE_PLATE_DOWN.create(pressurePlateBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createPressurePlate(pressurePlateBlock, upModel, downModel));
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider sign(Block signBlock) {
            if (this.family == null) {
                throw new IllegalStateException("Family not defined");
            } else {
                Block wallSignBlock = this.family.getVariants().get(BlockFamily.Variant.WALL_SIGN);
                ResourceLocation model = ModelTemplates.PARTICLE_ONLY.create(signBlock, this.textureMapping, SimpleBlockModelGenerator.this.modelOutput);
                SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(signBlock, model));
                SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(wallSignBlock, model));
                SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleFlatItemModel(signBlock.asItem());
                return this;
            }
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider slab(Block slabBlock) {
            if (this.fullBlock == null) {
                throw new IllegalStateException("Full block not generated yet");
            } else {
                ResourceLocation bottomModel = this.getOrCreateModel(ModelTemplates.SLAB_BOTTOM, slabBlock);
                ResourceLocation topModel = this.getOrCreateModel(ModelTemplates.SLAB_TOP, slabBlock);
                SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createSlab(slabBlock, bottomModel, topModel, this.fullBlock));
                SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleItemModel(slabBlock, bottomModel);
                return this;
            }
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider stairs(Block stairsBlock) {
            ResourceLocation innerModel = this.getOrCreateModel(ModelTemplates.STAIRS_INNER, stairsBlock);
            ResourceLocation straightModel = this.getOrCreateModel(ModelTemplates.STAIRS_STRAIGHT, stairsBlock);
            ResourceLocation outerModel = this.getOrCreateModel(ModelTemplates.STAIRS_OUTER, stairsBlock);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createStairs(stairsBlock, innerModel, straightModel, outerModel));
            SimpleBlockModelGenerator.this.blockModelGenerators.registerSimpleItemModel(stairsBlock, straightModel);
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider fullBlockVariant(Block block) {
            TexturedModel defaultTexturedModel = SimpleBlockModelGenerator.this.blockModelGenerators.texturedModels.getOrDefault(block, TexturedModel.CUBE.get(block));
            ResourceLocation defaultModel = defaultTexturedModel.create(block, SimpleBlockModelGenerator.this.blockModelGenerators.modelOutput);
            SimpleBlockModelGenerator.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, defaultModel));
            return this;
        }

        public SimpleBlockModelGenerator.BlockFamilyProvider door(Block doorBlock) {
            SimpleBlockModelGenerator.this.createDoor(doorBlock);
            return this;
        }

        public void trapdoor(Block trapdoorBlock) {
            if (SimpleBlockModelGenerator.this.blockModelGenerators.nonOrientableTrapdoor.contains(trapdoorBlock)) {
                SimpleBlockModelGenerator.this.createTrapdoor(trapdoorBlock);
            } else {
                SimpleBlockModelGenerator.this.createOrientableTrapdoor(trapdoorBlock);
            }
        }

        public ResourceLocation getOrCreateModel(ModelTemplate modelTemplate, Block block) {
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

        public ResourceLocation createItemModel(BlockModelGenerators generator, Block block) {
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
