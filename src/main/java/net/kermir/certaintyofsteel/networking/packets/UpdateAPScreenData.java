package net.kermir.certaintyofsteel.networking.packets;

import net.kermir.certaintyofsteel.android.AndroidPlayer;
import net.kermir.certaintyofsteel.networking.packets.util.sample.GetAndroidPlayerPacket;
import net.kermir.certaintyofsteel.screen.android.AndroidAbilitiesScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

//TODO implement this
public class UpdateAPScreenData extends GetAndroidPlayerPacket {
    public UpdateAPScreenData(UUID uuid, AndroidPlayer androidPlayer) {
        super(uuid, androidPlayer);
    }

    public UpdateAPScreenData(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void action(NetworkEvent.Context context, AndroidPlayer androidPlayer, UUID androidUUID) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return;
        if (mc.screen instanceof AndroidAbilitiesScreen screen) {
            screen.updateAndroid(androidPlayer);
        }

    }
}
