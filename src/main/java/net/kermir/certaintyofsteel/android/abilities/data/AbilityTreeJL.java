package net.kermir.certaintyofsteel.android.abilities.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.networking.PacketChannel;
import net.kermir.certaintyofsteel.networking.packets.UpdateClientAndroidInstance;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.kermir.certaintyofsteel.util.json.JsonOptionalObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;

//TODO Merge AbilitySettingsJL with this one
public class AbilityTreeJL extends SimpleJsonResourceReloadListener {
    //Stuff loaded from Jsons
    //Ability id (ex.: certaintyofsteel:generic_ability)
    //Modified data
    public static HashMap<String, JsonElement> ABILITY_TREE_DATA;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final AbilityTreeJL instance = new AbilityTreeJL();

    public AbilityTreeJL() {
        super(GSON, "android/ability_tree");
        ABILITY_TREE_DATA = new HashMap<>();
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        ABILITY_TREE_DATA = new HashMap<>();
        //TODO make data non overridable completely except if priority (an int) is higher
        //TODO syncronise with client

        pObject.forEach((resourceLocation, jsonElement) -> {
            ABILITY_TREE_DATA.put(resourceLocation.toString(), jsonElement);
        });
        CertaintyOfSteel.LOGGER.info("Reloaded Android Ability Tree JSONs");

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            if (AndroidsSD.getInstance().getAndroids() == null) return;
            for (UUID playerUUID : AndroidsSD.getInstance().getAndroids().keySet()) {
                ServerPlayer serverPlayer = server.getPlayerList().getPlayer(playerUUID);
                PacketChannel.sendToClient(new UpdateClientAndroidInstance(playerUUID, AndroidsSD.getInstance().getAndroid(playerUUID)), serverPlayer);
            }

        }
    }
}
