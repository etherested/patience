package etherested.patience.client;

//? if fabric {
/*import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import etherested.patience.config.ConfigManager;
import etherested.patience.network.ConfigSyncPayload;
//? if <1.21 {
/^import etherested.patience.network.NetworkHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import etherested.patience.config.PatienceConfig;
^///?}

// Fabric client entrypoint — registers client-side tick events and network receivers
public class PatienceFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CraftingHandler.initialize();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            CraftingHandler.getInstance().tick();
        });

        //? if >=1.21 {
        ClientPlayNetworking.registerGlobalReceiver(
                ConfigSyncPayload.TYPE,
                (payload, context) -> {
                    context.client().execute(() -> {
                        if (payload.config() != null) {
                            ConfigManager.setActiveConfig(payload.config());
                        }
                    });
                }
        );
        //?} else {
        /^ClientPlayNetworking.registerGlobalReceiver(
                NetworkHandler.CONFIG_SYNC,
                (client, handler, buf, responseSender) -> {
                    ConfigSyncPayload payload = ConfigSyncPayload.decode(buf);
                    client.execute(() -> {
                        if (payload.config() != null) {
                            ConfigManager.setActiveConfig(payload.config());
                        }
                    });
                }
        );
        ^///?}
    }
}
*///?} else {
// NeoForge/Forge stub — Fabric client entrypoint is Fabric-only
public class PatienceFabricClient {}
//?}
