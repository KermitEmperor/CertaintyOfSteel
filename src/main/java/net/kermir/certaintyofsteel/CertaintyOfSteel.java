package net.kermir.certaintyofsteel;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.kermir.certaintyofsteel.android.AbilityRegistry;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.kermir.certaintyofsteel.command.*;
import net.kermir.certaintyofsteel.command.util.AbilityArgument;
import net.kermir.certaintyofsteel.keybinds.KeyBinding;
import net.kermir.certaintyofsteel.networking.PacketChannel;
import net.kermir.certaintyofsteel.networking.packets.RequestAPScreen;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.kermir.certaintyofsteel.screen.MenuTypeRegistires;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
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
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import org.slf4j.Logger;

import java.awt.desktop.AboutEvent;

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

        //AbilityRegistry.ABILITIES.register(ModEventBus);

        MenuTypeRegistires.register(ModEventBus);
        AbilityRegistry.register(ModEventBus);

        IEventBus ForgeEventBus = MinecraftForge.EVENT_BUS;

        ForgeEventBus.register(this);
        ForgeEventBus.addListener(this::onCommandRegister);
        ForgeEventBus.addListener(this::onLevelLoad);
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

    public static IForgeRegistry<Ability> ABILITIES;

    private void onNewRegistry(final NewRegistryEvent event) {
        CertaintyOfSteel.LOGGER.debug("HMMMMMMM 621");

        //public static final Registry<ISkill<?>> SKILLS = new RegistryBuilder<>(VampirismRegistries.Keys.SKILL).callback(new SkillCallbacks()).sync(true).create();
        event.create(makeRegistry(new ResourceLocation(MOD_ID, "ability"), Ability.class, Integer.MAX_VALUE >> 5), r -> ABILITIES = r);

        //AbilityRegistry.ABILITIES.makeRegistry(Ability.class, RegistryBuilder::new);
    }

    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation name, Class<T> type, int max) {
        return new RegistryBuilder<T>().setName(name).setType(type).setMaxID(max);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        final Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;


        if (KeyBinding.OPEN_ANDROID_MENU_KEY.consumeClick()) {
            //mc.setScreen(new AndroidScreen(mc.player.getDisplayName().getString(), null));
            PacketChannel.sendToServer(new RequestAPScreen(mc.player.getUUID()));
        }
    }
}
