package etherested.patience.platform;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

// loader-agnostic utility methods for cross-cutting platform operations
public final class PlatformHelper {

    private PlatformHelper() {}

    public static boolean isModLoaded(String modId) {
        //? if neoforge {
        /*return net.neoforged.fml.ModList.get().isLoaded(modId);
        *///?} else {
        return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded(modId);
        //?}
    }

    public static boolean isClient() {
        //? if neoforge {
        /*return net.neoforged.fml.loading.FMLEnvironment.dist == net.neoforged.api.distmarker.Dist.CLIENT;
        *///?} else {
        return net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT;
        //?}
    }

    public static Path getConfigDir() {
        //? if neoforge {
        /*return net.neoforged.fml.loading.FMLPaths.CONFIGDIR.get();
        *///?} else {
        return net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir();
        //?}
    }

    public static void sendToServer(CustomPacketPayload payload) {
        //? if neoforge {
        /*net.neoforged.neoforge.network.PacketDistributor.sendToServer(payload);
        *///?} else {
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(payload);
        //?}
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        //? if neoforge {
        /*net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, payload);
        *///?} else {
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, payload);
        //?}
    }

    public static void sendToAllPlayers(net.minecraft.server.MinecraftServer server, CustomPacketPayload payload) {
        //? if neoforge {
        /*net.neoforged.neoforge.network.PacketDistributor.sendToAllPlayers(payload);
        *///?} else {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, payload);
        }
        //?}
    }
}
