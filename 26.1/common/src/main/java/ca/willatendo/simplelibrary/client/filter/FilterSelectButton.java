package ca.willatendo.simplelibrary.client.filter;

import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

public class FilterSelectButton extends Button {
    public static final Identifier SELECT_ALL = SimpleCoreUtils.resource("textures/gui/select_all.png");
    public static final Identifier DESELECT_ALL = SimpleCoreUtils.resource("textures/gui/deselect_all.png");
    private final boolean selectAll;

    public FilterSelectButton(CreativeModeTabFilter creativeModeTabFilter, CreativeModeInventoryScreen creativeModeInventoryScreen, int x, int y, boolean selectAll) {
        super(x, y, 20, 20, CommonComponents.EMPTY, button -> {
            if (selectAll) {
                creativeModeTabFilter.getCategories().forEach(filter -> filter.setEnabled(true));
            } else {
                creativeModeTabFilter.getCategories().forEach(filter -> filter.setEnabled(false));
            }
            creativeModeTabFilter.updateItems(creativeModeInventoryScreen);
        }, Button.DEFAULT_NARRATION);
        this.selectAll = selectAll;
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderDefaultSprite(guiGraphics);
        int iconX = this.getX() + (this.width - 10) / 2;
        int iconY = this.getY() + (this.height - 10) / 2;
        int brightness = ARGB.scaleRGB(0xFFFFFFFF, this.active ? 1.0F : 0.5F);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.selectAll ? SELECT_ALL : DESELECT_ALL, iconX, iconY, 0, 0, 10, 10, 10, 10, brightness);
    }
}
