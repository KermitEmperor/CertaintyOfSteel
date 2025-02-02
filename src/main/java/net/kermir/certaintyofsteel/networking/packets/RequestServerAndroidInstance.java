package net.kermir.certaintyofsteel.networking.packets;

import net.kermir.certaintyofsteel.android.LocalAndroidPlayer;
import net.kermir.certaintyofsteel.networking.PacketChannel;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

//Request FOR Server
public class RequestServerAndroidInstance extends RequestClientAndroidInstance{
    public RequestServerAndroidInstance(UUID uuid) {
        super(uuid);
    }

    public RequestServerAndroidInstance(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void action(NetworkEvent.Context context, UUID androidUUID) {

        PacketChannel.sendToServer(new UpdateServerAndroidInstance(androidUUID, LocalAndroidPlayer.INSTANCE));
    }
}
