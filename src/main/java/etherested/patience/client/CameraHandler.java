package etherested.patience.client;

//? if neoforge {
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import etherested.patience.Patience;

// NeoForge camera shake handler;
// on Fabric this is handled by CameraMixin
@EventBusSubscriber(modid = Patience.MODID, value = Dist.CLIENT)
public final class CameraHandler {
    private static final RandomSource RANDOM = RandomSource.create();

    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        float shake = CraftingHandler.getInstance().getCurrentShake();
        if (shake > 0) {
            float pitchOffset = (RANDOM.nextFloat() - 0.5F) * shake;
            float yawOffset = (RANDOM.nextFloat() - 0.5F) * shake;

            event.setPitch(event.getPitch() + pitchOffset);
            event.setYaw(event.getYaw() + yawOffset);
        }
    }

    private CameraHandler() {}
}
//?} else if (forge) {
/*import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import etherested.patience.Patience;

// Forge camera shake handler;
// on Fabric this is handled by CameraMixin
@Mod.EventBusSubscriber(modid = Patience.MODID, value = Dist.CLIENT)
public final class CameraHandler {
    private static final RandomSource RANDOM = RandomSource.create();

    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        float shake = CraftingHandler.getInstance().getCurrentShake();
        if (shake > 0) {
            float pitchOffset = (RANDOM.nextFloat() - 0.5F) * shake;
            float yawOffset = (RANDOM.nextFloat() - 0.5F) * shake;

            event.setPitch(event.getPitch() + pitchOffset);
            event.setYaw(event.getYaw() + yawOffset);
        }
    }

    private CameraHandler() {}
}
*///?} else {
/*// Fabric stub — camera shake is handled by CameraMixin on Fabric
public final class CameraHandler {
    private CameraHandler() {}
}
*///?}
