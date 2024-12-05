package net.kermir.certaintyofsteel.networking.packets.util.sample;

import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RequestAndroidPlayerPacket {
    protected UUID uuid;

    public RequestAndroidPlayerPacket(UUID uuid) {
        this.uuid = uuid;
    }

    public RequestAndroidPlayerPacket(FriendlyByteBuf buf) {
        this.uuid = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        UUID androidUUID = this.uuid;

        context.enqueueWork(() -> {
            CertaintyOfSteel.LOGGER.debug("recAP lol val: {}");
        });



        return true;
    }
}
