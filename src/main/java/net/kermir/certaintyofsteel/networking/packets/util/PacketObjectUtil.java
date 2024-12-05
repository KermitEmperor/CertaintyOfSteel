package net.kermir.certaintyofsteel.networking.packets.util;

import net.minecraft.network.FriendlyByteBuf;

import java.io.*;

public class PacketObjectUtil {

    //Should it ever break, just embed it back to AndroidPlayer and remove generic Types

    public static void writePacket(FriendlyByteBuf buf, Serializable object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            buf.writeByteArray(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize object", e);
        }
    }

    public static <T extends Serializable> T readPacket(FriendlyByteBuf buf) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf.readByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            @SuppressWarnings("unchecked")
            T object = (T) objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to serialize object", e);
        }
    }
}
