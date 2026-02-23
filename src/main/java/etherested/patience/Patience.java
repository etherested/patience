package etherested.patience;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import etherested.patience.config.PatienceConfigScreen;

//? if neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import etherested.patience.client.CraftingHandler;
import etherested.patience.client.sound.SoundRegistry;
import etherested.patience.network.NetworkHandler;
import etherested.patience.registry.AttributeRegistry;
*///?} else {
import etherested.patience.client.sound.SoundRegistry;
import etherested.patience.event.EventHandler;
import etherested.patience.network.NetworkHandler;
import etherested.patience.registry.AttributeRegistry;
//?}

//? if neoforge {
/*@Mod(Patience.MODID)
*///?}
public class Patience
    //? if fabric {
    implements net.fabricmc.api.ModInitializer
    //?}
{
    public static final String MODID = "patience";
    private static final Logger LOGGER = LoggerFactory.getLogger(Patience.class);

    //? if neoforge {
    /*public Patience(IEventBus modEventBus, ModContainer modContainer) {
        SoundRegistry.SOUNDS.register(modEventBus);
        AttributeRegistry.ATTRIBUTES.register(modEventBus);
        modEventBus.addListener(NetworkHandler::register);

        CraftingHandler.initialize();

        if (PatienceConfigScreen.isAvailable()) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class,
                    (container, screen) -> PatienceConfigScreen.create(screen));
        }

        LOGGER.info("patience mod initialized");
    }
    *///?} else {
    @Override
    public void onInitialize() {
        SoundRegistry.register();
        AttributeRegistry.register();
        NetworkHandler.registerCommon();
        EventHandler.registerFabricEvents();

        LOGGER.info("patience mod initialized");
    }
    //?}
}
