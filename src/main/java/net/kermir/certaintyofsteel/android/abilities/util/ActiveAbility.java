package net.kermir.certaintyofsteel.android.abilities.util;

import net.minecraft.server.level.ServerPlayer;

public interface ActiveAbility {
    boolean perform(ServerPlayer player);
}
