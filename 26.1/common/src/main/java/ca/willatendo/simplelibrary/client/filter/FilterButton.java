package ca.willatendo.simplelibrary.client.filter;

import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class FilterButton extends Button {
    private static final Identifier SELECTED_FILTER_TAB = SimpleCoreUtils.resource("container/creative_inventory/filter_tab_selected");
    private static final Identifier UNSELECTED_FILTER_TAB = SimpleCoreUtils.resource("container/creative_inventory/filter_tab_unselected");

    private final Filter filter;
    private final Component filterTooltip;

    FilterButton(int x, int y, Filter filter, Button.OnPress onPress) {
        super(x, y, 32, 26, CommonComponents.EMPTY, onPress, Button.DEFAULT_NARRATION);
        this.filter = filter;
        this.filter.setFilterButton(this);
        Identifier tagIdentifier = filter.getFilterTag().location();
        this.filterTooltip = CoreUtils.translation("itemGroup.filter", tagIdentifier.getNamespace(), tagIdentifier.getPath().replace("/", "."));
    }

    public Component getFilterTooltip() {
        return this.filterTooltip;
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.filter.isEnabled() ? SELECTED_FILTER_TAB : UNSELECTED_FILTER_TAB, this.getX(), this.getY(), 32, 26);
        guiGraphics.renderItem(this.filter.getFilterIcon(), this.getX() + 8, this.getY() + 5);
    }
}
