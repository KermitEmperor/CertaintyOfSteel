package net.kermir.certaintyofsteel.command.util;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.android.AbilityRegistry;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class AbilityArgument implements ArgumentType<Ability> {

    public static AbilityArgument ability() {
        return new AbilityArgument();
    }

    @Override
    public Ability parse(StringReader stringReader) throws CommandSyntaxException {
        String chatInput = stringReader.getString();

        List<String> chatInputSplitted = List.of(chatInput.split("\\s+"));
        String abilityID = chatInputSplitted.get(chatInputSplitted.indexOf("grant")+1);

        if (!abilityID.contains(":")) return null;

        Ability ability = AbilityRegistry.ABILITIES.getEntries().stream()
                        .filter(a -> {
                            String huh = a.getId().toString();
                            CertaintyOfSteel.LOGGER.debug(huh);
                            return huh.equals(abilityID);
                        })
                        .findFirst()
                        .orElseThrow(() -> new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException(), new Message() {
                            @Override
                            public String getString() {
                                return "Ability not found";
                            }
                        })).get();

        /* TODO whatever is this
            [23:10:01] [Render thread/INFO] [minecraft/ChatComponent]: [CHAT] Expected whitespace to end one argument, but found trailing data
            [23:10:01] [Render thread/INFO] [minecraft/ChatComponent]: [CHAT] ...Dev grant certaintyofsteel:generic_ability<--[HERE]
        */

        CertaintyOfSteel.LOGGER.debug(ability.getRegistryName().toString());

        return ability;
    }

    public static Ability getAbility(CommandContext<CommandSourceStack> pContext, String pName) throws CommandSyntaxException {
        Ability ability = pContext.getArgument(pName, Ability.class);

        CertaintyOfSteel.LOGGER.debug("E");

        if (ability != null)
            CertaintyOfSteel.LOGGER.debug(ability.getRegistryName().toString());
        else CertaintyOfSteel.LOGGER.debug("ability is null.. bruh");


        return ability;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(AbilityRegistry.ABILITIES.getEntries().stream().map((RegistryObject::getId)), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ArgumentType.super.getExamples();
    }
}
