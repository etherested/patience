package etherested.patience.client;

//? if fabric {
/*import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import etherested.patience.config.PatienceConfigScreen;

// Fabric ModMenu integration — provides a config screen factory when Cloth Config is available
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (PatienceConfigScreen.isAvailable()) {
            return PatienceConfigScreen::create;
        }
        return parent -> null;
    }
}
*///?} else {
// NeoForge/Forge stub — ModMenu integration is Fabric-only
public class ModMenuIntegration {}
//?}
