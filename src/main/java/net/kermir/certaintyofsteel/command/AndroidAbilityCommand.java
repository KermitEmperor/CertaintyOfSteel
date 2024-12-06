package net.kermir.certaintyofsteel.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.kermir.certaintyofsteel.command.util.AbilityArgument;
import net.kermir.certaintyofsteel.command.util.CommandUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class AndroidAbilityCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("androidability")
                .requires((source) -> source.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.player())
                        .then(Commands.argument("ability", AbilityArgument.ability())
                                .then(Commands.literal("grant")
                                        .executes(commandContext ->
                                                abilityGetOrRemove(
                                                        commandContext.getSource(),
                                                        EntityArgument.getPlayer(commandContext, "target"),
                                                        AbilityArgument.getAbility(commandContext, "ability"),
                                                        true
                                                )
                                        )
                                )
                                .then(Commands.literal("revoke")
                                        .executes(commandContext ->
                                                abilityGetOrRemove(
                                                        commandContext.getSource(),
                                                        EntityArgument.getPlayer(commandContext, "target"),
                                                        AbilityArgument.getAbility(commandContext, "ability"),
                                                        false
                                                )
                                        )
                                )
                        )
                )
        );
    }


    private static int abilityGetOrRemove(CommandSourceStack sourceStack, ServerPlayer player, Ability ability, boolean add) {
        DimensionDataStorage dataStorage = CommandUtil.getDataStorage(sourceStack);
        CommandUtil.getAndroidSD(dataStorage).setDirty();
        dataStorage.save();

        //TODO implement ability grant and revoke


        return Command.SINGLE_SUCCESS;
    }
}
