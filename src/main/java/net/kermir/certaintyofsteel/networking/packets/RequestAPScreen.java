package net.kermir.certaintyofsteel.networking.packets;

import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.networking.PacketChannel;
import net.kermir.certaintyofsteel.networking.packets.util.sample.RequestAndroidPlayerPacket;
import net.kermir.certaintyofsteel.android.AndroidPlayer;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

public class RequestAPScreen extends RequestAndroidPlayerPacket {
    public RequestAPScreen(FriendlyByteBuf buf) {
        super(buf);
    }

    public RequestAPScreen(UUID uuid) {
        super(uuid);
    }

    @Override
    public void action(NetworkEvent.Context context, UUID androidUUID) {
        if (context.getSender() == null) {
            CertaintyOfSteel.LOGGER.warn("RequestAPScreen sender was null!");
            return;
        }

        ServerPlayer sender = context.getSender();

        if (sender.getServer() == null) {
            CertaintyOfSteel.LOGGER.warn("RequestAPScreen server was null!");
            return;
        }

        MinecraftServer server = sender.getServer();

        AndroidsSD androidsSD = server.overworld().getDataStorage().get(AndroidsSD::load, AndroidsSD.ID);
        if (androidsSD == null) {
            CertaintyOfSteel.LOGGER.warn("RequestAPScreen AndroidsSD was null!");
            return;
        }

        AndroidPlayer androidPlayer = androidsSD.getAndroid(this.uuid);
        if (androidPlayer == null) {
            CertaintyOfSteel.LOGGER.warn("{} ({}) isn't recognized as an android!", server.getPlayerList().getPlayer(uuid).getDisplayName().getString(), uuid);
            return;
        }
        PacketChannel.sendToClient(new OpenAPScreen(this.uuid, androidPlayer), context.getSender());
    }
}
