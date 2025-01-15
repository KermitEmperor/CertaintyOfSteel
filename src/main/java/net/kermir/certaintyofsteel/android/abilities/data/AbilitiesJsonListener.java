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

public class AbilitiesJsonListener extends SimpleJsonResourceReloadListener {
    //Stuff loaded from Jsons
    //Ability id (ex.: certaintyofsteel:generic_ability)
    //Modified data
    public static HashMap<String, JsonElement> EXTRA_ABILITY_DATA;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final AbilitiesJsonListener instance = new AbilitiesJsonListener();

    public AbilitiesJsonListener() {
        super(GSON, "abilities");
        EXTRA_ABILITY_DATA = new HashMap<>();
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        EXTRA_ABILITY_DATA = new HashMap<>();
        //TODO make data non overridable completely except if priority (an int) is higher
        pObject.forEach((resourceLocation, jsonElement) -> {
            EXTRA_ABILITY_DATA.put(resourceLocation.toString(), jsonElement);
        });
        CertaintyOfSteel.LOGGER.info("Reloaded Android Ability JSONs");
    }
}
