package net.kermir.certaintyofsteel.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class ForceSaveAndroidDataCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        CertaintyOfSteel.LOGGER.debug("also yes");

        dispatcher.register(Commands.literal("saveandroids")
                .requires((source) -> source.hasPermission(2))
                .executes((source) -> forceSave(source.getSource()))
        );


        /*
        .then(Commands.argument("target", EntityArgument.entities()).executes((source) -> {
            return executeOnTarget(source.getSource(), EntityArgument.getPlayer(source, "target"));
        })));*/
    }


    private static int forceSave(CommandSourceStack sourceStack) {
        getDataStorage(sourceStack).save();

        return Command.SINGLE_SUCCESS;
    }

    private static DimensionDataStorage getDataStorage(CommandSourceStack sourceStack) {
        return sourceStack.getLevel().getServer().overworld().getDataStorage();
    }
}
