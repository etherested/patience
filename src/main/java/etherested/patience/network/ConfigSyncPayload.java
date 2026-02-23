package etherested.patience.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import etherested.patience.config.ConfigManager;
import etherested.patience.config.PatienceConfig;

//? if neoforge {
/*import net.neoforged.neoforge.network.handling.IPayloadContext;
*///?}

// server-to-client config sync payload;
// serializes PatienceConfig as JSON string over the network
public record ConfigSyncPayload(PatienceConfig config) implements CustomPacketPayload {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigSyncPayload.class);
    private static final Gson GSON = new Gson();

    public static final Type<ConfigSyncPayload> TYPE = new Type<>(NetworkHandler.CONFIG_SYNC);

    public static final StreamCodec<FriendlyByteBuf, ConfigSyncPayload> STREAM_CODEC = StreamCodec.of(
            ConfigSyncPayload::encode,
            ConfigSyncPayload::decode
    );

    private static void encode(FriendlyByteBuf buf, ConfigSyncPayload payload) {
        try {
            buf.writeUtf(GSON.toJson(payload.config.serialize()));
        } catch (Exception e) {
            LOGGER.error("failed to encode config", e);
            buf.writeUtf("{}");
        }
    }

    private static ConfigSyncPayload decode(FriendlyByteBuf buf) {
        try {
            JsonObject json = JsonParser.parseString(buf.readUtf()).getAsJsonObject();
            return new ConfigSyncPayload(PatienceConfig.deserialize(json));
        } catch (Exception e) {
            LOGGER.error("failed to decode config", e);
            return new ConfigSyncPayload(null);
        }
    }

    //? if neoforge {
    /*public static void handle(ConfigSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (payload.config != null) {
                ConfigManager.setActiveConfig(payload.config);
            }
        });
    }
    *///?}

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
