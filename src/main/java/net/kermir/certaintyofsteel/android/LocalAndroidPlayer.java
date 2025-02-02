package net.kermir.certaintyofsteel.android;

import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.kermir.certaintyofsteel.networking.PacketChannel;
import net.kermir.certaintyofsteel.networking.packets.RequestClientAndroidInstance;
import net.kermir.certaintyofsteel.networking.packets.UpdateServerAndroidInstance;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.minecraft.client.Minecraft;

//TODO Implement Ability unlock and remove that syncs properly
public class LocalAndroidPlayer extends AndroidPlayer {
    public static LocalAndroidPlayer INSTANCE = new LocalAndroidPlayer(new AndroidPlayer());
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

    @Override
    public boolean addUnlockedAbility(Ability ability) {
        if (!super.addUnlockedAbility(ability)) return false;

        return updateServer();
    }


    public boolean updateServer() {
        if (mc.player == null) return false;
        PacketChannel.sendToServer(new UpdateServerAndroidInstance(mc.player.getUUID(),this));
        return true;
    }

    public static void updateClient() {
        if (mc.player != null)
            PacketChannel.sendToServer(new RequestClientAndroidInstance(mc.player.getUUID()));
    }
}
