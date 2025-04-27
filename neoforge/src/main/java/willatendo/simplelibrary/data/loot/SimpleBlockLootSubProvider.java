package willatendo.simplelibrary.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class SimpleBlockLootSubProvider extends BlockLootSubProvider {
    private final String modId;

    public SimpleBlockLootSubProvider(HolderLookup.Provider registries, String modId) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        this.modId = modId;
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.stream().filter(block -> this.modId.equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace())).collect(Collectors.toSet());
    }

    public void dropNone(Block block) {
        this.add(block, BlockLootSubProvider.noDrop());
    }

    public void dropSelfSlab(Block block) {
        this.add(block, this::createSlabItemTable);
    }
}
