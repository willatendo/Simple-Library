package willatendo.simplelibrary.client.filter;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class ArrowButton extends Button {
    public static final ResourceLocation ARROW_UP = SimpleUtils.simple("textures/gui/arrow_up.png");
    public static final ResourceLocation ARROW_DOWN = SimpleUtils.simple("textures/gui/arrow_down.png");

    private final boolean up;

    public ArrowButton(int x, int y, boolean up, OnPress onPress) {
        super(x, y, 20, 20, CommonComponents.EMPTY, onPress, Button.DEFAULT_NARRATION);
        this.up = up;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        int iconX = this.getX() + (this.width - 10) / 2;
        int iconY = this.getY() + (this.height - 10) / 2;
        int brightness = ARGB.scaleRGB(0xFFFFFFFF, this.active ? 1.0F : 0.5F);
        graphics.blit(RenderType::guiTextured, this.up ? ARROW_UP : ARROW_DOWN, iconX, iconY, 0, 0, 10, 10, 10, 10, brightness);
    }
}
