package net.kermir.certaintyofsteel.command.util;

import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class CommandUtil {

    public static DimensionDataStorage getDataStorage(CommandSourceStack sourceStack) {
        return sourceStack.getLevel().getServer().overworld().getDataStorage();
    }

    public static AndroidsSD getAndroidSD(CommandSourceStack sourceStack) {
        return getDataStorage(sourceStack).get(AndroidsSD::load, AndroidsSD.ID);
    }
}
