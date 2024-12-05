package net.kermir.certaintyofsteel.player.android;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

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

    public void writePacket(FriendlyByteBuf buf) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            buf.writeByteArray(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize object", e);
        }
    }

    public static AndroidPlayer readPacket(FriendlyByteBuf buf) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf.readByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            @SuppressWarnings("unchecked")
            AndroidPlayer object = (AndroidPlayer) objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to serialize object", e);
        }
    }
}
