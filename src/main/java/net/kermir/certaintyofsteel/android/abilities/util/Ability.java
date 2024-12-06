package net.kermir.certaintyofsteel.android.abilities.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class Ability implements IForgeRegistryEntry<Ability> {
    private ResourceLocation registryName;

    public Ability() {

    }


    //Ignore these
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
