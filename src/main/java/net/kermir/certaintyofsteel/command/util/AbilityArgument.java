package net.kermir.certaintyofsteel.command.util;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.registry.AbilityRegistry;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class AbilityArgument implements ArgumentType<Ability> {

    public static AbilityArgument ability() {
        return new AbilityArgument();
    }

    @Override
    public Ability parse(StringReader stringReader) throws CommandSyntaxException {
        String chatInput = stringReader.readUnquotedString();

        String abilityID = chatInput + stringReader.read() + stringReader.readUnquotedString();

        if (!abilityID.contains(":")) return null;

        @SuppressWarnings("UnnecessaryLocalVariable")
        Ability ability = AbilityRegistry.ABILITIES.getEntries().stream()
                        .filter(a -> a.getId().toString().equals(abilityID))
                        .findFirst()
                        .orElseThrow(() -> new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException(), new Message() {
                            @Override
                            public String getString() {
                                return "Ability not found";
                            }
                        })).get();

        return ability;
    }

    public static Ability getAbility(CommandContext<CommandSourceStack> pContext, String pName) throws CommandSyntaxException {
        Ability ability = pContext.getArgument(pName, Ability.class);

        if (ability == null) CertaintyOfSteel.LOGGER.debug("ability is null.. bruh");

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
