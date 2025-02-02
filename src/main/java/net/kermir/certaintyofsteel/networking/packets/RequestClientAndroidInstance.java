package net.kermir.certaintyofsteel.networking.packets;

import net.kermir.certaintyofsteel.networking.PacketChannel;
import net.kermir.certaintyofsteel.networking.packets.util.sample.RequestAndroidPlayerPacket;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

//Request FOR Client
public class RequestClientAndroidInstance extends RequestAndroidPlayerPacket {
    public RequestClientAndroidInstance(UUID uuid) {
        super(uuid);
    }

    public RequestClientAndroidInstance(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void action(NetworkEvent.Context context, UUID androidUUID) {

        PacketChannel.sendToClient(
                new UpdateClientAndroidInstance(androidUUID, AndroidsSD.getInstance().getAndroid(androidUUID)),
                context.getSender());
    }
}
