package net.kermir.certaintyofsteel.android.abilities.util;

import com.google.gson.JsonObject;
import net.kermir.certaintyofsteel.android.AndroidPlayer;
import net.kermir.certaintyofsteel.android.abilities.data.AbilityTreeJL;
import net.kermir.certaintyofsteel.util.json.JsonOptionalObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.IForgeRegistryEntry;


public class Ability implements IForgeRegistryEntry<Ability>, INBTSerializable<CompoundTag> {
    protected int maxLevel = 0;
    protected int level = 1;
    protected int cooldown = 20; //TICKS

    public Ability() {

    }

    public final Component name() {
        return new TranslatableComponent(String.format("ability.%s.%s.name", getRegistryName().getNamespace(), getRegistryName().getPath()));
    }

    public final Component description() {
        return new TranslatableComponent(String.format("ability.%s.%s.description", getRegistryName().getNamespace(), getRegistryName().getPath()));
    }

    public void loadSettings(JsonObject jsonObject) {
        this.maxLevel = jsonObject.get("max_level").getAsInt();
    }


    //Information to load and save

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("level", level);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.level = nbt.getInt("level");
    }

    public boolean hasRequirements(AndroidPlayer androidPlayer) {

        JsonObject json = AbilityTreeJL.ABILITY_TREE_DATA.get(this.getRegistryName().toString()).getAsJsonObject();
        if (json != null) {

            if (json.has("requirements")) {
                JsonObject dependencies = json.getAsJsonObject("requirements");
                if (dependencies.has("ability")) {
                    JsonObject abilityDep = dependencies.getAsJsonObject("ability");
                    for (String key : abilityDep.keySet()) {
                        int levelRequirement = abilityDep.get(key).getAsInt();

                        if (androidPlayer.getAbilityLevel(key) < levelRequirement) return false;
                    }
                }
            }
        }

        return true;
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

    public boolean onAblityAdded(AndroidPlayer androidPlayer) {
        return true;
    }

    //Ignore these
    private ResourceLocation registryName;

    @Override
    public Ability setRegistryName(ResourceLocation name) {
        this.registryName = name;
        return this;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return this.registryName;
    }


    @Override
    public Class<Ability> getRegistryType() {
        return Ability.class;
    }
}
