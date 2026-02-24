package etherested.patience.event;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import etherested.patience.config.ConfigManager;
import etherested.patience.config.PatienceConfig;
import etherested.patience.network.ConfigSyncPayload;
import etherested.patience.platform.PlatformHelper;

//? if neoforge {
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import etherested.patience.Patience;
import etherested.patience.client.CraftingHandler;
//?} else if (forge) {
/*import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import etherested.patience.Patience;
import etherested.patience.client.CraftingHandler;
*///?} else {
/*import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
*///?}

//? if neoforge {
@EventBusSubscriber(modid = Patience.MODID)
//?} else if (forge) {
/*@Mod.EventBusSubscriber(modid = Patience.MODID)
*///?}
public final class EventHandler {

    //? if neoforge {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        handleServerStarted();
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            handlePlayerLogin(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) {
            CraftingHandler.getInstance().tick();
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        handleRegisterCommands(event.getDispatcher(), event.getBuildContext());
    }
    //?} else if (forge) {
    /*@SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        handleServerStarted();
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            handlePlayerLogin(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.player.level().isClientSide()) {
            CraftingHandler.getInstance().tick();
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        handleRegisterCommands(event.getDispatcher(), event.getBuildContext());
    }
    *///?} else {
    /*public static void registerFabricEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> handleServerStarted());

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            handlePlayerLogin(handler.getPlayer());
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            handleRegisterCommands(dispatcher, registryAccess);
        });
    }
    *///?}

    private static void handleServerStarted() {
        ConfigManager.load();
    }

    private static void handlePlayerLogin(ServerPlayer player) {
        PatienceConfig config = ConfigManager.getActiveConfig();
        if (config != null) {
            PlatformHelper.sendToPlayer(player, new ConfigSyncPayload(config));
        }
    }

    private static void handleRegisterCommands(
            com.mojang.brigadier.CommandDispatcher<net.minecraft.commands.CommandSourceStack> dispatcher,
            net.minecraft.commands.CommandBuildContext registryAccess) {
        dispatcher.register(
                Commands.literal("patience")
                        .then(Commands.literal("reload")
                                .requires(src -> src.hasPermission(2))
                                .executes(ctx -> {
                                    ConfigManager.load();
                                    PatienceConfig config = ConfigManager.getActiveConfig();
                                    if (config != null) {
                                        MinecraftServer server = ctx.getSource().getServer();
                                        PlatformHelper.sendToAllPlayers(server, new ConfigSyncPayload(config));
                                    }
                                    ctx.getSource().sendSuccess(
                                            //? if >=1.21 {
                                            () -> Component.translatable("command.patience.reload.success").withColor(0xFF68C1),
                                            //?} else {
                                            /*() -> Component.translatable("command.patience.reload.success")
                                                    .withStyle(style -> style.withColor(net.minecraft.network.chat.TextColor.fromRgb(0xFF68C1))),
                                            *///?}
                                            true
                                    );
                                    return 1;
                                })));
    }

    private EventHandler() {}
}
