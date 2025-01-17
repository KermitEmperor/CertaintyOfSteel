package net.kermir.certaintyofsteel.android;

public class LocalAndroidPlayer extends AndroidPlayer {
    public static AndroidPlayer INSTANCE = new LocalAndroidPlayer(new AndroidPlayer());

    private LocalAndroidPlayer(AndroidPlayer androidPlayer) {
        deserializeNBT(androidPlayer.serializeNBT());
    }

    public static void changeInstance(AndroidPlayer androidPlayer) {
        INSTANCE = new LocalAndroidPlayer(androidPlayer);
    }
}
