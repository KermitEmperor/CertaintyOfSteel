package net.kermir.certaintyofsteel.networking.packets;

import net.kermir.certaintyofsteel.android.AndroidPlayer;
import net.kermir.certaintyofsteel.networking.packets.util.sample.GetAndroidPlayerPacket;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Objects;
import java.util.UUID;

public class UpdateServerAndroidInstance extends GetAndroidPlayerPacket {
    public UpdateServerAndroidInstance(UUID uuid, AndroidPlayer androidPlayer) {
        super(uuid, androidPlayer);
    }

    public UpdateServerAndroidInstance(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void action(NetworkEvent.Context context, AndroidPlayer androidPlayer, UUID androidUUID) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        AndroidsSD sd = server.overworld().getDataStorage().computeIfAbsent(AndroidsSD::load, AndroidsSD::create, AndroidsSD.ID);
        sd.addAndroid(Objects.requireNonNull(server.getPlayerList().getPlayer(androidUUID)));
    }
}
