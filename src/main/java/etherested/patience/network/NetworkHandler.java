package etherested.patience.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import etherested.patience.Patience;

//? if neoforge {
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
//?} else if (fabric && >=1.21) {
/*import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
*///?} else if (forge) {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
*///?} else if (fabric && <1.21) {
/*import net.minecraft.network.FriendlyByteBuf;
import io.netty.buffer.Unpooled;
*///?}

// registers network payloads for both loaders
public final class NetworkHandler {
    //? if >=1.21 {
    public static final ResourceLocation CONFIG_SYNC =
            ResourceLocation.fromNamespaceAndPath(Patience.MODID, "config_sync");

    public static final ResourceLocation CRAFTING_EXHAUSTION =
            ResourceLocation.fromNamespaceAndPath(Patience.MODID, "crafting_exhaustion");
    //?} else {
    /*public static final ResourceLocation CONFIG_SYNC =
            new ResourceLocation(Patience.MODID, "config_sync");

    public static final ResourceLocation CRAFTING_EXHAUSTION =
            new ResourceLocation(Patience.MODID, "crafting_exhaustion");
    *///?}

    //? if neoforge {
    public static void register(RegisterPayloadHandlersEvent event) {
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
    //?} else if (fabric && >=1.21) {
    /*public static void registerCommon() {
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
    *///?} else if (forge) {
    /*private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Patience.MODID, "main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    private static int messageId = 0;

    public static void register() {
        CHANNEL.registerMessage(messageId++, ConfigSyncPayload.class,
                (msg, buf) -> ConfigSyncPayload.encode(buf, msg), ConfigSyncPayload::decode, ConfigSyncPayload::handle);
        CHANNEL.registerMessage(messageId++, CraftingExhaustionPayload.class,
                CraftingExhaustionPayload::encode, CraftingExhaustionPayload::decode, CraftingExhaustionPayload::handle);
    }

    public static void sendToServer(Object payload) {
        CHANNEL.sendToServer(payload);
    }

    public static void sendToPlayer(ServerPlayer player, Object payload) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), payload);
    }
    *///?} else if (fabric && <1.21) {
    /*public static void registerCommon() {
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(
                CRAFTING_EXHAUSTION,
                (server, player, handler, buf, responseSender) -> {
                    float amount = buf.readFloat();
                    server.execute(() -> player.causeFoodExhaustion(amount));
                }
        );
    }

    public static void sendToServer(Object payload) {
        if (payload instanceof CraftingExhaustionPayload exhaustion) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeFloat(exhaustion.amount());
            net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(CRAFTING_EXHAUSTION, buf);
        }
    }

    public static void sendToPlayer(ServerPlayer player, Object payload) {
        if (payload instanceof ConfigSyncPayload config) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            ConfigSyncPayload.encode(buf, config);
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, CONFIG_SYNC, buf);
        }
    }
    *///?}

    private NetworkHandler() {}
}
