package etherested.patience.config;

import net.minecraft.client.gui.screens.Screen;

// optional Cloth Config screen factory;
// checks availability via reflection to avoid hard dependency on Cloth Config
public final class PatienceConfigScreen {

    private static Boolean available;

    public static boolean isAvailable() {
        if (available == null) {
            try {
                Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder");
                available = true;
            } catch (ClassNotFoundException e) {
                available = false;
            }
        }
        return available;
    }

    public static Screen create(Screen parent) {
        if (!isAvailable()) {
            return parent;
        }
        return PatienceClothConfigBuilder.build(parent);
    }

    private PatienceConfigScreen() {}
}
