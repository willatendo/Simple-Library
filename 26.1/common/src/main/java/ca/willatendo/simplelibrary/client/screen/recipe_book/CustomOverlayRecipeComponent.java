package ca.willatendo.simplelibrary.client.screen.recipe_book;

import ca.willatendo.simplelibrary.client.CustomRecipeBooks;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.SlotSelectTime;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public final class CustomOverlayRecipeComponent extends OverlayRecipeComponent {
    private RecipeBookComponent<?> parent;

    public CustomOverlayRecipeComponent(SlotSelectTime slotSelectTime, RecipeBookComponent<?> parent) {
        super(slotSelectTime, false);
        this.parent = parent;
    }

    @Override
    public void init(RecipeCollection recipeCollection, ContextMap contextMap, boolean isFiltering, int x, int y, int overlayX, int overlayY, float width) {
        this.collection = recipeCollection;
        List<RecipeDisplayEntry> craftable = recipeCollection.getSelectedRecipes(RecipeCollection.CraftableStatus.CRAFTABLE);
        List<RecipeDisplayEntry> notCraftable = isFiltering ? Collections.emptyList() : recipeCollection.getSelectedRecipes(RecipeCollection.CraftableStatus.NOT_CRAFTABLE);
        int craftableSize = craftable.size();
        int fullSize = craftableSize + notCraftable.size();
        int k = fullSize <= 16 ? 4 : 5;
        int l = (int) Math.ceil((float) fullSize / (float) k);
        this.x = x;
        this.y = y;
        float f = (float) (this.x + Math.min(fullSize, k) * 25);
        float f1 = (float) (overlayX + 50);
        if (f > f1) {
            this.x = (int) ((float) this.x - width * (float) ((int) ((f - f1) / width)));
        }

        float f2 = (float) (this.y + l * 25);
        float f3 = (float) (overlayY + 50);
        if (f2 > f3) {
            this.y = (int) ((float) this.y - width * (float) Mth.ceil((f2 - f3) / width));
        }

        float f4 = (float) this.y;
        float f5 = (float) (overlayY - 100);
        if (f4 < f5) {
            this.y = (int) ((float) this.y - width * (float) Mth.ceil((f4 - f5) / width));
        }

        this.isVisible = true;
        this.recipeButtons.clear();

        for (int i = 0; i < fullSize; ++i) {
            boolean flag = i < craftableSize;
            RecipeDisplayEntry recipedisplayentry = flag ? craftable.get(i) : notCraftable.get(i - craftableSize);
            int buttonX = this.x + 4 + 25 * (i % k);
            int buttonY = this.y + 5 + 25 * (i / k);
            CustomRecipeBooks.getButton((Class<? extends RecipeBookComponent<?>>) this.parent.getClass(), buttonX, buttonY, recipedisplayentry.id(), recipedisplayentry.display(), contextMap, flag, this.recipeButtons, CustomOverlayRecipeComponent.CustomOverlayRecipeButton::new);
        }

        this.lastRecipeClicked = null;
    }

    public static OverlayRecipeComponent.OverlayRecipeButton.Pos createGridPos(int x, int y, List<ItemStack> possibleItems) {
        return new OverlayRecipeComponent.OverlayRecipeButton.Pos(3 + x * 7, 3 + y * 7, possibleItems);
    }

    public class CustomOverlayRecipeButton extends OverlayRecipeComponent.OverlayRecipeButton {
        private final BiFunction<Boolean, Boolean, Identifier> sprite;

        public CustomOverlayRecipeButton(int x, int y, RecipeDisplayId recipeDisplayId, RecipeDisplay recipeDisplay, ContextMap contextMap, boolean isCraftable, BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>> slots, BiFunction<Boolean, Boolean, Identifier> sprite) {
            super(x, y, recipeDisplayId, isCraftable, slots.apply(recipeDisplay, contextMap));
            this.sprite = sprite;
        }

        @Override
        protected Identifier getSprite(boolean enabled) {
            return this.sprite.apply(enabled, this.isHoveredOrFocused());
        }
    }
}
