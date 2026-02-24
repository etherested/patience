package etherested.patience.mixin;

//? if fabric {
/*import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import etherested.patience.registry.AttributeRegistry;

// Fabric-only mixin that adds the crafting_speed attribute to the player;
// on NeoForge/Forge this is handled by EntityAttributeModificationEvent in AttributeRegistry
@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void patience$addCraftingSpeed(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue().add(AttributeRegistry.craftingSpeed());
    }
}
*///?} else {
// NeoForge/Forge stub — PlayerMixin is Fabric-only
public class PlayerMixin {}
//?}
