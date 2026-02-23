package etherested.patience.client.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import etherested.patience.Patience;

//? if neoforge {
/*import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
*///?}

// registers mod sound events for both loaders
public final class SoundRegistry {

    //? if neoforge {
    /*public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, Patience.MODID);

    public static final Supplier<SoundEvent> CRAFTING = SOUNDS.register("crafting",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Patience.MODID, "crafting")));

    public static final Supplier<SoundEvent> FINISH = SOUNDS.register("finish",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Patience.MODID, "finish")));
    *///?} else {
    public static final SoundEvent CRAFTING = SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath(Patience.MODID, "crafting"));

    public static final SoundEvent FINISH = SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath(Patience.MODID, "finish"));

    public static void register() {
        Registry.register(BuiltInRegistries.SOUND_EVENT,
                ResourceLocation.fromNamespaceAndPath(Patience.MODID, "crafting"), CRAFTING);
        Registry.register(BuiltInRegistries.SOUND_EVENT,
                ResourceLocation.fromNamespaceAndPath(Patience.MODID, "finish"), FINISH);
    }
    //?}

    public static SoundEvent crafting() {
        //? if neoforge {
        /*return CRAFTING.get();
        *///?} else {
        return CRAFTING;
        //?}
    }

    public static SoundEvent finish() {
        //? if neoforge {
        /*return FINISH.get();
        *///?} else {
        return FINISH;
        //?}
    }

    private SoundRegistry() {}
}
