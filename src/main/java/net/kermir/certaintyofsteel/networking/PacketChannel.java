package net.kermir.certaintyofsteel.networking;

import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.networking.packets.OpenAPScreen;
import net.kermir.certaintyofsteel.networking.packets.RequestAPScreen;
import net.kermir.certaintyofsteel.networking.packets.util.GetAndroidPlayerPacket;
import net.kermir.certaintyofsteel.networking.packets.util.RequestAndroidPlayerPacket;
import net.kermir.certaintyofsteel.networking.packets.util.SetAndroidPlayerPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketChannel {
    private static SimpleChannel INSTANCE;

    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(CertaintyOfSteel.MOD_ID, "packetchannel"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(GetAndroidPlayerPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(GetAndroidPlayerPacket::encode)
                .decoder(GetAndroidPlayerPacket::new)
                .consumer(GetAndroidPlayerPacket::handle)
                .add();

        net.messageBuilder(RequestAndroidPlayerPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(RequestAndroidPlayerPacket::encode)
                .decoder(RequestAndroidPlayerPacket::new)
                .consumer(RequestAndroidPlayerPacket::handle)
                .add();

        net.messageBuilder(SetAndroidPlayerPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetAndroidPlayerPacket::encode)
                .decoder(SetAndroidPlayerPacket::new)
                .consumer(SetAndroidPlayerPacket::handle)
                .add();

        net.messageBuilder(RequestAPScreen.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(RequestAPScreen::encode)
                .decoder(RequestAPScreen::new)
                .consumer(RequestAPScreen::handle)
                .add();

        net.messageBuilder(OpenAPScreen.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(OpenAPScreen::encode)
                .decoder(OpenAPScreen::new)
                .consumer(OpenAPScreen::handle)
                .add();
    }


    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToAllClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
