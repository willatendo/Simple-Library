package willatendo.simplelibrary.data.loot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import willatendo.simplelibrary.server.registry.SimpleRegistry;

public abstract class SimpleEntityLootSubProvider implements LootTableSubProvider {
	protected static final EntityPredicate.Builder ENTITY_ON_FIRE = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true));
	private static final Set<EntityType<?>> SPECIAL_LOOT_TABLE_TYPES = ImmutableSet.of(EntityType.PLAYER, EntityType.ARMOR_STAND, EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM, EntityType.VILLAGER);
	private final Map<EntityType<?>, Map<ResourceLocation, LootTable.Builder>> map = Maps.newHashMap();

	protected static LootTable.Builder createSheepTable(ItemLike itemLike) {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(itemLike))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootTableReference.lootTableReference(EntityType.SHEEP.getDefaultLootTable())));
	}

	public abstract void generate();

	public abstract SimpleRegistry<EntityType<?>> getEntityTypeRegistry();

	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
		this.generate();
		HashSet set = Sets.newHashSet();
		this.getEntityTypeRegistry().getEntries().forEach(reference -> {
			EntityType entityType = reference.get();
			if (SimpleEntityLootSubProvider.canHaveLootTable(entityType)) {
				Map<ResourceLocation, LootTable.Builder> map = this.map.remove(entityType);
				ResourceLocation resourceLocation2 = entityType.getDefaultLootTable();
				if (!(resourceLocation2.equals(BuiltInLootTables.EMPTY) || map != null && map.containsKey(resourceLocation2))) {
					throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", resourceLocation2, reference.getId()));
				}
				if (map != null) {
					map.forEach((resourceLocation, builder) -> {
						if (!set.add(resourceLocation)) {
							throw new IllegalStateException(String.format(Locale.ROOT, "Duplicate loottable '%s' for '%s'", resourceLocation, reference.getId()));
						}
						biConsumer.accept((ResourceLocation) resourceLocation, (LootTable.Builder) builder);
					});
				}
			} else {
				Map<ResourceLocation, LootTable.Builder> map = this.map.remove(entityType);
				if (map != null) {
					throw new IllegalStateException(String.format(Locale.ROOT, "Weird loottables '%s' for '%s', not a LivingEntity so should not have loot", map.keySet().stream().map(ResourceLocation::toString).collect(Collectors.joining(",")), reference.getId()));
				}
			}
		});
		if (!this.map.isEmpty()) {
			throw new IllegalStateException("Created loot tables for entities not supported by datapack: " + this.map.keySet());
		}
	}

	private static boolean canHaveLootTable(EntityType<?> entityType) {
		return SPECIAL_LOOT_TABLE_TYPES.contains(entityType) || entityType.getCategory() != MobCategory.MISC;
	}

	protected LootItemCondition.Builder killedByFrog() {
		return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().source(EntityPredicate.Builder.entity().of(EntityType.FROG)));
	}

	protected LootItemCondition.Builder killedByFrogVariant(FrogVariant frogVariant) {
		return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().source(EntityPredicate.Builder.entity().of(EntityType.FROG).subPredicate(EntitySubPredicate.variant(frogVariant))));
	}

	protected void add(EntityType<?> entityType, LootTable.Builder builder) {
		this.add(entityType, entityType.getDefaultLootTable(), builder);
	}

	protected void add(EntityType<?> entityType2, ResourceLocation resourceLocation, LootTable.Builder builder) {
		this.map.computeIfAbsent(entityType2, entityType -> new HashMap()).put(resourceLocation, builder);
	}
}
