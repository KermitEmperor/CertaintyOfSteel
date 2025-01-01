package net.kermir.certaintyofsteel.screen.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;

public class TextUtil {
    public static void renderString(PoseStack poseStack, String string, int pX, int pY, int blitOffset, int pColor) {
        renderString(poseStack, new TextComponent(string), pX, pY, blitOffset, pColor);
    }

    public static void renderString(PoseStack poseStack, Component text, int pX, int pY, int blitOffset, int pColor) {
        poseStack.translate(0,0, blitOffset);
        GuiComponent.drawString(poseStack, Minecraft.getInstance().font, text, pX, pY, pColor);
        poseStack.translate(0,0,-blitOffset);
    }

    public static void renderCenteredString(PoseStack pPoseStack, String pText, int pX, int pY, int blitOffset, int pColor) {
        renderCenteredString(pPoseStack, new TextComponent(pText), pX, pY, blitOffset, pColor);
    }

    public static void renderCenteredString(PoseStack poseStack, Component pText, int pX, int pY, int blitOffset, int pColor) {
        poseStack.translate(0,0, blitOffset);
        GuiComponent.drawCenteredString(poseStack, Minecraft.getInstance().font, pText, pX, pY, pColor);
        poseStack.translate(0,0, -blitOffset);
    }
}
