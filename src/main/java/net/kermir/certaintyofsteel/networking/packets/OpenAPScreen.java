package net.kermir.certaintyofsteel.networking.packets;


import net.kermir.certaintyofsteel.networking.packets.util.sample.GetAndroidPlayerPacket;
import net.kermir.certaintyofsteel.android.AndroidPlayer;
import net.kermir.certaintyofsteel.screen.android.AndroidAbilitiesScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

public class OpenAPScreen extends GetAndroidPlayerPacket {
    public OpenAPScreen(UUID uuid, AndroidPlayer androidPlayer) {
        super(uuid, androidPlayer);
    }

    public OpenAPScreen(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void action(NetworkEvent.Context context, AndroidPlayer androidPlayer, UUID androidUUID) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getConnection() == null) return;
        PlayerInfo playerInfo = mc.getConnection().getPlayerInfo(androidUUID);
        String playerName;
        if (playerInfo != null)
            playerName = playerInfo.getProfile().getName();
        else playerName = "null";
        mc.setScreen(new AndroidAbilitiesScreen(playerName, androidPlayer));
    };
}
