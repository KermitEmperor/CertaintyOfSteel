package net.kermir.certaintyofsteel.android;

import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AbilityRegistry {
    public static final ResourceKey<Registry<Ability>> ABILITIES_KEY = ResourceKey.createRegistryKey(new ResourceLocation(CertaintyOfSteel.MOD_ID, "ability"));

    public static final DeferredRegister<Ability> ABILITIES =
            DeferredRegister.create(ABILITIES_KEY, CertaintyOfSteel.MOD_ID);


    public static final RegistryObject<Ability> GENERIC_ABILITY = ABILITIES.register("generic_ability",
            () -> new Ability());

    public static void register(IEventBus bus) {
        ABILITIES.register(bus);
    }

}
