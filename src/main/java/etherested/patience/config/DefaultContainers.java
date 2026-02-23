package etherested.patience.config;

import etherested.patience.util.SlotRange;

import java.util.ArrayList;
import java.util.List;

// provides default ContainerSettings for all supported vanilla and modded workstations
public final class DefaultContainers {
    private static final List<ContainerSettings> CONTAINERS = new ArrayList<>();

    static {
        register(ContainerSettings.builder()
                .screenClass("net.minecraft.client.gui.screens.inventory.InventoryScreen")
                .ingredientSlots(SlotRange.parse("1-4"))
                .resultSlot(9)
                .overlayTexture("patience:textures/inventory.png")
                .overlayX(134)
                .overlayY(28)
                .overlayWidth(18)
                .overlayHeight(15)
                .build());

        register(ContainerSettings.builder()
                .screenClass("top.theillusivec4.curios.client.gui.CuriosScreen")
                .ingredientSlots(SlotRange.parse("1-4"))
                .resultSlot(9)
                .overlayTexture("patience:textures/inventory.png")
                .overlayX(134)
                .overlayY(28)
                .overlayWidth(18)
                .overlayHeight(15)
                .build());

        register(ContainerSettings.builder()
                .screenClass("net.minecraft.client.gui.screens.inventory.CraftingScreen")
                .resultSlot(10)
                .build());

        register(ContainerSettings.builder()
                .screenClass("net.minecraft.client.gui.screens.inventory.SmithingScreen")
                .ingredientSlots(SlotRange.parse("0-2"))
                .outputSlot(3)
                .resultSlot(4)
                .overlayX(67)
                .overlayY(48)
                .craftingSound("patience:smithing_table")
                .build());

        register(ContainerSettings.builder()
                .screenClass("net.minecraft.client.gui.screens.inventory.AnvilScreen")
                .ingredientSlots(SlotRange.parse("0-1"))
                .outputSlot(2)
                .resultSlot(3)
                .overlayX(101)
                .overlayY(47)
                .craftingSound("patience:anvil")
                .build());

        register(ContainerSettings.builder()
                .screenClass("net.minecraft.client.gui.screens.inventory.GrindstoneScreen")
                .ingredientSlots(SlotRange.parse("0-1"))
                .outputSlot(2)
                .resultSlot(3)
                .overlayX(94)
                .overlayY(33)
                .craftingSound("patience:grindstone")
                .build());

        register(ContainerSettings.builder()
                .screenClass("net.minecraft.client.gui.screens.inventory.StonecutterScreen")
                .ingredientSlots(SlotRange.parse("0"))
                .outputSlot(1)
                .resultSlot(2)
                .showOverlay(false)
                .craftingSound("patience:stonecutter")
                .build());

        register(ContainerSettings.builder()
                .screenClass("net.minecraft.client.gui.screens.inventory.CartographyTableScreen")
                .ingredientSlots(SlotRange.parse("0-1"))
                .outputSlot(2)
                .resultSlot(3)
                .showOverlay(false)
                .craftingSound("patience:cartography_table")
                .build());

        register(ContainerSettings.builder()
                .screenClass("net.minecraft.client.gui.screens.inventory.LoomScreen")
                .ingredientSlots(SlotRange.parse("0-2"))
                .outputSlot(3)
                .resultSlot(4)
                .showOverlay(false)
                .craftingSound("patience:loom")
                .build());

        register(ContainerSettings.builder()
                .screenClass("net.mehvahdjukaar.sawmill.SawmillScreen")
                .ingredientSlots(SlotRange.parse("0"))
                .outputSlot(1)
                .resultSlot(2)
                .showOverlay(false)
                .craftingSound("patience:sawmill")
                .build());

        register(ContainerSettings.builder()
                .screenClass("com.teamabnormals.woodworks.client.gui.screens.inventory.SawmillScreen")
                .ingredientSlots(SlotRange.parse("0"))
                .outputSlot(1)
                .resultSlot(2)
                .showOverlay(false)
                .craftingSound("patience:sawmill")
                .build());

        register(ContainerSettings.builder()
                .screenClass("com.dolthhaven.easeldoesit.common.inventory.EaselScreen")
                .ingredientSlots(SlotRange.parse("0"))
                .outputSlot(1)
                .resultSlot(2)
                .showOverlay(false)
                .craftingSound("patience:easel")
                .build());

        register(ContainerSettings.builder()
                .screenClass("net.orcinus.galosphere.client.gui.CombustionTableScreen")
                .ingredientSlots(SlotRange.parse("0-3"))
                .outputSlot(4)
                .resultSlot(5)
                .showOverlay(false)
                .craftingSound("patience:combustion_table")
                .build());

        register(ContainerSettings.builder()
                .screenClass("com.teamabnormals.clayworks.client.gui.screens.inventory.PotteryScreen")
                .ingredientSlots(SlotRange.parse("0-2"))
                .outputSlot(3)
                .resultSlot(4)
                .showOverlay(false)
                .craftingSound("patience:pottery_table")
                .build());

        register(ContainerSettings.builder()
                .screenClass("com.momosoftworks.coldsweat.client.gui.SewingScreen")
                .ingredientSlots(SlotRange.parse("0-1"))
                .outputSlot(2)
                .resultSlot(3)
                .showOverlay(false)
                .craftingSound("patience:sewing_table")
                .build());

        register(ContainerSettings.builder()
                .screenClass("net.joefoxe.hexerei.screen.WoodcutterScreen")
                .ingredientSlots(SlotRange.parse("0"))
                .outputSlot(1)
                .resultSlot(2)
                .ingredientMode("custom")
                .showOverlay(false)
                .craftingSound("patience:sawmill")
                .build());

        register(ContainerSettings.builder()
                .screenClass("de.rubixdev.inventorio.client.ui.InventorioScreen")
                .ingredientSlots(SlotRange.parse("1-4"))
                .resultSlot(9)
                .overlayTexture("patience:textures/inventory.png")
                .overlayX(154)
                .overlayY(28)
                .overlayWidth(18)
                .overlayHeight(15)
                .build());
    }

    public static void register(ContainerSettings settings) {
        CONTAINERS.add(settings);
    }

    public static List<ContainerSettings> getAll() {
        return new ArrayList<>(CONTAINERS);
    }

    private DefaultContainers() {}
}
