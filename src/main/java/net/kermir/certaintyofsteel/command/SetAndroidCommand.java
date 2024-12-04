package net.kermir.certaintyofsteel.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class SetAndroidCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("setandroid")
                .requires((source) -> source.hasPermission(2))
                .then(Commands.literal("add")
                        .executes(source ->
                                add(source.getSource(), source.getSource().getPlayerOrException())
                        )
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes((source) ->
                                        add(source.getSource(), EntityArgument.getPlayer(source, "target"))
                                )
                        )
                )
                .then(Commands.literal("remove")
                        .executes(source ->
                                remove(source.getSource(), source.getSource().getPlayerOrException())
                        )
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes((source) ->
                                    remove(source.getSource(), EntityArgument.getPlayer(source, "target"))
                                )
                        )
                )
        );


        /*
        .then(Commands.argument("target", EntityArgument.entities()).executes((source) -> {
            return executeOnTarget(source.getSource(), EntityArgument.getPlayer(source, "target"));
        })));*/
    }


    private static int add(CommandSourceStack sourceStack, ServerPlayer pTarget) {
        AtomicBoolean ret = new AtomicBoolean(true);
        AtomicBoolean isAndroidAlready = new AtomicBoolean(false);

        AndroidsSD androidData = getDataStorage(sourceStack).get(AndroidsSD::load, AndroidsSD.ID);
        ret.set(ret.get() | androidData != null);
        if (androidData != null) {
            isAndroidAlready.set(!androidData.addAndroid(pTarget));
        }

        TextComponent response;

        if (ret.get() && !isAndroidAlready.get()) {
            response = new TextComponent(String.format("Succesfully turned %s into an Android", pTarget.getDisplayName().getString()));
        } else {
            response = new TextComponent(String.format("Failed to turn %s into an Android", pTarget.getDisplayName().getString()));
        }

        sourceStack.sendSuccess(response, true);

        return Command.SINGLE_SUCCESS;
    }

    private static int remove(CommandSourceStack sourceStack, ServerPlayer pTarget) {
        AtomicBoolean ret = new AtomicBoolean(true);
        AtomicBoolean isAndroidAlready = new AtomicBoolean(false);

        AndroidsSD androidData = getDataStorage(sourceStack).get(AndroidsSD::load, AndroidsSD.ID);
        ret.set(ret.get() | androidData != null);
        if (androidData != null) {
            isAndroidAlready.set(androidData.removeAndroid(pTarget));
        }

        TextComponent response;

        if (ret.get() && isAndroidAlready.get()) {
            response = new TextComponent(String.format("Succesfully turned %s back from an Android", pTarget.getDisplayName().getString()));
        } else {
            response = new TextComponent(String.format("Failed to turn %s back into an Android", pTarget.getDisplayName().getString()));
        }

        sourceStack.sendSuccess(response, true);

        return Command.SINGLE_SUCCESS;
    }

    private static DimensionDataStorage getDataStorage(CommandSourceStack sourceStack) {
        return sourceStack.getLevel().getServer().overworld().getDataStorage();
    }
}
