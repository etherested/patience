package etherested.patience.config;

import com.google.gson.JsonObject;
import etherested.patience.util.SlotRange;

import java.util.Arrays;

// per-container configuration for crafting time behavior, overlay rendering, and sound overrides
public class ContainerSettings {

    private boolean enabled;
    private String screenClass;
    private String recipeType;
    private float timeMultiplier;
    private int outputSlot;
    private int resultSlot;
    private SlotRange ingredientSlots;
    private String ingredientMode;
    private Boolean showOverlay;
    private String overlayTexture;
    private String overlayDirection;
    private Integer overlayX;
    private Integer overlayY;
    private Integer overlayWidth;
    private Integer overlayHeight;
    private String craftingSound;
    private String finishSound;

    public ContainerSettings() {
        this.enabled = true;
        this.timeMultiplier = 1.0F;
        this.outputSlot = 0;
        this.resultSlot = -1;
        this.ingredientSlots = new SlotRange(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        this.ingredientMode = "slot";
        this.showOverlay = null;
        this.overlayTexture = null;
        this.overlayDirection = null;
        this.overlayX = null;
        this.overlayY = null;
        this.overlayWidth = null;
        this.overlayHeight = null;
        this.craftingSound = null;
        this.finishSound = null;
        this.recipeType = null;
    }

    public boolean isEnabled() { return enabled; }
    public String getScreenClass() { return screenClass; }
    public String getRecipeType() { return recipeType; }
    public float getTimeMultiplier() { return timeMultiplier; }
    public int getOutputSlot() { return outputSlot; }
    public int getResultSlot() { return resultSlot; }
    public SlotRange getIngredientSlots() { return ingredientSlots; }
    public String getIngredientMode() { return ingredientMode; }

    public boolean isShowOverlay() {
        return showOverlay != null ? showOverlay : true;
    }

    public String getOverlayTexture() {
        return overlayTexture != null ? overlayTexture : "patience:textures/generic.png";
    }

    public String getOverlayDirection() {
        return overlayDirection != null ? overlayDirection : "right";
    }

    public int getOverlayX() {
        return overlayX != null ? overlayX : 89;
    }

    public int getOverlayY() {
        return overlayY != null ? overlayY : 34;
    }

    public int getOverlayWidth() {
        return overlayWidth != null ? overlayWidth : 24;
    }

    public int getOverlayHeight() {
        return overlayHeight != null ? overlayHeight : 17;
    }

    public String getCraftingSound() {
        return craftingSound != null ? craftingSound : "";
    }

    public String getFinishSound() {
        return finishSound != null ? finishSound : "";
    }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setScreenClass(String screenClass) { this.screenClass = screenClass; }
    public void setRecipeType(String recipeType) { this.recipeType = recipeType; }
    public void setTimeMultiplier(float timeMultiplier) { this.timeMultiplier = timeMultiplier; }
    public void setOutputSlot(int outputSlot) { this.outputSlot = outputSlot; }
    public void setResultSlot(int resultSlot) { this.resultSlot = resultSlot; }
    public void setIngredientSlots(SlotRange ingredientSlots) { this.ingredientSlots = ingredientSlots; }
    public void setIngredientMode(String ingredientMode) { this.ingredientMode = ingredientMode; }
    public void setShowOverlay(Boolean showOverlay) { this.showOverlay = showOverlay; }
    public void setOverlayTexture(String overlayTexture) { this.overlayTexture = overlayTexture; }
    public void setOverlayDirection(String overlayDirection) { this.overlayDirection = overlayDirection; }
    public void setOverlayX(Integer overlayX) { this.overlayX = overlayX; }
    public void setOverlayY(Integer overlayY) { this.overlayY = overlayY; }
    public void setOverlayWidth(Integer overlayWidth) { this.overlayWidth = overlayWidth; }
    public void setOverlayHeight(Integer overlayHeight) { this.overlayHeight = overlayHeight; }
    public void setCraftingSound(String craftingSound) { this.craftingSound = craftingSound; }
    public void setFinishSound(String finishSound) { this.finishSound = finishSound; }

    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("enabled", enabled);
        if (screenClass != null) json.addProperty("screen_class", screenClass);
        if (recipeType != null) json.addProperty("recipe_type", recipeType);
        json.addProperty("time_multiplier", timeMultiplier);
        json.addProperty("output_slot", outputSlot);
        json.addProperty("result_slot", resultSlot);
        json.addProperty("ingredient_slots", ingredientSlots.toString());
        json.addProperty("ingredient_mode", ingredientMode);
        if (showOverlay != null) json.addProperty("show_overlay", showOverlay);
        if (overlayTexture != null) json.addProperty("overlay_texture", overlayTexture);
        if (overlayDirection != null) json.addProperty("overlay_direction", overlayDirection);
        if (overlayX != null) json.addProperty("overlay_x", overlayX);
        if (overlayY != null) json.addProperty("overlay_y", overlayY);
        if (overlayWidth != null) json.addProperty("overlay_width", overlayWidth);
        if (overlayHeight != null) json.addProperty("overlay_height", overlayHeight);
        if (craftingSound != null) json.addProperty("crafting_sound", craftingSound);
        if (finishSound != null) json.addProperty("finish_sound", finishSound);
        return json;
    }

    public static ContainerSettings deserialize(JsonObject json) {
        ContainerSettings s = new ContainerSettings();
        if (json.has("enabled")) s.enabled = json.get("enabled").getAsBoolean();
        if (json.has("screen_class")) s.screenClass = json.get("screen_class").getAsString();
        if (json.has("recipe_type")) s.recipeType = json.get("recipe_type").getAsString();
        if (json.has("time_multiplier")) s.timeMultiplier = json.get("time_multiplier").getAsFloat();
        if (json.has("output_slot")) s.outputSlot = json.get("output_slot").getAsInt();
        if (json.has("result_slot")) s.resultSlot = json.get("result_slot").getAsInt();
        if (json.has("ingredient_slots")) s.ingredientSlots = SlotRange.parse(json.get("ingredient_slots").getAsString());
        if (json.has("ingredient_mode")) s.ingredientMode = json.get("ingredient_mode").getAsString();
        if (json.has("show_overlay")) s.showOverlay = json.get("show_overlay").getAsBoolean();
        if (json.has("overlay_texture")) s.overlayTexture = json.get("overlay_texture").getAsString();
        if (json.has("overlay_direction")) s.overlayDirection = json.get("overlay_direction").getAsString();
        if (json.has("overlay_x")) s.overlayX = json.get("overlay_x").getAsInt();
        if (json.has("overlay_y")) s.overlayY = json.get("overlay_y").getAsInt();
        if (json.has("overlay_width")) s.overlayWidth = json.get("overlay_width").getAsInt();
        if (json.has("overlay_height")) s.overlayHeight = json.get("overlay_height").getAsInt();
        if (json.has("crafting_sound")) s.craftingSound = json.get("crafting_sound").getAsString();
        if (json.has("finish_sound")) s.finishSound = json.get("finish_sound").getAsString();
        return s;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ContainerSettings settings = new ContainerSettings();

        public Builder enabled(boolean enabled) { settings.setEnabled(enabled); return this; }
        public Builder screenClass(String screenClass) { settings.setScreenClass(screenClass); return this; }
        public Builder recipeType(String recipeType) { settings.setRecipeType(recipeType); return this; }
        public Builder timeMultiplier(float multiplier) { settings.setTimeMultiplier(multiplier); return this; }
        public Builder outputSlot(int slot) { settings.setOutputSlot(slot); return this; }
        public Builder resultSlot(int slot) { settings.setResultSlot(slot); return this; }
        public Builder ingredientSlots(SlotRange slots) { settings.setIngredientSlots(slots); return this; }
        public Builder ingredientMode(String mode) { settings.setIngredientMode(mode); return this; }
        public Builder showOverlay(boolean show) { settings.setShowOverlay(show); return this; }
        public Builder overlayTexture(String texture) { settings.setOverlayTexture(texture); return this; }
        public Builder overlayDirection(String direction) { settings.setOverlayDirection(direction); return this; }
        public Builder overlayX(int x) { settings.setOverlayX(x); return this; }
        public Builder overlayY(int y) { settings.setOverlayY(y); return this; }
        public Builder overlayWidth(int width) { settings.setOverlayWidth(width); return this; }
        public Builder overlayHeight(int height) { settings.setOverlayHeight(height); return this; }
        public Builder craftingSound(String sound) { settings.setCraftingSound(sound); return this; }
        public Builder finishSound(String sound) { settings.setFinishSound(sound); return this; }

        public ContainerSettings build() {
            return settings;
        }
    }
}
