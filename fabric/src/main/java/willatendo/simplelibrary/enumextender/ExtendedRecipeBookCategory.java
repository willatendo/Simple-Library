package willatendo.simplelibrary.enumextender;

import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public record ExtendedRecipeBookCategory(String name, Supplier<ItemStack>... itemStacks) {
}
