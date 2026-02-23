package etherested.patience.network;

import net.minecraft.resources.ResourceLocation;
import etherested.patience.Patience;

//? if neoforge {
/*import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
*///?} else {
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
//?}

// registers network payloads for both loaders
public final class NetworkHandler {
    public static final ResourceLocation CONFIG_SYNC =
            ResourceLocation.fromNamespaceAndPath(Patience.MODID, "config_sync");

    public static final ResourceLocation CRAFTING_EXHAUSTION =
            ResourceLocation.fromNamespaceAndPath(Patience.MODID, "crafting_exhaustion");

    //? if neoforge {
    /*public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Patience.MODID).versioned("1.0.0");

        registrar.playToClient(
                ConfigSyncPayload.TYPE,
                ConfigSyncPayload.STREAM_CODEC,
                ConfigSyncPayload::handle
        );

        registrar.playToServer(
                CraftingExhaustionPayload.TYPE,
                CraftingExhaustionPayload.STREAM_CODEC,
                CraftingExhaustionPayload::handle
        );
    }
    *///?} else {
    public static void registerCommon() {
        PayloadTypeRegistry.playS2C().register(
                ConfigSyncPayload.TYPE,
                ConfigSyncPayload.STREAM_CODEC
        );
        PayloadTypeRegistry.playC2S().register(
                CraftingExhaustionPayload.TYPE,
                CraftingExhaustionPayload.STREAM_CODEC
        );

        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(
                CraftingExhaustionPayload.TYPE,
                (payload, context) -> {
                    context.player().server.execute(() -> {
                        context.player().causeFoodExhaustion(payload.amount());
                    });
                }
        );
    }
    //?}

    private NetworkHandler() {}
}
