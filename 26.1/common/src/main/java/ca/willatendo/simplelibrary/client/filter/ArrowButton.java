package ca.willatendo.simplelibrary.client.filter;

import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

public class ArrowButton extends Button {
    public static final Identifier ARROW_UP = SimpleCoreUtils.resource("textures/gui/arrow_up.png");
    public static final Identifier ARROW_DOWN = SimpleCoreUtils.resource("textures/gui/arrow_down.png");

    private final boolean up;

    public ArrowButton(int x, int y, boolean up, OnPress onPress) {
        super(x, y, 20, 20, CommonComponents.EMPTY, onPress, Button.DEFAULT_NARRATION);
        this.up = up;
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderDefaultSprite(guiGraphics);
        int iconX = this.getX() + (this.width - 10) / 2;
        int iconY = this.getY() + (this.height - 10) / 2;
        int brightness = ARGB.scaleRGB(0xFFFFFFFF, this.active ? 1.0F : 0.5F);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.up ? ARROW_UP : ARROW_DOWN, iconX, iconY, 0, 0, 10, 10, 10, 10, brightness);
    }
}
