package willatendo.simplelibrary.client.filter;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class FilterButton extends Button {
    private static final ResourceLocation SELECTED_FILTER_TAB = SimpleUtils.simple("filter_tab_selected");
    private static final ResourceLocation UNSELECTED_FILTER_TAB = SimpleUtils.simple("filter_tab_unselected");

    private final Filter filter;
    private final Component filterTooltip;

    FilterButton(int x, int y, Filter filter, Button.OnPress onPress) {
        super(x, y, 32, 26, CommonComponents.EMPTY, onPress, Button.DEFAULT_NARRATION);
        this.filter = filter;
        this.filter.setFilterButton(this);
        ResourceLocation tagId = filter.getFilterTag().location();
        this.filterTooltip = SimpleUtils.translation(tagId.getNamespace(), "itemGroup.filter", tagId.getPath().replace("/", "."));
    }

    public Component getFilterTooltip() {
        return this.filterTooltip;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.blitSprite(RenderType::guiTextured, this.filter.isEnabled() ? SELECTED_FILTER_TAB : UNSELECTED_FILTER_TAB, this.getX(), this.getY(), 32, 26);
        guiGraphics.renderItem(this.filter.getFilterIcon(), this.getX() + 8, this.getY() + 5);
    }
}
