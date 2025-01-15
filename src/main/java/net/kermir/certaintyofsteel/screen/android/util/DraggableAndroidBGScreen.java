package net.kermir.certaintyofsteel.screen.android.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.HashSet;

public class DraggableAndroidBGScreen extends AndroidScreen {
    protected int xOffset;
    protected int yOffset;
    protected int xOffsetGlobal;
    protected int yOffsetGlobal;
    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private boolean isDragging = false;
    protected final HashSet<AbstractWidget> draggableWidgets = new HashSet<>();

    protected DraggableAndroidBGScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        this.xOffset = 0;
        this.yOffset = 0;
        super.init();
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        for (AbstractWidget widget : draggableWidgets ) {
            widget.x += xOffset;
            widget.y += yOffset;
        }

        xOffsetGlobal += xOffset;
        yOffsetGlobal += yOffset;

        this.xOffset = 0;
        this.yOffset = 0;
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (pButton == 1) {
            if (!isDragging) {
                isDragging = true;
                lastMouseX = pMouseX;
                lastMouseY = pMouseY;
            }

            //jank

            int deltaX = (int) (pMouseX - lastMouseX);
            int deltaY = (int) (pMouseY - lastMouseY);

            this.xOffset += deltaX;
            this.yOffset += deltaY;

            lastMouseX = pMouseX;
            lastMouseY = pMouseY;
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 1) {
            // End dragging
            isDragging = false;
        }
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }


    protected <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableDraggableWidget(T pWidget) {
        if (pWidget instanceof AbstractWidget abstractWidget) draggableWidgets.add(abstractWidget);
        return super.addRenderableWidget(pWidget);
    }

    @Override
    protected void removeWidget(GuiEventListener pListener) {
        if (pListener instanceof AbstractWidget abstractWidget) {
            draggableWidgets.remove(abstractWidget);
        }
        super.removeWidget(pListener);
    }

    public void draggableHLine(PoseStack pPoseStack, int pMinX, int pMaxX, int pY, int pColor) {
        this.hLine(pPoseStack, pMinX+this.xOffset, pMaxX+this.xOffset, pY+this.yOffset ,pColor);
    }

    public void draggableVLine(PoseStack pPoseStack, int pX, int pMinY, int pMaxY, int pColor) {
        this.vLine(pPoseStack, pX+this.xOffset, pMinY+this.yOffset, pMaxY+this.yOffset, pColor);
    }
}
