package net.kermir.certaintyofsteel.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.TreeMap;

public class TextUtil {
    public static void renderString(PoseStack poseStack, String string, int pX, int pY, int blitOffset, int pColor) {
        renderString(poseStack, new TextComponent(string), pX, pY, blitOffset, pColor);
    }

    public static void renderString(PoseStack poseStack, Component text, int pX, int pY, int blitOffset, int pColor) {
        poseStack.translate(0,0, blitOffset);
        GuiComponent.drawString(poseStack, Minecraft.getInstance().font, text, pX, pY, pColor);
        poseStack.translate(0,0,-blitOffset);
    }

    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public final static String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }
}
