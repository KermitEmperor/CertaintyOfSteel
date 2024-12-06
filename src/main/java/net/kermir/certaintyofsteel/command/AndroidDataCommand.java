package net.kermir.certaintyofsteel.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.command.util.CommandUtil;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class AndroidDataCommand {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){

        dispatcher.register(Commands.literal("androiddata")
            .requires((source) -> source.hasPermission(2))
            .then(Commands.literal("get")
                .executes(source ->
                        getData(source.getSource(), source.getSource().getPlayerOrException())
                )
                .then(Commands.argument("target", EntityArgument.player())
                    .executes((source) ->
                        getData(source.getSource(), EntityArgument.getPlayer(source, "target"))
                    )
                )
            )
            .then(Commands.literal("set")
                .then(Commands.argument("target", EntityArgument.player())
                    .executes((source) -> {
                        if (source.getSource().getEntity() instanceof Player player)
                            player.sendMessage(new TextComponent("This command is not implemented :(").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                        return 0;
                    })
                )
            )
        );


        /*
        .then(Commands.argument("target", EntityArgument.entities()).executes((source) -> {
            return executeOnTarget(source.getSource(), EntityArgument.getPlayer(source, "target"));
        })));*/
    }


    private static int getData(CommandSourceStack sourceStack, ServerPlayer pTarget) {
        AndroidsSD androidData = CommandUtil.getAndroidSD(sourceStack);

        pTarget.sendMessage(new TextComponent(String.format("%s", androidData.getAndroids().containsKey(pTarget.getUUID()))), Util.NIL_UUID);
        sourceStack.sendSuccess(new TextComponent("success"), false);

        return Command.SINGLE_SUCCESS;
    }
}
