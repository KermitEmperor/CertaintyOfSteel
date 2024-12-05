package net.kermir.certaintyofsteel.player.android;

import net.minecraft.nbt.CompoundTag;

import java.io.*;

public class AndroidPlayer implements Serializable {
    private boolean lol;

    public void setLol(boolean lol) {
        this.lol = lol;
    }

    public boolean getLol() {
        return this.lol;
    }

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();

        return nbt;
    }

    public void load(CompoundTag nbt) {

    }
}
