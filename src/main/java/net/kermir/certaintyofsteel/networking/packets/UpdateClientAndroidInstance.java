package net.kermir.certaintyofsteel.networking.packets;

import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.android.AndroidPlayer;
import net.kermir.certaintyofsteel.android.LocalAndroidPlayer;
import net.kermir.certaintyofsteel.networking.packets.util.sample.GetAndroidPlayerPacket;
import net.kermir.certaintyofsteel.screen.android.AndroidAbilitiesScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

//TODO implement this
public class UpdateClientAndroidInstance extends GetAndroidPlayerPacket {
    public UpdateClientAndroidInstance(UUID uuid, AndroidPlayer androidPlayer) {
        super(uuid, androidPlayer);
    }

    public UpdateClientAndroidInstance(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void action(NetworkEvent.Context context, AndroidPlayer androidPlayer, UUID androidUUID) {
        LocalAndroidPlayer.changeInstance(androidPlayer);
    }
}
