package net.kermir.certaintyofsteel.screen.android.util;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AndroidScreen extends Screen {
    protected AndroidScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
