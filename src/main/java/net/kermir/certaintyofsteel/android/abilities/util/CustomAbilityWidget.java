package net.kermir.certaintyofsteel.android.abilities.util;

import net.kermir.certaintyofsteel.screen.android.widgets.AbilityWidget;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;

import java.util.function.Consumer;
import java.util.function.Function;

public interface CustomAbilityWidget {
    AbilityWidget customWidget(int pX, int pY, Ability ability, Function<AbstractWidget, GuiComponent> addMethod, Consumer<AbstractWidget> removeMethod);
}
