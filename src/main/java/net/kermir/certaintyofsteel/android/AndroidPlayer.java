package net.kermir.certaintyofsteel.android;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

//Serializable interface is for the PacketObjectUtil
//TODO well the whole AndroidPlayer class bruh
public class AndroidPlayer implements Serializable, INBTSerializable<CompoundTag> {
    protected HashMap<String, CompoundTag> unlockedAbilities = new HashMap<>();

    public AndroidPlayer() {

    }

    public AndroidPlayer(Map<String, CompoundTag> unlockedAbilities) {
        this.unlockedAbilities = new HashMap<>(unlockedAbilities);
    }

    public boolean addUnlockedAbility(Ability ability) {
        if (ability.onAblityAdded(this)) {
            if (hasAbility(ability)) return false;
            this.unlockedAbilities.put(ability.getRegistryName().toString(), ability.serializeNBT());
            return true;
        }
        return false;
    }

    public boolean removeAbility(Ability ability) {
        if (hasAbility(ability)) {
            this.unlockedAbilities.remove(ability.getRegistryName().toString());
            return true;
        } else return false;
    }

    public int unlockedAbilitiesCount() {
        return this.unlockedAbilities.keySet().size();
    }

    public boolean hasAbility(Ability ability) {
        if (ability == null)
            return false;
        return unlockedAbilities.containsKey(ability.getRegistryName().toString());
    }

    public boolean hasAbility(String abilityRegistryName) {
        return unlockedAbilities.containsKey(abilityRegistryName);
    }

    public Optional<Ability> ifHasAbility(Ability ability) {
        return Optional.of(ability);
    }

    @Nullable
    public CompoundTag getAbilityInfo(Ability ability) {

        if (hasAbility(ability))
            return unlockedAbilities.get(ability.getRegistryName().toString());
        else return null;
    }

    @Nullable
    public CompoundTag getAbilityInfo(String abilityRegistryName) {
        if (hasAbility(abilityRegistryName))
            return unlockedAbilities.get(abilityRegistryName);
        else return null;
    }

    public int getAbilityLevel(String abilityRegistryName) {
        if (hasAbility(abilityRegistryName)) {
            if (getAbilityInfo(abilityRegistryName).contains("level"))
                return getAbilityInfo(abilityRegistryName).getInt("level");
            else return 0;
        } else return 0;
    }

    public int getAbilityLevel(Ability ability) {
        if (hasAbility(ability)) {
            if (getAbilityInfo(ability).contains("level"))
                return getAbilityInfo(ability).getInt("level");
            else return 0;
        } else return 0;
    }


    //Any newcomer trying to see how to serialize stuff and learn from it
    //Please don't look, this is just plain Lobotomy in here
    @Override
    public CompoundTag serializeNBT() {
        CertaintyOfSteel.LOGGER.debug("Serializing Android player");
        CompoundTag nbt = new CompoundTag();

        DataResult<Tag> result = ANDROID_PLAYER_CODEC.encodeStart(NbtOps.INSTANCE, this);

        result.resultOrPartial((tag) -> {
            CertaintyOfSteel.LOGGER.error("UH OH! AndroidPlayer::serializeNBT only recieved partial result in Codec");
        }).ifPresent((tag -> {
            nbt.put("AndroidInfo", tag);
        }));

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        CertaintyOfSteel.LOGGER.debug("Deserializing Android player");
        DataResult<AndroidPlayer> dataResult = ANDROID_PLAYER_CODEC.parse(NbtOps.INSTANCE, nbt.get("AndroidInfo"));
        dataResult.result().ifPresent((androidPlayer -> {
            this.unlockedAbilities = androidPlayer.unlockedAbilities;
        }));
    }
    public static final Codec<AndroidPlayer> ANDROID_PLAYER_CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                //
                Codec.unboundedMap(Codec.STRING, CompoundTag.CODEC).fieldOf("unlockedAbilities").forGetter(AndroidPlayer::getUnlockedAbilitiesCodec)
        ).apply(instance, AndroidPlayer::new)
    );

    private static Map<String, CompoundTag> getUnlockedAbilitiesCodec(AndroidPlayer androidPlayer) {
        return androidPlayer.unlockedAbilities != null ? androidPlayer.unlockedAbilities : new HashMap<>();
    }
}
