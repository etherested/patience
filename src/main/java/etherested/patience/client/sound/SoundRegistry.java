package etherested.patience.client.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import etherested.patience.Patience;

//? if neoforge {
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
//?} else if (forge) {
/*import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
*///?}

// registers mod sound events for both loaders
public final class SoundRegistry {

    //? if neoforge {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, Patience.MODID);

    public static final Supplier<SoundEvent> CRAFTING = SOUNDS.register("crafting",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Patience.MODID, "crafting")));

    public static final Supplier<SoundEvent> FINISH = SOUNDS.register("finish",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Patience.MODID, "finish")));
    //?} else if (forge) {
    /*public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Patience.MODID);

    public static final RegistryObject<SoundEvent> CRAFTING = SOUNDS.register("crafting",
            () -> SoundEvent.createVariableRangeEvent(
                    new ResourceLocation(Patience.MODID, "crafting")));

    public static final RegistryObject<SoundEvent> FINISH = SOUNDS.register("finish",
            () -> SoundEvent.createVariableRangeEvent(
                    new ResourceLocation(Patience.MODID, "finish")));
    *///?} else if (fabric && >=1.21) {
    /*public static final SoundEvent CRAFTING = SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath(Patience.MODID, "crafting"));

    public static final SoundEvent FINISH = SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath(Patience.MODID, "finish"));

    public static void register() {
        Registry.register(BuiltInRegistries.SOUND_EVENT,
                ResourceLocation.fromNamespaceAndPath(Patience.MODID, "crafting"), CRAFTING);
        Registry.register(BuiltInRegistries.SOUND_EVENT,
                ResourceLocation.fromNamespaceAndPath(Patience.MODID, "finish"), FINISH);
    }
    *///?} else {
    /*public static final SoundEvent CRAFTING = SoundEvent.createVariableRangeEvent(
            new ResourceLocation(Patience.MODID, "crafting"));

    public static final SoundEvent FINISH = SoundEvent.createVariableRangeEvent(
            new ResourceLocation(Patience.MODID, "finish"));

    public static void register() {
        Registry.register(BuiltInRegistries.SOUND_EVENT,
                new ResourceLocation(Patience.MODID, "crafting"), CRAFTING);
        Registry.register(BuiltInRegistries.SOUND_EVENT,
                new ResourceLocation(Patience.MODID, "finish"), FINISH);
    }
    *///?}

    public static SoundEvent crafting() {
        //? if neoforge || forge {
        return CRAFTING.get();
        //?} else {
        /*return CRAFTING;
        *///?}
    }

    public static SoundEvent finish() {
        //? if neoforge || forge {
        return FINISH.get();
        //?} else {
        /*return FINISH;
        *///?}
    }

    private SoundRegistry() {}
}
