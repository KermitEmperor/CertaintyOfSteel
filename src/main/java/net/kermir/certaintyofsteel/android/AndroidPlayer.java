package net.kermir.certaintyofsteel.android;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.*;

//Serializable interface is for the PacketObjectUtil
public class AndroidPlayer implements Serializable, INBTSerializable<CompoundTag> {
    private String lol;

    public void setLol(String lol) {
        this.lol = lol;
    }

    public String getLol() {
        return this.lol != null ? this.lol : "";
    }

    public AndroidPlayer() {

    }

    public AndroidPlayer(String lol) {
        this.lol = lol;
    }



    //Any newcomer trying to see how to serialize stuff and learn from it
    //Please don't look, this is just plain Lobotomy in here
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        DataResult<Tag> result = ANDROID_PLAYER_CODEC.encodeStart(NbtOps.INSTANCE, this);
        result.resultOrPartial((tag) -> {
            nbt.putString("AndroidInfo", tag);
        });

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        DataResult<AndroidPlayer> result = ANDROID_PLAYER_CODEC.parse(NbtOps.INSTANCE, nbt.get("AndroidInfo"));
        result.result().ifPresent((androidPlayer -> {
            this.lol = androidPlayer.lol;
        }));

        CertaintyOfSteel.LOGGER.debug(this.lol);
    }

    public static final Codec<AndroidPlayer> ANDROID_PLAYER_CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                Codec.STRING.optionalFieldOf("lol", "notnulllol").forGetter(AndroidPlayer::getLol)
        ).apply(instance, AndroidPlayer::new)
    );
}
