package net.kermir.certaintyofsteel.networking.packets.util.sample;

import net.kermir.certaintyofsteel.android.AndroidPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

//We extend cuz we have similar info
public class SetAndroidPlayerPacket extends GetAndroidPlayerPacket {

    public SetAndroidPlayerPacket(UUID uuid, AndroidPlayer androidPlayer) {
        super(uuid, androidPlayer);
    }

    public SetAndroidPlayerPacket(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        return true;
    }
}
