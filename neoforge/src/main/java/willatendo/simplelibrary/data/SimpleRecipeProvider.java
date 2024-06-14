package willatendo.simplelibrary.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class SimpleRecipeProvider extends RecipeProvider {
    //Broad
    private final Map<String, RecipeBuilder> recipeBuilders = new HashMap<>();

    //Special Cases
    private final Map<String, SmithingTransformRecipeBuilder> smithingTransformRecipeBuilderMap = new HashMap<>();
    private final Map<String, SpecialRecipeBuilder> specialRecipeBuilderMap = new HashMap<>();

    private final String modId;

    public SimpleRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String modId) {
        super(packOutput, registries);
        this.modId = modId;
    }

    public abstract void addRecipes();

    //Broad
    public void add(String name, RecipeBuilder recipeBuilder) {
        this.recipeBuilders.put(name, recipeBuilder);
    }

    public void shaped(String name, RecipeCategory recipeCategory, String group, ItemLike output, int outputCount, PatternBuilder patternBuilder, IngredientBuilder... ingredientBuilders) {
        ShapedRecipeBuilder shapedRecipeBuilder;
        if (outputCount > 1) {
            shapedRecipeBuilder = ShapedRecipeBuilder.shaped(recipeCategory, output, outputCount);
        } else {
            shapedRecipeBuilder = ShapedRecipeBuilder.shaped(recipeCategory, output);
        }
        if (group != null) {
            shapedRecipeBuilder.group(group);
        }
        String[] patterns = patternBuilder.getPattern();
        for (String pattern : patterns) {
            shapedRecipeBuilder.pattern(pattern);
        }
        for (IngredientBuilder ingredientBuilder : ingredientBuilders) {
            Ingredient ingredient = ingredientBuilder.getIngredient();
            char symbol = ingredientBuilder.getSymbol();
            shapedRecipeBuilder.define(symbol, ingredient);

            if (ingredientBuilder.isMain() || ingredientBuilders.length < 2) {
                ingredientBuilder.createRequires(shapedRecipeBuilder);
            }
        }
        this.recipeBuilders.put(name != null ? name : this.toName(output), shapedRecipeBuilder);
    }

    public void shaped(RecipeCategory recipeCategory, String group, ItemLike output, int outputCount, PatternBuilder patternBuilder, IngredientBuilder... ingredientBuilders) {
        this.shaped(null, recipeCategory, group, output, outputCount, patternBuilder, ingredientBuilders);
    }

    public void shaped(String name, RecipeCategory recipeCategory, String group, ItemLike output, PatternBuilder patternBuilder, IngredientBuilder... ingredientBuilders) {
        this.shaped(name, recipeCategory, group, output, 1, patternBuilder, ingredientBuilders);
    }

    public void shaped(RecipeCategory recipeCategory, String group, ItemLike output, PatternBuilder patternBuilder, IngredientBuilder... ingredientBuilders) {
        this.shaped(null, recipeCategory, group, output, 1, patternBuilder, ingredientBuilders);
    }

    public void shaped(String name, RecipeCategory recipeCategory, ItemLike output, int outputCount, PatternBuilder patternBuilder, IngredientBuilder... ingredientBuilders) {
        this.shaped(name, recipeCategory, null, output, outputCount, patternBuilder, ingredientBuilders);
    }

    public void shaped(RecipeCategory recipeCategory, ItemLike output, int outputCount, PatternBuilder patternBuilder, IngredientBuilder... ingredientBuilders) {
        this.shaped(null, recipeCategory, null, output, outputCount, patternBuilder, ingredientBuilders);
    }

    public void shaped(String name, RecipeCategory recipeCategory, ItemLike output, PatternBuilder patternBuilder, IngredientBuilder... ingredientBuilders) {
        this.shaped(name, recipeCategory, null, output, 1, patternBuilder, ingredientBuilders);
    }

    public void shaped(RecipeCategory recipeCategory, ItemLike output, PatternBuilder patternBuilder, IngredientBuilder... ingredientBuilders) {
        this.shaped(null, recipeCategory, null, output, 1, patternBuilder, ingredientBuilders);
    }

    public void shapeless(String name, RecipeCategory recipeCategory, String group, ItemLike output, int outputCount, IngredientBuilder... ingredientBuilders) {
        ShapelessRecipeBuilder shapelessRecipeBuilder;
        if (outputCount > 1) {
            shapelessRecipeBuilder = ShapelessRecipeBuilder.shapeless(recipeCategory, output, outputCount);
        } else {
            shapelessRecipeBuilder = ShapelessRecipeBuilder.shapeless(recipeCategory, output);
        }
        if (group != null) {
            shapelessRecipeBuilder.group(group);
        }
        for (IngredientBuilder ingredientBuilder : ingredientBuilders) {
            Ingredient ingredient = ingredientBuilder.getIngredient();
            int count = ingredientBuilder.getCount();
            shapelessRecipeBuilder.requires(ingredient, count);

            if (ingredientBuilder.isMain() || ingredientBuilders.length < 2) {
                ingredientBuilder.createRequires(shapelessRecipeBuilder);
            }
        }
        this.recipeBuilders.put(name != null ? name : this.toName(output), shapelessRecipeBuilder);
    }

    public void shapeless(RecipeCategory recipeCategory, String group, ItemLike output, int outputCount, IngredientBuilder... ingredientBuilders) {
        this.shapeless(null, recipeCategory, group, output, outputCount, ingredientBuilders);
    }

    public void shapeless(String name, RecipeCategory recipeCategory, String group, ItemLike output, IngredientBuilder... ingredientBuilders) {
        this.shapeless(name, recipeCategory, group, output, 1, ingredientBuilders);
    }

    public void shapeless(RecipeCategory recipeCategory, String group, ItemLike output, IngredientBuilder... ingredientBuilders) {
        this.shapeless(recipeCategory, group, output, 1, ingredientBuilders);
    }

    public void shapeless(String name, RecipeCategory recipeCategory, ItemLike output, IngredientBuilder... ingredientBuilders) {
        this.shapeless(name, recipeCategory, null, output, 1, ingredientBuilders);
    }

    public void shapeless(RecipeCategory recipeCategory, ItemLike output, IngredientBuilder... ingredientBuilders) {
        this.shapeless(recipeCategory, null, output, 1, ingredientBuilders);
    }

    public void shapeless(String name, RecipeCategory recipeCategory, ItemLike output, int count, IngredientBuilder... ingredientBuilders) {
        this.shapeless(name, recipeCategory, null, output, count, ingredientBuilders);
    }

    public void shapeless(RecipeCategory recipeCategory, ItemLike output, int count, IngredientBuilder... ingredientBuilders) {
        this.shapeless(recipeCategory, null, output, count, ingredientBuilders);
    }

    public void smelting(String group, ItemLike output, ItemLike input, float experience, int time) {
        SimpleCookingRecipeBuilder smeltingRecipeBuilder = SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD, output, experience, time).group(group).unlockedBy(getHasName(input), has(input));
        if (group != null) {
            smeltingRecipeBuilder.group(group);
        }
        String baseName = this.toName(output);
        this.recipeBuilders.put(baseName + "_from_smelting", smeltingRecipeBuilder);
    }

    public void smelting(String group, ItemLike output, TagKey<Item> input, String requires, float experience, int time) {
        SimpleCookingRecipeBuilder smeltingRecipeBuilder = SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD, output, experience, time).unlockedBy(requires, has(input));
        if (group != null) {
            smeltingRecipeBuilder.group(group);
        }
        String baseName = this.toName(output);
        this.recipeBuilders.put(baseName + "_from_smelting", smeltingRecipeBuilder);
    }

    public void smelting(ItemLike output, ItemLike input, float experience, int time) {
        this.ore(null, output, input, experience, time);
    }

    public void smelting(ItemLike output, TagKey<Item> input, String requires, float experience, int time) {
        this.ore(null, output, input, requires, experience, time);
    }

    public void food(String group, ItemLike output, ItemLike input, float experience, int time) {
        SimpleCookingRecipeBuilder smeltingRecipeBuilder = SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD, output, experience, time).unlockedBy(getHasName(input), has(input));
        SimpleCookingRecipeBuilder smokingRecipeBuilder = SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), RecipeCategory.FOOD, output, experience, time).unlockedBy(getHasName(input), has(input));
        SimpleCookingRecipeBuilder campfireCookingRecipeBuilder = SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(input), RecipeCategory.FOOD, output, experience, time).unlockedBy(getHasName(input), has(input));
        if (group != null) {
            smeltingRecipeBuilder.group(group);
            smokingRecipeBuilder.group(group);
            campfireCookingRecipeBuilder.group(group);
        }
        String baseName = this.toName(output);
        this.recipeBuilders.put(baseName + "_from_smelting", smeltingRecipeBuilder);
        this.recipeBuilders.put(baseName + "_from_smoking", smokingRecipeBuilder);
        this.recipeBuilders.put(baseName + "_from_campfire_cooking", campfireCookingRecipeBuilder);
    }

    public void food(String group, ItemLike output, TagKey<Item> input, String requires, float experience, int time) {
        SimpleCookingRecipeBuilder smeltingRecipeBuilder = SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD, output, experience, time).unlockedBy(requires, has(input));
        SimpleCookingRecipeBuilder smokingRecipeBuilder = SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), RecipeCategory.FOOD, output, experience, time).unlockedBy(requires, has(input));
        SimpleCookingRecipeBuilder campfireCookingRecipeBuilder = SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(input), RecipeCategory.FOOD, output, experience, time).unlockedBy(requires, has(input));
        if (group != null) {
            smeltingRecipeBuilder.group(group);
            smokingRecipeBuilder.group(group);
            campfireCookingRecipeBuilder.group(group);
        }
        String baseName = this.toName(output);
        this.recipeBuilders.put(baseName + "_from_smelting", smeltingRecipeBuilder);
        this.recipeBuilders.put(baseName + "_from_smoking", smokingRecipeBuilder);
        this.recipeBuilders.put(baseName + "_from_campfire_cooking", campfireCookingRecipeBuilder);
    }

    public void food(ItemLike output, ItemLike input, float experience, int time) {
        this.food(null, output, input, experience, time);
    }

    public void food(ItemLike output, TagKey<Item> input, String requires, float experience, int time) {
        this.food(null, output, input, requires, experience, time);
    }

    public void ore(String group, ItemLike output, ItemLike input, float experience, int time) {
        SimpleCookingRecipeBuilder smeltingRecipeBuilder = SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD, output, experience, time).group(group).unlockedBy(getHasName(input), has(input));
        SimpleCookingRecipeBuilder blastingRecipeBuilder = SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), RecipeCategory.FOOD, output, experience, time).group(group).unlockedBy(getHasName(input), has(input));
        if (group != null) {
            smeltingRecipeBuilder.group(group);
            blastingRecipeBuilder.group(group);
        }
        String baseName = this.toName(output);
        this.recipeBuilders.put(baseName + "_from_smelting", smeltingRecipeBuilder);
        this.recipeBuilders.put(baseName + "_from_blasting", blastingRecipeBuilder);
    }

    public void ore(String group, ItemLike output, TagKey<Item> input, String requires, float experience, int time) {
        SimpleCookingRecipeBuilder smeltingRecipeBuilder = SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD, output, experience, time).unlockedBy(requires, has(input));
        SimpleCookingRecipeBuilder blastingRecipeBuilder = SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), RecipeCategory.FOOD, output, experience, time).unlockedBy(requires, has(input));
        if (group != null) {
            smeltingRecipeBuilder.group(group);
            blastingRecipeBuilder.group(group);
        }
        String baseName = this.toName(output);
        this.recipeBuilders.put(baseName + "_from_smelting", smeltingRecipeBuilder);
        this.recipeBuilders.put(baseName + "_from_blasting", blastingRecipeBuilder);
    }

    public void ore(ItemLike output, ItemLike input, float experience, int time) {
        this.ore(null, output, input, experience, time);
    }

    public void ore(ItemLike output, TagKey<Item> input, String requires, float experience, int time) {
        this.ore(null, output, input, requires, experience, time);
    }

    public void smithing(String name, ItemLike template, ItemLike base, ItemLike addition, ItemLike result, RecipeCategory recipeCategory) {
        SmithingTransformRecipeBuilder smithingTransformRecipeBuilder = SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(base), Ingredient.of(addition), recipeCategory, result.asItem()).unlocks(getHasName(base), has(base));
        this.smithingTransformRecipeBuilderMap.put(name != null ? name : this.toName(result), smithingTransformRecipeBuilder);
    }

    public void smithing(ItemLike template, ItemLike base, ItemLike addition, ItemLike result, RecipeCategory recipeCategory) {
        this.smithing(null, template, base, addition, result, recipeCategory);
    }

    public void special(String name, Function<CraftingBookCategory, Recipe<?>> recipeType) {
        SpecialRecipeBuilder specialRecipeBuilder = SpecialRecipeBuilder.special(recipeType);
        this.specialRecipeBuilderMap.put(name, specialRecipeBuilder);
    }

    // Specific
    public void copySmithingTemplate(ItemLike smithingTemplate, ItemLike copyBlock) {
        this.shaped(RecipeCategory.MISC, smithingTemplate, 2, PatternBuilder.builder("#S#", "#C#", "###"), IngredientBuilder.build(Items.DIAMOND).symbol('#'), IngredientBuilder.build(copyBlock).symbol('C'), IngredientBuilder.build(smithingTemplate).symbol('S').requires());
    }

    private ResourceLocation toResourceLocation(String path) {
        return new ResourceLocation(this.modId, path);
    }

    private String toName(ItemLike itemLike) {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath();
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        this.addRecipes();

        this.recipeBuilders.forEach((name, recipeBuilder) -> {
            recipeBuilder.save(recipeOutput, this.toResourceLocation(name));
        });
        this.smithingTransformRecipeBuilderMap.forEach((name, smithingTransformRecipeBuilder) -> {
            smithingTransformRecipeBuilder.save(recipeOutput, this.toResourceLocation(name));
        });
        this.specialRecipeBuilderMap.forEach((name, specialRecipeBuilder) -> {
            specialRecipeBuilder.save(recipeOutput, this.toResourceLocation(name));
        });
    }

    public static final class PatternBuilder {
        private final String[] pattern;

        private PatternBuilder(String[] pattern) {
            this.pattern = pattern;
        }

        public String[] getPattern() {
            return this.pattern;
        }

        public static PatternBuilder builder(String... pattern) {
            return new PatternBuilder(pattern);
        }
    }

    public static final class IngredientBuilder {
        private final Ingredient ingredient;
        private ItemLike itemLikeRequirement;
        private TagKey<Item> itemTagKeyRequirement;
        private String requires;
        private final int count;
        private boolean main = false;
        private char symbol;

        private IngredientBuilder(Ingredient ingredient, int count) {
            this.ingredient = ingredient;
            this.count = count;
        }

        private IngredientBuilder main(ItemLike itemLikeRequirement, TagKey<Item> itemTagKeyRequirement, String requires) {
            this.main = true;
            this.itemLikeRequirement = itemLikeRequirement;
            this.itemTagKeyRequirement = itemTagKeyRequirement;
            this.requires = requires;
            return this;
        }

        public IngredientBuilder requires(ItemLike itemLikeRequirement) {
            return this.main(itemLikeRequirement, null, null);
        }

        public IngredientBuilder requires(TagKey<Item> itemLikeRequirement, String requires) {
            return this.main(null, itemTagKeyRequirement, requires);
        }

        public IngredientBuilder requires() {
            return this.main(null, null, null);
        }

        public IngredientBuilder symbol(char symbol) {
            this.symbol = symbol;
            return this;
        }

        public Ingredient getIngredient() {
            return this.ingredient;
        }

        public void createRequires(RecipeBuilder recipeBuilder) {
            if (this.itemLikeRequirement == null) {
                this.itemLikeRequirement = this.ingredient.getItems()[0].getItem();
            }
            if (this.itemTagKeyRequirement != null && this.requires != null) {
                recipeBuilder.unlockedBy(this.requires, RecipeProvider.has(this.itemTagKeyRequirement));
            } else {
                recipeBuilder.unlockedBy(RecipeProvider.getHasName(this.itemLikeRequirement), RecipeProvider.has(this.itemLikeRequirement));
            }
        }

        public int getCount() {
            return this.count;
        }

        public boolean isMain() {
            return this.main;
        }

        public char getSymbol() {
            return this.symbol;
        }

        public static IngredientBuilder build(Ingredient ingredient, int count) {
            return new IngredientBuilder(ingredient, count);
        }

        public static IngredientBuilder build(ItemLike itemLike, int count) {
            return IngredientBuilder.build(Ingredient.of(new ItemLike[]{itemLike}), count);
        }

        public static IngredientBuilder build(TagKey<Item> itemTagKey, int count) {
            return IngredientBuilder.build(Ingredient.of(itemTagKey), count);
        }

        public static IngredientBuilder build(Ingredient ingredient) {
            return IngredientBuilder.build(ingredient, 1);
        }

        public static IngredientBuilder build(ItemLike itemLike) {
            return IngredientBuilder.build(Ingredient.of(new ItemLike[]{itemLike}), 1);
        }

        public static IngredientBuilder build(TagKey<Item> itemTagKey) {
            return IngredientBuilder.build(Ingredient.of(itemTagKey), 1);
        }
    }
}
