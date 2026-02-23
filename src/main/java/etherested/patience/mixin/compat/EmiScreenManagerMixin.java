package etherested.patience.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// blocks EMI's craft interaction to prevent bypassing the crafting time mechanic
@Pseudo
@Mixin(targets = "dev.emi.emi.screen.EmiScreenManager", remap = false)
public class EmiScreenManagerMixin {

    @Inject(method = "craftInteraction", at = @At("HEAD"), cancellable = true, remap = false)
    private static void patience$blockCraftInteraction(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
