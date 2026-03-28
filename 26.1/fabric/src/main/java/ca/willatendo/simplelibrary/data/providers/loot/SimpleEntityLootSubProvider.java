package ca.willatendo.simplelibrary.data.providers.loot;

import com.google.common.collect.Maps;
import net.minecraft.advancements.criterion.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SimpleEntityLootSubProvider implements LootTableSubProvider {
    private final Map<EntityType<?>, Map<ResourceKey<LootTable>, LootTable.Builder>> map = Maps.newHashMap();
    protected final HolderLookup.Provider registries;
    private final String modId;
    private final FeatureFlagSet allowed = FeatureFlags.REGISTRY.allFlags();
    private final FeatureFlagSet required = FeatureFlags.REGISTRY.allFlags();

    protected final AnyOfCondition.Builder shouldSmeltLoot() {
        HolderLookup.RegistryLookup<Enchantment> enchantmentsRegistry = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return AnyOfCondition.anyOf(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true))), LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().withComponents(DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(enchantmentsRegistry.getOrThrow(EnchantmentTags.SMELTS_LOOT), MinMaxBounds.Ints.ANY)))).build())))));
    }

    public SimpleEntityLootSubProvider(HolderLookup.Provider registries, String modId) {
        this.registries = registries;
        this.modId = modId;
    }

    public static LootPool.Builder createSheepDispatchPool(Map<DyeColor, ResourceKey<LootTable>> tableNames) {
        AlternativesEntry.Builder variants = AlternativesEntry.alternatives();

        for (Map.Entry<DyeColor, ResourceKey<LootTable>> entry : tableNames.entrySet()) {
            variants = variants.otherwise(NestedLootTable.lootTableReference(entry.getValue()).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.SHEEP_COLOR, entry.getKey())).build()).subPredicate(SheepPredicate.hasWool()))));
        }

        return LootPool.lootPool().add(variants);
    }

    public abstract void generate();


    protected LootItemCondition.Builder killedByFrog(HolderGetter<EntityType<?>> entityTypes) {
        return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().source(EntityPredicate.Builder.entity().of(entityTypes, EntityType.FROG)));
    }

    protected LootItemCondition.Builder killedByFrogVariant(HolderGetter<EntityType<?>> entityTypes, HolderGetter<FrogVariant> frogVariants, ResourceKey<FrogVariant> variant) {
        return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().source(EntityPredicate.Builder.entity().of(entityTypes, EntityType.FROG).components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.FROG_VARIANT, frogVariants.getOrThrow(variant))).build())));
    }

    protected void add(EntityType<?> type, LootTable.Builder builder) {
        this.add(type, type.getDefaultLootTable().orElseThrow(() -> new IllegalStateException("Entity " + type + " has no loot table")), builder);
    }

    protected void add(EntityType<?> type, ResourceKey<LootTable> lootTable, LootTable.Builder builder) {
        this.map.computeIfAbsent(type, entityType -> new HashMap<>()).put(lootTable, builder);
    }

    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return BuiltInRegistries.ENTITY_TYPE.stream().filter(entityType -> this.modId.equals(BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getNamespace()));
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        this.generate();
        Set<ResourceKey<LootTable>> seen = new HashSet<>();
        this.getKnownEntityTypes().map(EntityType::builtInRegistryHolder).forEach((holder) -> {
            EntityType<?> entityType = holder.value();
            if (entityType.isEnabled(this.allowed)) {
                Optional<ResourceKey<LootTable>> defaultLootTable = entityType.getDefaultLootTable();
                if (defaultLootTable.isPresent()) {
                    Map<ResourceKey<LootTable>, LootTable.Builder> builders = this.map.remove(entityType);
                    if (entityType.isEnabled(this.required) && (builders == null || !builders.containsKey(defaultLootTable.get()))) {
                        throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", defaultLootTable.get(), holder.key().identifier()));
                    }

                    if (builders != null) {
                        builders.forEach((id, builder) -> {
                            if (!seen.add(id)) {
                                throw new IllegalStateException(String.format(Locale.ROOT, "Duplicate loottable '%s' for '%s'", id, holder.key().identifier()));
                            } else {
                                output.accept(id, builder);
                            }
                        });
                    }
                } else {
                    Map<ResourceKey<LootTable>, LootTable.Builder> buildersx = this.map.remove(entityType);
                    if (buildersx != null) {
                        throw new IllegalStateException(String.format(Locale.ROOT, "Weird loottables '%s' for '%s', not a LivingEntity so should not have loot", buildersx.keySet().stream().map((r) -> r.identifier().toString()).collect(Collectors.joining(",")), holder.key().identifier()));
                    }
                }
            }

        });
        if (!this.map.isEmpty()) {
            throw new IllegalStateException("Created loot tables for entities not supported by datapack: " + String.valueOf(this.map.keySet()));
        }
    }
}
