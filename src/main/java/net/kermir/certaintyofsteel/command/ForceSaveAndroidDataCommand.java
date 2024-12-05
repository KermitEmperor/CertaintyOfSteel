package net.kermir.certaintyofsteel.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.command.util.CommandUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class ForceSaveAndroidDataCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        CertaintyOfSteel.LOGGER.debug("also yes");

        dispatcher.register(Commands.literal("saveandroids")
                .requires((source) -> source.hasPermission(2))
                .executes((source) -> forceSave(source.getSource()))
        );
    }


    private static int forceSave(CommandSourceStack sourceStack) {
        DimensionDataStorage dataStorage = CommandUtil.getDataStorage(sourceStack);
        dataStorage.save();


        return Command.SINGLE_SUCCESS;
    }
}
