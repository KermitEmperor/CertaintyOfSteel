package net.kermir.certaintyofsteel.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.kermir.certaintyofsteel.android.AndroidPlayer;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.kermir.certaintyofsteel.command.util.AbilityArgument;
import net.kermir.certaintyofsteel.command.util.CommandUtil;
import net.kermir.certaintyofsteel.networking.PacketChannel;
import net.kermir.certaintyofsteel.networking.packets.RequestClientAndroidInstance;
import net.kermir.certaintyofsteel.networking.packets.UpdateClientAndroidInstance;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class AndroidAbilityCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("androidability")
                .requires((source) -> source.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.player())
                        .then(Commands.literal("grant")
                                .then(Commands.argument("ability", AbilityArgument.ability())
                                        .executes(commandContext ->
                                                abilityGetOrRemove(
                                                        commandContext.getSource(),
                                                        EntityArgument.getPlayer(commandContext, "target"),
                                                        AbilityArgument.getAbility(commandContext, "ability"),
                                                        true
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("revoke")
                                .then(Commands.argument("ability", AbilityArgument.ability())
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
                        .then(Commands.literal("count")
                                .executes(commandContext ->
                                    getAbilityAmount(
                                            commandContext.getSource(),
                                            EntityArgument.getPlayer(commandContext, "target")
                                    )
                                )
                        )
                )
        );
    }

    private static int getAbilityAmount(CommandSourceStack sourceStack, ServerPlayer player) {
        AndroidsSD androidsSD = CommandUtil.getAndroidSD(sourceStack);
        AndroidPlayer androidPlayer = androidsSD.getAndroid(player.getUUID());

        sourceStack.sendSuccess(new TextComponent(String.format("%s has %s abilities unlocked", player.getDisplayName().getString(), androidPlayer.unlockedAbilitiesCount())), true);

        return Command.SINGLE_SUCCESS;
    }


    private static int abilityGetOrRemove(CommandSourceStack sourceStack, ServerPlayer player, Ability ability, boolean add) {
        DimensionDataStorage dataStorage = CommandUtil.getDataStorage(sourceStack);
        AndroidsSD androidsSD = CommandUtil.getAndroidSD(dataStorage);

        AndroidPlayer androidPlayer = androidsSD.getAndroid(player.getUUID());

        boolean result = false;

        if (add) result = androidPlayer.addUnlockedAbility(ability);
        else result = androidPlayer.removeAbility(ability);

        if (add) {
            if (result) sourceStack.sendSuccess(new TranslatableComponent("commands.ability.grant.success", "\""+ability.name().getString()+"\"" , player.getDisplayName().getString()), false);
            else sourceStack.sendFailure(new TranslatableComponent("commands.ability.grant.failure", "\""+ability.name().getString()+"\"" , player.getDisplayName().getString()));
        } else {
            if (result) sourceStack.sendSuccess(new TranslatableComponent("commands.ability.revoke.success", "\""+ability.name().getString()+"\"" , player.getDisplayName().getString()), false);
            else sourceStack.sendFailure(new TranslatableComponent("commands.ability.revoke.failure", "\""+ability.name().getString()+"\"" , player.getDisplayName().getString()));
        }

        if (result) {
            PacketChannel.sendToClient(new UpdateClientAndroidInstance(player.getUUID(), androidPlayer), player);
        }

        androidsSD.setDirty();
        dataStorage.save();


        return Command.SINGLE_SUCCESS;
    }
}
