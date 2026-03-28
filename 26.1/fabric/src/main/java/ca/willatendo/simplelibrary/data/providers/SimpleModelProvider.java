package ca.willatendo.simplelibrary.data.providers;

import ca.willatendo.simplelibrary.data.providers.model.SimpleBlockModelGenerator;
import ca.willatendo.simplelibrary.data.providers.model.SimpleItemModelGenerator;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class SimpleModelProvider implements DataProvider {
    private final PackOutput.PathProvider blockStatePathProvider;
    private final PackOutput.PathProvider itemInfoPathProvider;
    private final PackOutput.PathProvider modelPathProvider;
    private final String modId;
    private final Function<ItemModelGenerators, SimpleItemModelGenerator> simpleItemModelGenerator;
    private final Function<BlockModelGenerators, SimpleBlockModelGenerator> simpleBlockModelGeneratorFunction;
    private final List<? extends Holder<Block>> forBlocks;
    private final List<? extends Holder<Item>> forItems;

    public SimpleModelProvider(Function<ItemModelGenerators, SimpleItemModelGenerator> simpleItemModelGenerator, Function<BlockModelGenerators, SimpleBlockModelGenerator> simpleBlockModelGeneratorFunction, PackOutput packOutput, String modId) {
        this(simpleItemModelGenerator, simpleBlockModelGeneratorFunction, packOutput, modId, List.of(), List.of());
    }

    private SimpleModelProvider(Function<ItemModelGenerators, SimpleItemModelGenerator> simpleItemModelGenerator, Function<BlockModelGenerators, SimpleBlockModelGenerator> simpleBlockModelGeneratorFunction, PackOutput packOutput, String modId, List<? extends Holder<Block>> forBlocks, List<? extends Holder<Item>> forItems) {
        this.modId = modId;
        this.simpleItemModelGenerator = simpleItemModelGenerator;
        this.simpleBlockModelGeneratorFunction = simpleBlockModelGeneratorFunction;
        if (!forBlocks.isEmpty()) {
            this.forBlocks = forBlocks;
        } else {
            this.forBlocks = List.of();
        }
        if (!forItems.isEmpty()) {
            this.forItems = forItems;
        } else {
            this.forItems = List.of();
        }
        this.blockStatePathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.itemInfoPathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
        this.modelPathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
    }

    private void registerModels(BlockModelGenerators blockModelGenerators, ItemModelGenerators itemModelGenerators) {
        if (this.simpleBlockModelGeneratorFunction != null) {
            this.simpleBlockModelGeneratorFunction.apply(blockModelGenerators).run();
        }
        if (this.simpleItemModelGenerator != null) {
            this.simpleItemModelGenerator.apply(itemModelGenerators).run();
        }
    }

    private Stream<? extends Holder<Block>> getKnownBlocks() {
        if (this.forItems.isEmpty()) {
            return BuiltInRegistries.BLOCK.listElements().filter((holder) -> holder.getKey().identifier().getNamespace().equals(this.modId));
        }
        return BuiltInRegistries.BLOCK.listElements().filter(this.forBlocks::contains);
    }

    private Stream<? extends Holder<Item>> getKnownItems() {
        if (this.forBlocks.isEmpty()) {
            return BuiltInRegistries.ITEM.listElements().filter((holder) -> holder.getKey().identifier().getNamespace().equals(this.modId));
        }
        return BuiltInRegistries.ITEM.listElements().filter(this.forItems::contains);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        SimpleModelProvider.ItemInfoCollector itemInfoCollector = new SimpleModelProvider.ItemInfoCollector(this::getKnownItems);
        SimpleModelProvider.BlockStateGeneratorCollector blockStateGeneratorCollector = new SimpleModelProvider.BlockStateGeneratorCollector(this::getKnownBlocks);
        SimpleModelProvider.SimpleModelCollector simpleModelCollector = new SimpleModelProvider.SimpleModelCollector();
        this.registerModels(new BlockModelGenerators(blockStateGeneratorCollector, itemInfoCollector, simpleModelCollector), new ItemModelGenerators(itemInfoCollector, simpleModelCollector));
        blockStateGeneratorCollector.validate();
        itemInfoCollector.finalizeAndValidate();
        return CompletableFuture.allOf(blockStateGeneratorCollector.save(cachedOutput, this.blockStatePathProvider), simpleModelCollector.save(cachedOutput, this.modelPathProvider), itemInfoCollector.save(cachedOutput, this.itemInfoPathProvider));
    }

    @Override
    public String getName() {
        return "SimpleLibrary: Model Provider for " + this.modId;
    }


    private static class BlockStateGeneratorCollector implements Consumer<BlockModelDefinitionGenerator> {
        private final Map<Block, BlockModelDefinitionGenerator> generators = new HashMap<>();
        private final Supplier<Stream<? extends Holder<Block>>> knownBlocks;

        public BlockStateGeneratorCollector(Supplier<Stream<? extends Holder<Block>>> knownBlocks) {
            this.knownBlocks = knownBlocks;
        }

        @Override
        public void accept(BlockModelDefinitionGenerator blockModelDefinitionGenerator) {
            Block block = blockModelDefinitionGenerator.block();
            BlockModelDefinitionGenerator previous = this.generators.put(block, blockModelDefinitionGenerator);
            if (previous != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        }

        public void validate() {
            Stream<? extends Holder<Block>> knownBlocks = this.knownBlocks.get();
            List<Identifier> missingDefinitions = knownBlocks.filter(blockHolder -> !this.generators.containsKey(blockHolder.value())).map(blockHolder -> blockHolder.unwrapKey().orElseThrow().identifier()).toList();
            if (!missingDefinitions.isEmpty()) {
                throw new IllegalStateException("Missing blockstate definitions for: " + missingDefinitions);
            }
        }

        public CompletableFuture<?> save(CachedOutput cachedOutput, PackOutput.PathProvider pathProvider) {
            Map<Block, BlockStateModelDispatcher> definitions = Maps.transformValues(this.generators, BlockModelDefinitionGenerator::create);
            Function<Block, Path> pathGetter = (block) -> pathProvider.json(block.builtInRegistryHolder().key().identifier());
            return DataProvider.saveAll(cachedOutput, BlockStateModelDispatcher.CODEC, pathGetter, definitions);
        }
    }


    private static class ItemInfoCollector implements ItemModelOutput {
        private final Map<Item, ClientItem> itemInfos = new HashMap<>();
        private final Map<Item, Item> copies = new HashMap<>();
        private final Map<Identifier, ClientItem> idItemInfos = new HashMap<>();
        private final Supplier<Stream<? extends Holder<Item>>> knownItems;

        public ItemInfoCollector(Supplier<Stream<? extends Holder<Item>>> knownItems) {
            this.knownItems = knownItems;
        }

        public void accept(Item item, ItemModel.Unbaked unbaked, ClientItem.Properties properties) {
            this.register(item, new ClientItem(unbaked, properties));
        }

        public void register(Item item, ClientItem itemInfo) {
            ClientItem previous = this.itemInfos.put(item, itemInfo);
            if (previous != null) {
                throw new IllegalStateException("Duplicate item model definition for " + String.valueOf(item));
            }
        }

        public void register(Identifier identifier, ClientItem clientItem) {
            ClientItem existing = this.idItemInfos.putIfAbsent(identifier, clientItem);
            if (existing != null) {
                throw new IllegalStateException("Duplicate item model definition for " + String.valueOf(identifier));
            }
        }

        public void copy(Item donorItem, Item acceptorItem) {
            this.copies.put(acceptorItem, donorItem);
        }

        public void finalizeAndValidate() {
            this.knownItems.get().map(Holder::value).forEach(item -> {
                if (!this.copies.containsKey(item) && item instanceof BlockItem blockItem) {
                    if (!this.itemInfos.containsKey(blockItem)) {
                        Identifier targetModel = ModelLocationUtils.getModelLocation(blockItem.getBlock());
                        this.accept(blockItem, ItemModelUtils.plainModel(targetModel));
                    }
                }
            });
            this.copies.forEach((acceptorItem, donorItem) -> {
                ClientItem donorInfo = this.itemInfos.get(donorItem);
                if (donorInfo == null) {
                    throw new IllegalStateException("Missing donor: " + donorItem + " -> " + acceptorItem);
                } else {
                    this.register(acceptorItem, donorInfo);
                }
            });
            List<Identifier> missingDefinitions = this.knownItems.get().filter(itemHolder -> !this.itemInfos.containsKey(itemHolder.value())).map(itemHolder -> itemHolder.unwrapKey().orElseThrow().identifier()).toList();
            if (!missingDefinitions.isEmpty()) {
                throw new IllegalStateException("Missing item model definitions for: " + missingDefinitions);
            }
        }

        public CompletableFuture<?> save(CachedOutput cachedOutput, PackOutput.PathProvider pathProvider) {
            CompletableFuture<?>[] completableFutures = new CompletableFuture[]{DataProvider.saveAll(cachedOutput, ClientItem.CODEC, (item) -> pathProvider.json(item.builtInRegistryHolder().key().identifier()), this.itemInfos), null};
            Objects.requireNonNull(pathProvider);
            completableFutures[1] = DataProvider.saveAll(cachedOutput, ClientItem.CODEC, pathProvider::json, this.idItemInfos);
            return CompletableFuture.allOf(completableFutures);
        }
    }


    private static class SimpleModelCollector implements BiConsumer<Identifier, ModelInstance> {
        private final Map<Identifier, ModelInstance> models = new HashMap<>();

        @Override
        public void accept(Identifier id, ModelInstance contents) {
            Supplier<JsonElement> previous = this.models.put(id, contents);
            if (previous != null) {
                throw new IllegalStateException("Duplicate model definition for " + id);
            }
        }

        public CompletableFuture<?> save(CachedOutput cachedOutput, PackOutput.PathProvider pathProvider) {
            Objects.requireNonNull(pathProvider);
            return DataProvider.saveAll(cachedOutput, Supplier::get, pathProvider::json, this.models);
        }
    }
}
