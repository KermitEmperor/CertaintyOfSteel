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

import java.io.*;
import java.util.*;

//Serializable interface is for the PacketObjectUtil
//TODO well the whole AndroidPlayer class bruh
public class AndroidPlayer implements Serializable, INBTSerializable<CompoundTag> {
    private HashMap<String, CompoundTag> unlockedAbilities = new HashMap<>();


    //TODO implement remembering what abilities are unlocked

    public AndroidPlayer() {

    }

    public AndroidPlayer(Map<String, CompoundTag> unlockedAbilities) {
        this.unlockedAbilities = new HashMap<>(unlockedAbilities);
    }

    public void addUnlockedAbility(Ability ability) {
        if (ability.onAblityAdded(this)) {
            this.unlockedAbilities.put(ability.getRegistryName().toString(), ability.serializeNBT());
        }
    }

    public void removeAbility(Ability ability) {
        this.unlockedAbilities.remove(ability.getRegistryName().toString());
    }

    public int unlockedAbilitiesCount() {
        return this.unlockedAbilities.keySet().size();
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

    //TODO Codec for AndroidPlayer
    public static final Codec<AndroidPlayer> ANDROID_PLAYER_CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                //
                Codec.unboundedMap(Codec.STRING, CompoundTag.CODEC).fieldOf("unlockedAbilities").forGetter(AndroidPlayer::getUnlockedAbilitiesCodec)
        ).apply(instance, AndroidPlayer::new)
    );

    private static Map<String, CompoundTag> getUnlockedAbilitiesCodec(AndroidPlayer androidPlayer) {
        CertaintyOfSteel.LOGGER.debug("{}", androidPlayer.unlockedAbilities);
        return androidPlayer.unlockedAbilities != null ? androidPlayer.unlockedAbilities : new HashMap<>();
    }
}
