package etherested.patience.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import etherested.patience.config.ConfigManager;
import etherested.patience.config.PatienceConfig;

//? if >=1.21 {
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
//?}

//? if neoforge {
import net.neoforged.neoforge.network.handling.IPayloadContext;
//?} else if (forge) {
/*import java.util.function.Supplier;
import net.minecraftforge.network.NetworkEvent;
*///?}

// server-to-client config sync payload;
// serializes PatienceConfig as JSON string over the network
//? if >=1.21 {
public record ConfigSyncPayload(PatienceConfig config) implements CustomPacketPayload {
//?} else {
/*public class ConfigSyncPayload {
    private final PatienceConfig config;

    public ConfigSyncPayload(PatienceConfig config) {
        this.config = config;
    }

    public PatienceConfig config() {
        return config;
    }
*///?}
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigSyncPayload.class);
    private static final Gson GSON = new Gson();

    //? if >=1.21 {
    public static final Type<ConfigSyncPayload> TYPE = new Type<>(NetworkHandler.CONFIG_SYNC);

    public static final StreamCodec<FriendlyByteBuf, ConfigSyncPayload> STREAM_CODEC = StreamCodec.of(
            ConfigSyncPayload::encode,
            ConfigSyncPayload::decode
    );
    //?}

    //? if >=1.21 {
    private static void encode(FriendlyByteBuf buf, ConfigSyncPayload payload) {
    //?} else {
    /*public static void encode(FriendlyByteBuf buf, ConfigSyncPayload payload) {
    *///?}
        try {
            buf.writeUtf(GSON.toJson(payload.config.serialize()));
        } catch (Exception e) {
            LOGGER.error("failed to encode config", e);
            buf.writeUtf("{}");
        }
    }

    //? if >=1.21 {
    private static ConfigSyncPayload decode(FriendlyByteBuf buf) {
    //?} else {
    /*public static ConfigSyncPayload decode(FriendlyByteBuf buf) {
    *///?}
        try {
            JsonObject json = JsonParser.parseString(buf.readUtf()).getAsJsonObject();
            return new ConfigSyncPayload(PatienceConfig.deserialize(json));
        } catch (Exception e) {
            LOGGER.error("failed to decode config", e);
            return new ConfigSyncPayload(null);
        }
    }

    //? if neoforge {
    public static void handle(ConfigSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (payload.config != null) {
                ConfigManager.setActiveConfig(payload.config);
            }
        });
    }
    //?} else if (forge) {
    /*public static void handle(ConfigSyncPayload payload, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            if (payload.config() != null) {
                ConfigManager.setActiveConfig(payload.config());
            }
        });
        ctx.setPacketHandled(true);
    }
    *///?}

    //? if >=1.21 {
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    //?}
}
