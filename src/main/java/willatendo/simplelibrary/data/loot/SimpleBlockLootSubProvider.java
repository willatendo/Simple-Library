package willatendo.simplelibrary.data.loot;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import willatendo.simplelibrary.server.registry.RegistryHolder;
import willatendo.simplelibrary.server.registry.SimpleRegistry;

public abstract class SimpleBlockLootSubProvider extends BlockLootSubProvider {
	public SimpleBlockLootSubProvider() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	abstract SimpleRegistry<Block> getBlockRegistry();

	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
		this.generate();
		HashSet<ResourceLocation> set = new HashSet<ResourceLocation>();
		for (RegistryHolder<? extends Block> block : this.getBlockRegistry().getEntries()) {
			ResourceLocation resourceLocation;
			if (!block.get().isEnabled(this.enabledFeatures) || (resourceLocation = block.get().getLootTable()) == BuiltInLootTables.EMPTY || !set.add(resourceLocation))
				continue;
			LootTable.Builder builder = this.map.remove(resourceLocation);
			if (builder == null) {
				throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", resourceLocation, BuiltInRegistries.BLOCK.getKey(block.get())));
			}
			biConsumer.accept(resourceLocation, builder);
		}
		if (!this.map.isEmpty()) {
			throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
		}
	}
}
