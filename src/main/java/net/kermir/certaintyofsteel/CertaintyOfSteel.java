package net.kermir.certaintyofsteel;

import com.mojang.logging.LogUtils;
import net.kermir.certaintyofsteel.command.AndroidDataCommand;
import net.kermir.certaintyofsteel.command.ForceSaveAndroidDataCommand;
import net.kermir.certaintyofsteel.command.SetAndroidCommand;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CertaintyOfSteel.MOD_ID)
public class CertaintyOfSteel {

    public static final String MOD_ID = "certaintyofsteel";

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public CertaintyOfSteel() {
        IEventBus ModEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEventBus.addListener(this::setup);
        ModEventBus.addListener(this::enqueueIMC);
        ModEventBus.addListener(this::processIMC);

        IEventBus ForgeEventBus = MinecraftForge.EVENT_BUS;

        // Register ourselves for server and other game events we are interested in
        ForgeEventBus.register(this);
        ForgeEventBus.addListener(this::onCommandRegister);
        ForgeEventBus.addListener(this::onLevelLoad);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
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
    }

    public void onLevelLoad(final WorldEvent.Load event) {
        if (event.getWorld() != null) {
            MinecraftServer server = event.getWorld().getServer();
            if (server != null) {
                server.overworld().getDataStorage().computeIfAbsent(AndroidsSD::load, AndroidsSD::create, AndroidsSD.ID);
            }
        }
    }
}
