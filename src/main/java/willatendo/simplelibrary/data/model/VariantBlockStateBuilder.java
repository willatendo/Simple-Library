package willatendo.simplelibrary.data.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import willatendo.simplelibrary.data.SimpleBlockStateProvider.ConfiguredModelList;

public class VariantBlockStateBuilder implements GeneratedBlockState {
	private final Block owner;
	private final Map<PartialBlockstate, ConfiguredModelList> models = new LinkedHashMap<>();
	private final Set<BlockState> coveredStates = new HashSet<>();

	public VariantBlockStateBuilder(Block block) {
		this.owner = block;
	}

	public Map<PartialBlockstate, ConfiguredModelList> getModels() {
		return this.models;
	}

	public Block getOwner() {
		return this.owner;
	}

	@Override
	public JsonObject toJson() {
		List<BlockState> missingStates = Lists.newArrayList(this.owner.getStateDefinition().getPossibleStates());
		missingStates.removeAll(coveredStates);
		Preconditions.checkState(missingStates.isEmpty(), "Blockstate for block %s does not cover all states. Missing: %s", this.owner, missingStates);
		JsonObject variants = new JsonObject();
		this.getModels().entrySet().stream().sorted(Entry.comparingByKey(PartialBlockstate.comparingByProperties())).forEach(entry -> variants.add(entry.getKey().toString(), entry.getValue().toJSON()));
		JsonObject main = new JsonObject();
		main.add("variants", variants);
		return main;
	}

	public VariantBlockStateBuilder addModels(PartialBlockstate partialBlockstate, ConfiguredModel... configuredModels) {
		Preconditions.checkNotNull(partialBlockstate, "state must not be null");
		Preconditions.checkArgument(configuredModels.length > 0, "Cannot set models to empty array");
		Preconditions.checkArgument(partialBlockstate.getOwner() == this.owner, "Cannot set models for a different block. Found: %s, Current: %s", partialBlockstate.getOwner(), this.owner);
		if (!this.models.containsKey(partialBlockstate)) {
			Preconditions.checkArgument(disjointToAll(partialBlockstate), "Cannot set models for a state for which a partial match has already been configured");
			this.models.put(partialBlockstate, new ConfiguredModelList(configuredModels));
			for (BlockState fullState : owner.getStateDefinition().getPossibleStates()) {
				if (partialBlockstate.test(fullState)) {
					coveredStates.add(fullState);
				}
			}
		} else {
			this.models.compute(partialBlockstate, (partialBlockState, configuredModelList) -> configuredModelList.append(configuredModels));
		}
		return this;
	}

	public VariantBlockStateBuilder setModels(PartialBlockstate partialBlockstate, ConfiguredModel... configuredModels) {
		Preconditions.checkArgument(!this.models.containsKey(partialBlockstate), "Cannot set models for a state that has already been configured: %s", partialBlockstate);
		this.addModels(partialBlockstate, configuredModels);
		return this;
	}

	private boolean disjointToAll(PartialBlockstate partialBlockstate) {
		return this.coveredStates.stream().noneMatch(partialBlockstate);
	}

	public PartialBlockstate partialState() {
		return new PartialBlockstate(this.owner, this);
	}

	public VariantBlockStateBuilder forAllStates(Function<BlockState, ConfiguredModel[]> mapper) {
		return this.forAllStatesExcept(mapper);
	}

	public VariantBlockStateBuilder forAllStatesExcept(Function<BlockState, ConfiguredModel[]> mapper, Property<?>... exceptions) {
		Set<PartialBlockstate> partialBlockstates = new HashSet<>();
		for (BlockState fullState : this.owner.getStateDefinition().getPossibleStates()) {
			Map<Property<?>, Comparable<?>> propertyValues = Maps.newLinkedHashMap(fullState.getValues());
			for (Property<?> p : exceptions) {
				propertyValues.remove(p);
			}
			PartialBlockstate partialBlockstate = new PartialBlockstate(owner, propertyValues, this);
			if (partialBlockstates.add(partialBlockstate)) {
				this.setModels(partialBlockstate, mapper.apply(fullState));
			}
		}
		return this;
	}

	public static class PartialBlockstate implements Predicate<BlockState> {
		private final Block block;
		private final SortedMap<Property<?>, Comparable<?>> states;
		@Nullable
		private final VariantBlockStateBuilder outerBuilder;

		public PartialBlockstate(Block block, @Nullable VariantBlockStateBuilder variantBlockStateBuilder) {
			this(block, ImmutableMap.of(), variantBlockStateBuilder);
		}

		public PartialBlockstate(Block block, Map<Property<?>, Comparable<?>> setStates, @Nullable VariantBlockStateBuilder variantBlockStateBuilder) {
			this.block = block;
			this.outerBuilder = variantBlockStateBuilder;
			for (Map.Entry<Property<?>, Comparable<?>> entry : setStates.entrySet()) {
				Property<?> prop = entry.getKey();
				Comparable<?> value = entry.getValue();
				Preconditions.checkArgument(block.getStateDefinition().getProperties().contains(prop), "Property %s not found on block %s", entry, this.block);
				Preconditions.checkArgument(prop.getPossibleValues().contains(value), "%s is not a valid value for %s", value, prop);
			}
			this.states = Maps.newTreeMap(Comparator.comparing(Property::getName));
			this.states.putAll(setStates);
		}

		public <T extends Comparable<T>> PartialBlockstate with(Property<T> property, T value) {
			Preconditions.checkArgument(!states.containsKey(property), "Property %s has already been set", property);
			Map<Property<?>, Comparable<?>> newState = new HashMap<>(this.states);
			newState.put(property, value);
			return new PartialBlockstate(this.block, newState, this.outerBuilder);
		}

		private void checkValidOwner() {
			Preconditions.checkNotNull(this.outerBuilder, "Partial blockstate must have a valid owner to perform this action");
		}

		public ConfiguredModel.Builder<VariantBlockStateBuilder> modelForState() {
			this.checkValidOwner();
			return ConfiguredModel.builder(this.outerBuilder, this);
		}

		public PartialBlockstate addModels(ConfiguredModel... models) {
			this.checkValidOwner();
			this.outerBuilder.addModels(this, models);
			return this;
		}

		public VariantBlockStateBuilder setModels(ConfiguredModel... models) {
			this.checkValidOwner();
			return this.outerBuilder.setModels(this, models);
		}

		public PartialBlockstate partialState() {
			this.checkValidOwner();
			return this.outerBuilder.partialState();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			PartialBlockstate partialBlockstate = (PartialBlockstate) o;
			return this.block.equals(partialBlockstate.block) && this.states.equals(partialBlockstate.states);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.block, this.states);
		}

		public Block getOwner() {
			return this.block;
		}

		public SortedMap<Property<?>, Comparable<?>> getSetStates() {
			return this.states;
		}

		@Override
		public boolean test(BlockState blockState) {
			if (blockState.getBlock() != getOwner()) {
				return false;
			}
			for (Map.Entry<Property<?>, Comparable<?>> entry : this.states.entrySet()) {
				if (blockState.getValue(entry.getKey()) != entry.getValue()) {
					return false;
				}
			}
			return true;
		}

		@Override
		public String toString() {
			StringBuilder ret = new StringBuilder();
			for (Map.Entry<Property<?>, Comparable<?>> entry : this.states.entrySet()) {
				if (ret.length() > 0) {
					ret.append(',');
				}
				ret.append(entry.getKey().getName()).append('=').append(((Property) entry.getKey()).getName(entry.getValue()));
			}
			return ret.toString();
		}

		public static Comparator<PartialBlockstate> comparingByProperties() {
			return (partialBlockState, s2) -> {
				SortedSet<Property<?>> propUniverse = new TreeSet<>(partialBlockState.getSetStates().comparator().reversed());
				propUniverse.addAll(partialBlockState.getSetStates().keySet());
				propUniverse.addAll(s2.getSetStates().keySet());
				for (Property<?> prop : propUniverse) {
					Comparable val1 = partialBlockState.getSetStates().get(prop);
					Comparable val2 = s2.getSetStates().get(prop);
					if (val1 == null && val2 != null) {
						return -1;
					} else if (val2 == null && val1 != null) {
						return 1;
					} else if (val1 != null && val2 != null) {
						int cmp = val1.compareTo(val2);
						if (cmp != 0) {
							return cmp;
						}
					}
				}
				return 0;
			};
		}
	}
}
