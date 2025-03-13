package net.kermir.certaintyofsteel.android.abilities.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AbilitySettingsJL extends SimpleJsonResourceReloadListener {
    public static HashMap<String, JsonElement> ABILITY_SETTINGS_DATA;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final AbilitySettingsJL instance = new AbilitySettingsJL();

    public AbilitySettingsJL() {
        super(GSON, "android/ability_settings");
        ABILITY_SETTINGS_DATA = new HashMap<>();
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        ABILITY_SETTINGS_DATA = new HashMap<>();

        pObject.forEach((resourceLocation, jsonElement) -> {
            ABILITY_SETTINGS_DATA.put(resourceLocation.toString(), jsonElement);
            CertaintyOfSteel.LOGGER.info(jsonElement.getAsJsonObject().toString());
        });
        CertaintyOfSteel.LOGGER.info("Reloaded Android Ability Settings JSONs");



        /*
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            if (AndroidsSD.getInstance().getAndroids() == null) return;
            for (UUID playerUUID : AndroidsSD.getInstance().getAndroids().keySet()) {
                ServerPlayer serverPlayer = server.getPlayerList().getPlayer(playerUUID);
                PacketChannel.sendToClient(new UpdateClientAndroidInstance(playerUUID, AndroidsSD.getInstance().getAndroid(playerUUID)), serverPlayer);
            }
        }*/

    }
}
