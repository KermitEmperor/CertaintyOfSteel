package net.kermir.certaintyofsteel.registry;

import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class AbilityRegistry {
    /*Registry Hell :(*/
    public static IForgeRegistry<Ability> ABILITIES_REGISTRY;

    public static void registerRegistry(NewRegistryEvent event) {
        event.create(makeRegistry(new ResourceLocation(CertaintyOfSteel.MOD_ID, "ability"), Ability.class, Integer.MAX_VALUE >> 5), registry -> ABILITIES_REGISTRY = registry);
    }

    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation name, Class<T> type, int max) {
        return new RegistryBuilder<T>().setName(name).setType(type).setMaxID(max);
    }

    public static final ResourceKey<Registry<Ability>> ABILITIES_KEY = ResourceKey.createRegistryKey(new ResourceLocation(CertaintyOfSteel.MOD_ID, "ability"));

    /*Normal Registry stuff :)*/
    public static final DeferredRegister<Ability> ABILITIES =
            DeferredRegister.create(ABILITIES_KEY, CertaintyOfSteel.MOD_ID);


    public static final RegistryObject<Ability> GENERIC_ABILITY = ABILITIES.register("generic_ability",
            () -> new Ability());

    public static final RegistryObject<Ability> SECOND_GENERIC_ABILITY = ABILITIES.register("generic_ability_two",
            () -> new Ability());

    public static void register(IEventBus bus) {
        ABILITIES.register(bus);
    }
}
