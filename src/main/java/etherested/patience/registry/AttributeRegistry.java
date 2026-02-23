package etherested.patience.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import etherested.patience.Patience;

//? if neoforge {
/*import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
*///?}

//? if neoforge {
/*@EventBusSubscriber(modid = Patience.MODID, bus = EventBusSubscriber.Bus.MOD)
*///?}
public final class AttributeRegistry {

    //? if neoforge {
    /*public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Patience.MODID);

    public static final DeferredHolder<Attribute, Attribute> CRAFTING_SPEED = ATTRIBUTES.register("crafting_speed",
            () -> new RangedAttribute("attribute.name.patience.crafting_speed", 1.0D, 0.0D, 1024.0D).setSyncable(true));

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, CRAFTING_SPEED);
    }
    *///?} else {
    public static final Attribute CRAFTING_SPEED_ATTR = new RangedAttribute(
            "attribute.name.patience.crafting_speed", 1.0D, 0.0D, 1024.0D).setSyncable(true);

    private static final Holder<Attribute> CRAFTING_SPEED_HOLDER = Holder.direct(CRAFTING_SPEED_ATTR);

    public static void register() {
        Registry.register(BuiltInRegistries.ATTRIBUTE,
                ResourceLocation.fromNamespaceAndPath(Patience.MODID, "crafting_speed"),
                CRAFTING_SPEED_ATTR);
    }
    //?}

    public static Holder<Attribute> craftingSpeed() {
        //? if neoforge {
        /*return CRAFTING_SPEED;
        *///?} else {
        return CRAFTING_SPEED_HOLDER;
        //?}
    }

    private AttributeRegistry() {}
}
