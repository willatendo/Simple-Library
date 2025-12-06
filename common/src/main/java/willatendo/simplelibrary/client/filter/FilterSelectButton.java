package willatendo.simplelibrary.client.filter;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class FilterSelectButton extends Button {
    public static final ResourceLocation SELECT_ALL = SimpleUtils.simple("textures/gui/select_all.png");
    public static final ResourceLocation DESELECT_ALL = SimpleUtils.simple("textures/gui/deselect_all.png");
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
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        int iconX = this.getX() + (this.width - 10) / 2;
        int iconY = this.getY() + (this.height - 10) / 2;
        int brightness = ARGB.scaleRGB(0xFFFFFFFF, this.active ? 1.0F : 0.5F);
        graphics.blit(RenderType::guiTextured, this.selectAll ? SELECT_ALL : DESELECT_ALL, iconX, iconY, 0, 0, 10, 10, 10, 10, brightness);
    }
}
