package etherested.patience.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

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

// client-to-server payload requesting hunger exhaustion after completing a craft
//? if >=1.21 {
public record CraftingExhaustionPayload(float amount) implements CustomPacketPayload {
//?} else {
/*public class CraftingExhaustionPayload {
    private final float amount;

    public CraftingExhaustionPayload(float amount) {
        this.amount = amount;
    }

    public float amount() {
        return amount;
    }
*///?}

    //? if >=1.21 {
    public static final Type<CraftingExhaustionPayload> TYPE = new Type<>(NetworkHandler.CRAFTING_EXHAUSTION);

    public static final StreamCodec<FriendlyByteBuf, CraftingExhaustionPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> buf.writeFloat(payload.amount),
            buf -> new CraftingExhaustionPayload(buf.readFloat())
    );
    //?}

    //? if <1.21 {
    /*public static void encode(CraftingExhaustionPayload payload, FriendlyByteBuf buf) {
        buf.writeFloat(payload.amount);
    }

    public static CraftingExhaustionPayload decode(FriendlyByteBuf buf) {
        return new CraftingExhaustionPayload(buf.readFloat());
    }
    *///?}

    //? if neoforge {
    public static void handle(CraftingExhaustionPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.causeFoodExhaustion(payload.amount);
            }
        });
    }
    //?} else if (forge) {
    /*public static void handle(CraftingExhaustionPayload payload, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                player.causeFoodExhaustion(payload.amount());
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
