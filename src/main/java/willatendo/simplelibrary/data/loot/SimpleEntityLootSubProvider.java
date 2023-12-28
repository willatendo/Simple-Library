package willatendo.simplelibrary.data.loot;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import com.google.common.collect.Sets;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLootTableProvider;
import net.fabricmc.fabric.impl.datagen.loot.FabricLootTableProviderImpl;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public abstract class SimpleEntityLootSubProvider extends EntityLootSubProvider implements FabricLootTableProvider {
	private final FabricDataOutput fabricDataOutput;
	private final Set<ResourceLocation> excludedFromStrictValidation = new HashSet<>();

	public SimpleEntityLootSubProvider(FabricDataOutput fabricDataOutput) {
		super(FeatureFlags.REGISTRY.allFlags());
		this.fabricDataOutput = fabricDataOutput;
	}

	public void excludeFromStrictValidation(Block block) {
		this.excludedFromStrictValidation.add(BuiltInRegistries.BLOCK.getKey(block));
	}

	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
		this.generate();

		for (Entry<EntityType<?>, Map<ResourceLocation, Builder>> entityEntry : this.map.entrySet()) {
			for (Entry<ResourceLocation, Builder> entityEntries : entityEntry.getValue().entrySet()) {
				ResourceLocation identifier = entityEntries.getKey();

				if (identifier.equals(BuiltInLootTables.EMPTY)) {
					continue;
				}

				biConsumer.accept(identifier, entityEntries.getValue());
			}
		}

		if (this.fabricDataOutput.isStrictValidationEnabled()) {
			Set<ResourceLocation> missing = Sets.newHashSet();

			for (ResourceLocation blockId : BuiltInRegistries.BLOCK.keySet()) {
				if (blockId.getNamespace().equals(this.fabricDataOutput.getModId())) {
					ResourceLocation blockLootTableId = BuiltInRegistries.BLOCK.get(blockId).getLootTable();

					if (blockLootTableId.getNamespace().equals(this.fabricDataOutput.getModId())) {
						for (Entry<EntityType<?>, Map<ResourceLocation, Builder>> entityEntry : this.map.entrySet()) {
							if (!entityEntry.getValue().containsKey(blockLootTableId)) {
								missing.add(blockId);
							}
						}
					}
				}
			}

			missing.removeAll(this.excludedFromStrictValidation);

			if (!missing.isEmpty()) {
				throw new IllegalStateException("Missing loot table(s) for %s".formatted(missing));
			}
		}
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		return FabricLootTableProviderImpl.run(cachedOutput, this, LootContextParamSets.ENTITY, this.fabricDataOutput);
	}

	@Override
	public String getName() {
		return "EntityType Loot Tables";
	}
}
