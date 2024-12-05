package net.kermir.certaintyofsteel.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kermir.certaintyofsteel.command.util.CommandUtil;
import net.kermir.certaintyofsteel.networking.PacketChannel;
import net.kermir.certaintyofsteel.networking.packets.OpenAPScreen;
import net.kermir.certaintyofsteel.player.android.AndroidPlayer;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.concurrent.atomic.AtomicBoolean;

public class OpenAPScreenCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("openandroidpanel")
                .requires((source) -> source.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.player()).executes((source) ->
                        openPanel(source.getSource(), EntityArgument.getPlayer(source, "target"))
                ))
        );
    }


    private static int openPanel(CommandSourceStack sourceStack, ServerPlayer pTarget) throws CommandSyntaxException {
        boolean isAndroid = false;

        AndroidsSD androidData = CommandUtil.getAndroidSD(sourceStack);

        AndroidPlayer androidPlayer = null;
        if (androidData != null) {
            androidPlayer = androidData.getAndroid(pTarget.getUUID());
        }

        if (androidPlayer != null) {
            PacketChannel.sendToClient(new OpenAPScreen(pTarget.getUUID(), androidPlayer), sourceStack.getPlayerOrException());
            isAndroid = true;
        }

        TextComponent response;
        if (isAndroid) {
            response = new TextComponent(String.format("Successfully opened %s's Android panel", pTarget.getDisplayName().getString()));
        } else {
            response = new TextComponent(String.format("Failed to open %s's Android panel", pTarget.getDisplayName().getString()));
        }

        sourceStack.sendSuccess(response, true);

        return Command.SINGLE_SUCCESS;
    }
}
