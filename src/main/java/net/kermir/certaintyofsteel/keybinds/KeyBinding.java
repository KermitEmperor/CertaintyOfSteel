package net.kermir.certaintyofsteel.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    private static final String KEY_CATEGORY = "key.category." + CertaintyOfSteel.MOD_ID + ".main";
    private static final String KEY_ANDROID_MENU = "key." + CertaintyOfSteel.MOD_ID + ".open_android_menu";
    public static final KeyMapping OPEN_ANDROID_MENU_KEY = new KeyMapping(KEY_ANDROID_MENU, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY);
}
