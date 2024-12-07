package net.kermir.certaintyofsteel.android.abilities.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;


public class Ability implements IForgeRegistryEntry<Ability> {

    protected int maxLevel = 0;
    protected int cooldown = 20; //TICKS

    public Ability() {

    }

    public final Component name() {
        return new TranslatableComponent(String.format("ability.%s.%s.name", getRegistryName().getNamespace(), getRegistryName().getPath()));
    }

    public final Component description() {
        return new TranslatableComponent(String.format("ability.%s.%s.description", getRegistryName().getNamespace(), getRegistryName().getPath()));
    }


    //--------------------------BUILDER--------------------------
    public static class AbilityBuilder extends Ability {
        public AbilityBuilder() {

        }

        public AbilityBuilder setMaxLevel(int maxLevel) {
            this.maxLevel = maxLevel;
            return this;
        }

        public AbilityBuilder setCooldown(int ticks) {
            this.cooldown = ticks;
            return this;
        }
    }

    //Ignore these
    private ResourceLocation registryName;

    @Override
    public Ability setRegistryName(ResourceLocation name) {
        this.registryName = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.registryName;
    }


    @Override
    public Class<Ability> getRegistryType() {
        return Ability.class;
    }
}
