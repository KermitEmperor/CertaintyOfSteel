package net.kermir.certaintyofsteel;

import com.mojang.logging.LogUtils;
import net.kermir.certaintyofsteel.android.LocalAndroidPlayer;
import net.kermir.certaintyofsteel.android.abilities.data.AbilitySettingsJL;
import net.kermir.certaintyofsteel.android.abilities.data.AbilityTreeJL;
import net.kermir.certaintyofsteel.networking.packets.UpdateClientAndroidInstance;
import net.kermir.certaintyofsteel.registry.AbilityRegistry;
import net.kermir.certaintyofsteel.command.*;
import net.kermir.certaintyofsteel.command.util.AbilityArgument;
import net.kermir.certaintyofsteel.keybinds.KeyBinding;
import net.kermir.certaintyofsteel.networking.PacketChannel;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.kermir.certaintyofsteel.screen.MenuTypeRegistires;
import net.kermir.certaintyofsteel.screen.android.AndroidAbilitiesScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CertaintyOfSteel.MOD_ID)
public class CertaintyOfSteel {

    public static final String MOD_ID = "certaintyofsteel";

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    public CertaintyOfSteel() {
        IEventBus ModEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEventBus.addListener(this::setup);
        ModEventBus.addListener(this::clientSetup);
        ModEventBus.addListener(this::enqueueIMC);
        ModEventBus.addListener(this::processIMC);
        ModEventBus.addListener(this::onNewRegistry);

        MenuTypeRegistires.register(ModEventBus);
        AbilityRegistry.register(ModEventBus);

        IEventBus ForgeEventBus = MinecraftForge.EVENT_BUS;

        ForgeEventBus.register(this);
        ForgeEventBus.addListener(this::onCommandRegister);
        ForgeEventBus.addListener(this::onLevelLoad);
        ForgeEventBus.addListener(this::onPlayerLeaveClient);
        ForgeEventBus.addListener(this::onPlayerJoin);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Some preinit code
        PacketChannel.register();
        ArgumentTypes.register(MOD_ID + ":ability", AbilityArgument.class, new EmptyArgumentSerializer<>(AbilityArgument::ability));
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(KeyBinding.OPEN_ANDROID_MENU_KEY);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("certaintyofsteel", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // Some example code to receive and process InterModComms from other mods
        //LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.messageSupplier().get()).collect(Collectors.toList()));
    }


    public void onCommandRegister(final RegisterCommandsEvent event) {
        LOGGER.debug("yes");
        AndroidDataCommand.register(event.getDispatcher());
        SetAndroidCommand.register(event.getDispatcher());
        ForceSaveAndroidDataCommand.register(event.getDispatcher());
        OpenAPScreenCommand.register(event.getDispatcher());
        AndroidAbilityCommand.register(event.getDispatcher());
    }

    public void onLevelLoad(final WorldEvent.Load event) {
        if (event.getWorld() != null) {
            MinecraftServer server = event.getWorld().getServer();
            if (server != null) {
                server.overworld().getDataStorage().computeIfAbsent(AndroidsSD::load, AndroidsSD::create, AndroidsSD.ID);
            }
        }
    }

    public void onPlayerLeaveClient(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        CertaintyOfSteel.LOGGER.info("LOGGED OUT");
        LocalAndroidPlayer.reset();
    }

    private void onNewRegistry(final NewRegistryEvent event) {
        CertaintyOfSteel.LOGGER.debug("Creating ability registry");
        AbilityRegistry.registerRegistry(event);
    }

    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            if (AndroidsSD.getInstance().getAndroid(event.getPlayer().getUUID()) != null) {
                PacketChannel.sendToClient(new UpdateClientAndroidInstance(event.getPlayer().getUUID(), AndroidsSD.getInstance().getAndroid(event.getPlayer().getUUID())),(ServerPlayer) event.getPlayer());
                CertaintyOfSteel.LOGGER.info("Sent Android Instance update to {} on join", event.getPlayer().getName().getString());
            }
        }
    }


    @SubscribeEvent
    public void jsonReading(AddReloadListenerEvent event) {
        //ORDER MATTERS!!!!!
        event.addListener(AbilitySettingsJL.instance);
        event.addListener(AbilityTreeJL.instance);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        final Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;


        if (KeyBinding.OPEN_ANDROID_MENU_KEY.consumeClick()) {
            if (LocalAndroidPlayer.INSTANCE != null)
                mc.setScreen(new AndroidAbilitiesScreen(mc.player.getDisplayName().getString()));
        }
    }
}
