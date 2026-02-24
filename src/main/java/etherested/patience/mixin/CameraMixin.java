package etherested.patience.mixin;

//? if fabric {
/*import net.minecraft.client.Camera;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import etherested.patience.client.CraftingHandler;

// Fabric-only mixin that applies camera shake during crafting;
// on NeoForge/Forge this is handled by ViewportEvent.ComputeCameraAngles in CameraHandler
@Mixin(Camera.class)
public abstract class CameraMixin {
    @Unique
    private static final RandomSource patience$RANDOM = RandomSource.create();

    @Shadow
    protected abstract void setRotation(float yRot, float xRot);

    @Shadow
    private float xRot;

    @Shadow
    private float yRot;

    @Inject(method = "setup", at = @At("TAIL"))
    private void patience$onSetup(BlockGetter level, Entity entity, boolean detached, boolean mirrored, float partialTick, CallbackInfo ci) {
        float shake = CraftingHandler.getInstance().getCurrentShake();
        if (shake > 0) {
            float pitchOffset = (patience$RANDOM.nextFloat() - 0.5F) * shake;
            float yawOffset = (patience$RANDOM.nextFloat() - 0.5F) * shake;
            setRotation(yRot + yawOffset, xRot + pitchOffset);
        }
    }
}
*///?} else {
// NeoForge/Forge stub — CameraMixin is Fabric-only
public class CameraMixin {}
//?}
