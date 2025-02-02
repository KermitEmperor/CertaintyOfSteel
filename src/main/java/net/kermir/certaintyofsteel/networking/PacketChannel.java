package net.kermir.certaintyofsteel.networking;

import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.networking.packets.*;
import net.kermir.certaintyofsteel.networking.packets.util.sample.GetAndroidPlayerPacket;
import net.kermir.certaintyofsteel.networking.packets.util.sample.RequestAndroidPlayerPacket;
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

        net.messageBuilder(RequestClientAndroidInstance.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(RequestClientAndroidInstance::encode)
                .decoder(RequestClientAndroidInstance::new)
                .consumer(RequestClientAndroidInstance::handle)
                .add();

        net.messageBuilder(RequestServerAndroidInstance.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(RequestServerAndroidInstance::encode)
                .decoder(RequestServerAndroidInstance::new)
                .consumer(RequestServerAndroidInstance::handle)
                .add();

        net.messageBuilder(UpdateClientAndroidInstance.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UpdateClientAndroidInstance::encode)
                .decoder(UpdateClientAndroidInstance::new)
                .consumer(UpdateClientAndroidInstance::handle)
                .add();

        net.messageBuilder(UpdateServerAndroidInstance.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(UpdateServerAndroidInstance::encode)
                .decoder(UpdateServerAndroidInstance::new)
                .consumer(UpdateServerAndroidInstance::handle)
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
