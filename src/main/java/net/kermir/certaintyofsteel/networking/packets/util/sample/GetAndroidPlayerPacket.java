package net.kermir.certaintyofsteel.networking.packets.util.sample;

import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.android.AndroidPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class GetAndroidPlayerPacket {
    protected CompoundTag androidTag;
    protected UUID androidUUID;

    public GetAndroidPlayerPacket(UUID uuid, AndroidPlayer androidPlayer) {
        this.androidUUID = uuid;
        this.androidTag = androidPlayer.serializeNBT();
    }

    public GetAndroidPlayerPacket(FriendlyByteBuf buf) {
        this.androidUUID = buf.readUUID();
        this.androidTag = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.androidUUID);
        buf.writeNbt(this.androidTag);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        AndroidPlayer androidPlayer = new AndroidPlayer();
        androidPlayer.deserializeNBT(this.androidTag);
        UUID uuid = this.androidUUID;

        context.enqueueWork(() -> {
            CertaintyOfSteel.LOGGER.debug("recAP lol val: {}",androidPlayer.getLol());
        });



        return true;
    }
}
