package etherested.patience.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import etherested.patience.api.CraftingContainer;
import etherested.patience.client.CraftingHandler;
import etherested.patience.config.ContainerSettings;

// intercepts slot clicks, renders crafting progress overlay, and handles craft completion;
// shared between both loaders
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu>
        extends Screen implements CraftingContainer {

    @Unique
    private static final Logger patience$LOGGER = LoggerFactory.getLogger("Patience/Mixin");

    @SuppressWarnings("unchecked")
    @Unique
    private final AbstractContainerScreen<T> patience$self = (AbstractContainerScreen<T>) (Object) this;

    @Shadow
    protected int leftPos;

    @Shadow
    protected int topPos;

    @Unique
    private boolean patience$completing = false;

    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    protected abstract void slotClicked(Slot slot, int slotId, int button, ClickType type);

    @Override
    public void patience$completeCraft(Slot slot, int slotId) {
        patience$completing = true;

        try {
            AbstractContainerMenu menu = patience$self.getMenu();
            ItemStack currentlyHolding = menu.getCarried();
            boolean wasHoldingItem = !currentlyHolding.isEmpty();
            int stashSlotId = -1;

            ContainerSettings settings = CraftingHandler.getInstance().getCurrentContainerSettings();
            int startSearchIndex = settings.getResultSlot();

            if (startSearchIndex < 0) {
                startSearchIndex = Math.max(0, menu.slots.size() - 36);
            }

            if (wasHoldingItem) {
                stashSlotId = patience$findEmptyPlayerSlot(menu, startSearchIndex);
                if (stashSlotId == -1) {
                    patience$completing = false;
                    return;
                }
                slotClicked(menu.getSlot(stashSlotId), stashSlotId, 0, ClickType.PICKUP);
            }

            slotClicked(slot, slotId, 0, ClickType.PICKUP);
            ItemStack resultOnCursor = menu.getCarried();

            if (!resultOnCursor.isEmpty()) {
                int destSlotId = patience$findSlotForStack(menu, resultOnCursor, startSearchIndex);
                if (destSlotId != -1) {
                    slotClicked(menu.getSlot(destSlotId), destSlotId, 0, ClickType.PICKUP);
                }
            }

            if (wasHoldingItem && stashSlotId != -1) {
                slotClicked(menu.getSlot(stashSlotId), stashSlotId, 0, ClickType.PICKUP);
            }

        } catch (Exception e) {
            patience$LOGGER.error("error distributing crafted item", e);
        }

        patience$completing = false;
    }

    @Unique
    private int patience$findEmptyPlayerSlot(AbstractContainerMenu menu, int startIndex) {
        int limit = menu.slots.size();
        for (int i = startIndex; i < limit; i++) {
            Slot s = menu.slots.get(i);
            if (!s.hasItem() && s.mayPlace(ItemStack.EMPTY)) {
                return i;
            }
        }
        return -1;
    }

    @Unique
    private int patience$findSlotForStack(AbstractContainerMenu menu, ItemStack stack, int startIndex) {
        int limit = menu.slots.size();

        for (int i = startIndex; i < limit; i++) {
            Slot s = menu.slots.get(i);
            if (s.hasItem() && ItemStack.isSameItemSameComponents(s.getItem(), stack)) {
                if (s.getItem().getCount() + stack.getCount() <= s.getMaxStackSize(stack)) {
                    return i;
                }
            }
        }

        return patience$findEmptyPlayerSlot(menu, startIndex);
    }

    @Inject(method = "slotClicked", at = @At("HEAD"), cancellable = true)
    private void patience$onSlotClicked(Slot slot, int slotId, int button, ClickType type, CallbackInfo ci) {
        if (patience$completing) {
            return;
        }

        try {
            boolean shift = Screen.hasShiftDown();
            if (CraftingHandler.getInstance().handleSlotClick(patience$self, slotId, shift)) {
                ci.cancel();
            }
        } catch (Exception e) {
            patience$LOGGER.error("error handling slot click", e);
            try {
                if (CraftingHandler.getInstance().shouldBlockSlotClick(patience$self, slotId)) {
                    ci.cancel();
                }
            } catch (Exception ex) {
                patience$LOGGER.error("error in fallback check", ex);
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void patience$onRender(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        try {
            CraftingHandler handler = CraftingHandler.getInstance();
            ContainerSettings settings = handler.getCurrentContainerSettings();

            if (!settings.isEnabled()) {
                return;
            }

            if (handler.isCrafting() || handler.getCurrentTime() > 0) {
                if (settings.isShowOverlay() && handler.getTotalTime() > 0) {
                    patience$renderOverlay(graphics, settings, handler.getCurrentTime() / handler.getTotalTime());
                }
            }
        } catch (Exception e) {
            patience$LOGGER.error("error rendering overlay", e);
        }
    }

    @Unique
    private void patience$renderOverlay(GuiGraphics graphics, ContainerSettings settings, float progress) {
        ResourceLocation texture = ResourceLocation.parse(settings.getOverlayTexture());
        int x = leftPos + settings.getOverlayX();
        int y = topPos + settings.getOverlayY();
        int width = settings.getOverlayWidth();
        int height = settings.getOverlayHeight();

        String direction = settings.getOverlayDirection();
        if (direction == null) direction = "right";

        float[] colors = CraftingHandler.getInstance().getOverlayColor();
        RenderSystem.setShaderColor(colors[0], colors[1], colors[2], colors[3]);

        switch (direction.toLowerCase()) {
            case "left" -> {
                int w = (int) (progress * width);
                int offset = width - w;
                graphics.blit(texture, x + offset, y, offset, 0, w, height, width, height);
            }
            case "up" -> {
                int h = (int) (progress * height);
                int offset = height - h;
                graphics.blit(texture, x, y + offset, 0, offset, width, h, width, height);
            }
            case "down" -> {
                int h = (int) (progress * height);
                graphics.blit(texture, x, y, 0, 0, width, h, width, height);
            }
            default -> {
                int w = (int) (progress * width);
                graphics.blit(texture, x, y, 0, 0, w, height, width, height);
            }
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Inject(method = "onClose", at = @At("TAIL"))
    private void patience$onClose(CallbackInfo ci) {
        CraftingHandler.getInstance().clearScreen();
    }
}
