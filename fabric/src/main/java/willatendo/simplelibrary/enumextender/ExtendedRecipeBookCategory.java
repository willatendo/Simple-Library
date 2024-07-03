package willatendo.simplelibrary.enumextender;

import net.minecraft.world.item.ItemStack;

public record ExtendedRecipeBookCategory(String name, ItemStack... itemStacks) {
}
