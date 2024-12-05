package net.kermir.certaintyofsteel.networking.packets.util;

import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.player.android.AndroidPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class GetAndroidPlayerPacket {
    protected AndroidPlayer android;
    protected UUID androidUUID;

    public GetAndroidPlayerPacket(UUID uuid, AndroidPlayer androidPlayer) {
        this.androidUUID = uuid;
        this.android = androidPlayer;
    }

    public GetAndroidPlayerPacket(FriendlyByteBuf buf) {
        this.androidUUID = buf.readUUID();
        this.android = AndroidPlayer.readPacket(buf);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.androidUUID);
        this.android.writePacket(buf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        AndroidPlayer recievedAndroid = this.android;
        UUID uuid = this.androidUUID;

        context.enqueueWork(() -> {
            CertaintyOfSteel.LOGGER.debug("recAP lol val: {}",recievedAndroid.getLol());
        });



        return true;
    }
}
