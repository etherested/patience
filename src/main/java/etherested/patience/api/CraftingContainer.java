package etherested.patience.api;

import net.minecraft.world.inventory.Slot;

// interface injected into AbstractContainerScreen via mixin;
// provides a method to complete a craft by slot-clicking the output into the player inventory
public interface CraftingContainer {
    void patience$completeCraft(Slot slot, int slotId);
}
