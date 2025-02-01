package net.kermir.certaintyofsteel.android;

import net.kermir.certaintyofsteel.networking.PacketChannel;
import net.kermir.certaintyofsteel.networking.packets.RequestClientAndroidInstance;
import net.minecraft.client.Minecraft;

//TODO Implement Ability unlock and remove that syncs properly
public class LocalAndroidPlayer extends AndroidPlayer {
    public static AndroidPlayer INSTANCE = new LocalAndroidPlayer(new AndroidPlayer());
    private static Minecraft mc = Minecraft.getInstance();

    private LocalAndroidPlayer(AndroidPlayer androidPlayer) {
        deserializeNBT(androidPlayer.serializeNBT());
    }

    public static void changeInstance(AndroidPlayer androidPlayer) {
        INSTANCE = new LocalAndroidPlayer(androidPlayer);
    }

    public static void reset() {
        INSTANCE = null;
    }

    public static void update() {
        if (mc.player != null)
            PacketChannel.sendToServer(new RequestClientAndroidInstance(mc.player.getUUID()));
    }
}
