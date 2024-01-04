package willatendo.simplelibrary.data.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import willatendo.simplelibrary.data.SimpleBlockStateProvider;

public class MultiPartBlockStateBuilder implements GeneratedBlockState {
	private final List<PartBuilder> partBuilders = new ArrayList<>();
	private final Block block;

	public MultiPartBlockStateBuilder(Block block) {
		this.block = block;
	}

	public ConfiguredModel.Builder<PartBuilder> part() {
		return ConfiguredModel.builder(this);
	}

	public MultiPartBlockStateBuilder addPart(PartBuilder part) {
		this.partBuilders.add(part);
		return this;
	}

	@Override
	public JsonObject toJson() {
		JsonArray variants = new JsonArray();
		for (PartBuilder part : partBuilders) {
			variants.add(part.toJson());
		}
		JsonObject main = new JsonObject();
		main.add("multipart", variants);
		return main;
	}

	public class PartBuilder {
		public SimpleBlockStateProvider.ConfiguredModelList configuredModelList;
		public boolean useOr;
		public final Multimap<Property<?>, Comparable<?>> conditions = MultimapBuilder.linkedHashKeys().arrayListValues().build();
		public final List<ConditionGroup> nestedConditionGroups = new ArrayList<>();

		public PartBuilder(SimpleBlockStateProvider.ConfiguredModelList configuredModelList) {
			this.configuredModelList = configuredModelList;
		}

		public PartBuilder useOr() {
			this.useOr = true;
			return this;
		}

		@SafeVarargs
		public final <T extends Comparable<T>> PartBuilder condition(Property<T> property, T... values) {
			Preconditions.checkNotNull(property, "Property must not be null");
			Preconditions.checkNotNull(values, "Value list must not be null");
			Preconditions.checkArgument(values.length > 0, "Value list must not be empty");
			Preconditions.checkArgument(!conditions.containsKey(property), "Cannot set condition for property \"%s\" more than once", property.getName());
			Preconditions.checkArgument(canApplyTo(block), "IProperty %s is not valid for the block %s", property, block);
			Preconditions.checkState(nestedConditionGroups.isEmpty(), "Can't have normal conditions if there are already nested condition groups");
			this.conditions.putAll(property, Arrays.asList(values));
			return this;
		}

		public final ConditionGroup nestedGroup() {
			Preconditions.checkState(conditions.isEmpty(), "Can't have nested condition groups if there are already normal conditions");
			ConditionGroup group = new ConditionGroup();
			this.nestedConditionGroups.add(group);
			return group;
		}

		public MultiPartBlockStateBuilder end() {
			return MultiPartBlockStateBuilder.this;
		}

		private JsonObject toJson() {
			JsonObject jsonObject = new JsonObject();
			if (!this.conditions.isEmpty()) {
				jsonObject.add("when", MultiPartBlockStateBuilder.toJson(this.conditions, this.useOr));
			} else if (!this.nestedConditionGroups.isEmpty()) {
				jsonObject.add("when", MultiPartBlockStateBuilder.toJson(this.nestedConditionGroups, this.useOr));
			}
			jsonObject.add("apply", this.configuredModelList.toJSON());
			return jsonObject;
		}

		public boolean canApplyTo(Block block) {
			return block.getStateDefinition().getProperties().containsAll(conditions.keySet());
		}

		public class ConditionGroup {
			public final Multimap<Property<?>, Comparable<?>> conditions = MultimapBuilder.linkedHashKeys().arrayListValues().build();
			public final List<ConditionGroup> nestedConditionGroups = new ArrayList<>();
			private ConditionGroup parent = null;
			public boolean useOr;

			@SafeVarargs
			public final <T extends Comparable<T>> ConditionGroup condition(Property<T> property, T... values) {
				Preconditions.checkNotNull(property, "Property must not be null");
				Preconditions.checkNotNull(values, "Value list must not be null");
				Preconditions.checkArgument(values.length > 0, "Value list must not be empty");
				Preconditions.checkArgument(!this.conditions.containsKey(property), "Cannot set condition for property \"%s\" more than once", property.getName());
				Preconditions.checkArgument(canApplyTo(block), "IProperty %s is not valid for the block %s", property, block);
				Preconditions.checkState(this.nestedConditionGroups.isEmpty(), "Can't have normal conditions if there are already nested condition groups");
				this.conditions.putAll(property, Arrays.asList(values));
				return this;
			}

			public ConditionGroup nestedGroup() {
				Preconditions.checkState(conditions.isEmpty(), "Can't have nested condition groups if there are already normal conditions");
				ConditionGroup conditionGroup = new ConditionGroup();
				conditionGroup.parent = this;
				this.nestedConditionGroups.add(conditionGroup);
				return conditionGroup;
			}

			public ConditionGroup endNestedGroup() {
				if (this.parent == null) {
					throw new IllegalStateException("This condition group is not nested, use end() instead");
				}
				return this.parent;
			}

			public MultiPartBlockStateBuilder.PartBuilder end() {
				if (this.parent != null) {
					throw new IllegalStateException("This is a nested condition group, use endNestedGroup() instead");
				}
				return MultiPartBlockStateBuilder.PartBuilder.this;
			}

			public ConditionGroup useOr() {
				this.useOr = true;
				return this;
			}

			JsonObject toJson() {
				if (!this.conditions.isEmpty()) {
					return MultiPartBlockStateBuilder.toJson(this.conditions, this.useOr);
				} else if (!this.nestedConditionGroups.isEmpty()) {
					return MultiPartBlockStateBuilder.toJson(this.nestedConditionGroups, this.useOr);
				}
				return new JsonObject();
			}
		}
	}

	private static JsonObject toJson(List<PartBuilder.ConditionGroup> conditionGroup, boolean useOr) {
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		jsonObject.add(useOr ? "OR" : "AND", jsonArray);
		for (PartBuilder.ConditionGroup conditionGroups : conditionGroup) {
			jsonArray.add(conditionGroups.toJson());
		}
		return jsonObject;
	}

	private static JsonObject toJson(Multimap<Property<?>, Comparable<?>> conditions, boolean useOr) {
		JsonObject jsonObject = new JsonObject();
		for (Entry<Property<?>, Collection<Comparable<?>>> e : conditions.asMap().entrySet()) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Comparable<?> val : e.getValue()) {
				if (stringBuilder.length() > 0)
					stringBuilder.append("|");
				stringBuilder.append(((Property) e.getKey()).getName(val));
			}
			jsonObject.addProperty(e.getKey().getName(), stringBuilder.toString());
		}
		if (useOr) {
			JsonArray jsonArray = new JsonArray();
			for (Entry<String, JsonElement> jsonElement : jsonObject.entrySet()) {
				JsonObject obj = new JsonObject();
				obj.add(jsonElement.getKey(), jsonElement.getValue());
				jsonArray.add(obj);
			}
			jsonObject = new JsonObject();
			jsonObject.add("OR", jsonArray);
		}
		return jsonObject;
	}
}
