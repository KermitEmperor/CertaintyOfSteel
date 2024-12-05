package net.kermir.certaintyofsteel.networking.packets;

import net.kermir.certaintyofsteel.networking.packets.util.GetAndroidPlayerPacket;
import net.kermir.certaintyofsteel.player.android.AndroidPlayer;
import net.kermir.certaintyofsteel.screen.AndroidAbilitiesScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class OpenAPScreen extends GetAndroidPlayerPacket {
    public OpenAPScreen(UUID uuid, AndroidPlayer androidPlayer) {
        super(uuid, androidPlayer);
    }

    public OpenAPScreen(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        AndroidPlayer recievedAndroid = this.android;
        UUID uuid = this.androidUUID;

        //we are on client here
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.getConnection() == null) return;
            String playerName = mc.getConnection().getPlayerInfo(uuid).getProfile().getName();
            mc.setScreen(new AndroidAbilitiesScreen(playerName, recievedAndroid));
        });

        return true;
    }
}
