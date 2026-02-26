package etherested.patience.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// monolithic config with flat fields and manual JSON serialization;
// singleton instance — use get() for read-only access, getInstance() for mutable access (Cloth Config)
public class PatienceConfig {

    private static PatienceConfig INSTANCE;

    // general
    private boolean debug = false;
    private boolean enableSounds = true;
    private String defaultCraftingSound = "patience:crafting";
    private String defaultFinishSound = "patience:finish";
    private String defaultPenaltySound = "patience:penalty";
    private String defaultSuccessSound = "patience:success";
    private float globalTimeMultiplier = 1.0F;

    // experience
    private float experienceMultiplier = 1.0F;
    private float experienceBaseSpeed = 1.0F;
    private float experienceSpeedPerLevel = 0.02F;
    private int experienceMaxLevelCap = 200;

    // decay
    private boolean decayEnabled = true;
    private float decayRate = 2.0F;

    // screen shake
    private boolean screenShakeEnabled = false;
    private float screenShakeIntensity = 0.5F;

    // hunger
    private float hungerExhaustionCost = 0.1F;
    private boolean hungerPenaltyEnabled = true;
    private int hungerThreshold = 6;
    private float hungerPenaltyMultiplier = 0.5F;

    // minigame
    private boolean minigameEnabled = true;
    private float minigameChance = 0.5F;
    private float minigameWindowWidth = 0.15F;
    private float minigamePenaltyPercent = 0.25F;
    private boolean minigamePenaltyCancelsCraft = true;

    // item sounds
    private Map<String, String> itemSounds = new HashMap<>();

    // ingredient multipliers
    private Map<String, Float> ingredientMultipliersByMod = new HashMap<>();
    private Map<String, Float> ingredientMultipliersByItem = new HashMap<>();
    private Map<String, Float> ingredientMultipliersByTag = new HashMap<>();

    // output multipliers
    private Map<String, Float> outputMultipliersByMod = new HashMap<>();
    private Map<String, Float> outputMultipliersByItem = new HashMap<>();
    private Map<String, Float> outputMultipliersByTag = new HashMap<>();

    // recipe multipliers
    private Map<String, Float> recipeMultipliersByType = new HashMap<>();
    private Map<String, Float> recipeMultipliersByRecipe = new HashMap<>();

    // containers
    private List<ContainerSettings> containers = new ArrayList<>();

    private PatienceConfig() {}

    public static PatienceConfig get() {
        if (INSTANCE == null) {
            INSTANCE = new PatienceConfig();
        }
        return INSTANCE;
    }

    public static PatienceConfig getInstance() {
        return get();
    }

    public static void setInstance(PatienceConfig config) {
        INSTANCE = config;
    }

    // general getters
    public boolean isDebug() { return debug; }
    public boolean isSoundsEnabled() { return enableSounds; }
    public String getDefaultCraftingSound() { return defaultCraftingSound; }
    public String getDefaultFinishSound() { return defaultFinishSound; }
    public String getDefaultPenaltySound() { return defaultPenaltySound; }
    public String getDefaultSuccessSound() { return defaultSuccessSound; }
    public float getGlobalTimeMultiplier() { return globalTimeMultiplier; }

    // experience getters
    public float getExperienceMultiplier() { return experienceMultiplier; }
    public float getExperienceBaseSpeed() { return experienceBaseSpeed; }
    public float getExperienceSpeedPerLevel() { return experienceSpeedPerLevel; }
    public int getExperienceMaxLevelCap() { return experienceMaxLevelCap; }

    // decay getters
    public boolean isDecayEnabled() { return decayEnabled; }
    public float getDecayRate() { return decayRate; }

    // screen shake getters
    public boolean isScreenShakeEnabled() { return screenShakeEnabled; }
    public float getScreenShakeIntensity() { return screenShakeIntensity; }

    // hunger getters
    public float getHungerExhaustionCost() { return hungerExhaustionCost; }
    public boolean isHungerPenaltyEnabled() { return hungerPenaltyEnabled; }
    public int getHungerThreshold() { return hungerThreshold; }
    public float getHungerPenaltyMultiplier() { return hungerPenaltyMultiplier; }

    // minigame getters
    public boolean isMinigameEnabled() { return minigameEnabled; }
    public float getMinigameChance() { return minigameChance; }
    public float getMinigameWindowWidth() { return minigameWindowWidth; }
    public float getMinigamePenaltyPercent() { return minigamePenaltyPercent; }
    public boolean isMinigamePenaltyCancelsCraft() { return minigamePenaltyCancelsCraft; }

    // map getters
    public Map<String, String> getItemSounds() { return itemSounds; }
    public Map<String, Float> getIngredientMultipliersByMod() { return ingredientMultipliersByMod; }
    public Map<String, Float> getIngredientMultipliersByItem() { return ingredientMultipliersByItem; }
    public Map<String, Float> getIngredientMultipliersByTag() { return ingredientMultipliersByTag; }
    public Map<String, Float> getOutputMultipliersByMod() { return outputMultipliersByMod; }
    public Map<String, Float> getOutputMultipliersByItem() { return outputMultipliersByItem; }
    public Map<String, Float> getOutputMultipliersByTag() { return outputMultipliersByTag; }
    public Map<String, Float> getRecipeMultipliersByType() { return recipeMultipliersByType; }
    public Map<String, Float> getRecipeMultipliersByRecipe() { return recipeMultipliersByRecipe; }
    public List<ContainerSettings> getContainers() { return containers; }

    // general setters
    public void setDebug(boolean debug) { this.debug = debug; }
    public void setEnableSounds(boolean enableSounds) { this.enableSounds = enableSounds; }
    public void setDefaultCraftingSound(String defaultCraftingSound) { this.defaultCraftingSound = defaultCraftingSound; }
    public void setDefaultFinishSound(String defaultFinishSound) { this.defaultFinishSound = defaultFinishSound; }
    public void setDefaultPenaltySound(String defaultPenaltySound) { this.defaultPenaltySound = defaultPenaltySound; }
    public void setDefaultSuccessSound(String defaultSuccessSound) { this.defaultSuccessSound = defaultSuccessSound; }
    public void setGlobalTimeMultiplier(float globalTimeMultiplier) { this.globalTimeMultiplier = globalTimeMultiplier; }

    // experience setters
    public void setExperienceMultiplier(float experienceMultiplier) { this.experienceMultiplier = experienceMultiplier; }
    public void setExperienceBaseSpeed(float experienceBaseSpeed) { this.experienceBaseSpeed = experienceBaseSpeed; }
    public void setExperienceSpeedPerLevel(float experienceSpeedPerLevel) { this.experienceSpeedPerLevel = experienceSpeedPerLevel; }
    public void setExperienceMaxLevelCap(int experienceMaxLevelCap) { this.experienceMaxLevelCap = experienceMaxLevelCap; }

    // decay setters
    public void setDecayEnabled(boolean decayEnabled) { this.decayEnabled = decayEnabled; }
    public void setDecayRate(float decayRate) { this.decayRate = decayRate; }

    // screen shake setters
    public void setScreenShakeEnabled(boolean screenShakeEnabled) { this.screenShakeEnabled = screenShakeEnabled; }
    public void setScreenShakeIntensity(float screenShakeIntensity) { this.screenShakeIntensity = screenShakeIntensity; }

    // hunger setters
    public void setHungerExhaustionCost(float hungerExhaustionCost) { this.hungerExhaustionCost = hungerExhaustionCost; }
    public void setHungerPenaltyEnabled(boolean hungerPenaltyEnabled) { this.hungerPenaltyEnabled = hungerPenaltyEnabled; }
    public void setHungerThreshold(int hungerThreshold) { this.hungerThreshold = hungerThreshold; }
    public void setHungerPenaltyMultiplier(float hungerPenaltyMultiplier) { this.hungerPenaltyMultiplier = hungerPenaltyMultiplier; }

    // minigame setters
    public void setMinigameEnabled(boolean minigameEnabled) { this.minigameEnabled = minigameEnabled; }
    public void setMinigameChance(float minigameChance) { this.minigameChance = minigameChance; }
    public void setMinigameWindowWidth(float minigameWindowWidth) { this.minigameWindowWidth = minigameWindowWidth; }
    public void setMinigamePenaltyPercent(float minigamePenaltyPercent) { this.minigamePenaltyPercent = minigamePenaltyPercent; }
    public void setMinigamePenaltyCancelsCraft(boolean minigamePenaltyCancelsCraft) { this.minigamePenaltyCancelsCraft = minigamePenaltyCancelsCraft; }

    // map setters
    public void setItemSounds(Map<String, String> itemSounds) { this.itemSounds = itemSounds; }
    public void setContainers(List<ContainerSettings> containers) { this.containers = containers; }

    public void validate() {
        globalTimeMultiplier = clamp(globalTimeMultiplier, 0.0F, 100.0F);
        experienceMultiplier = clamp(experienceMultiplier, 0.0F, 100.0F);
        experienceBaseSpeed = clamp(experienceBaseSpeed, 0.01F, 100.0F);
        experienceSpeedPerLevel = clamp(experienceSpeedPerLevel, 0.0F, 10.0F);
        experienceMaxLevelCap = Math.max(0, Math.min(experienceMaxLevelCap, 30000));
        decayRate = clamp(decayRate, 0.0F, 100.0F);
        screenShakeIntensity = clamp(screenShakeIntensity, 0.0F, 5.0F);
        hungerExhaustionCost = clamp(hungerExhaustionCost, 0.0F, 40.0F);
        hungerThreshold = Math.max(0, Math.min(hungerThreshold, 20));
        hungerPenaltyMultiplier = clamp(hungerPenaltyMultiplier, 0.0F, 10.0F);
        minigameChance = clamp(minigameChance, 0.0F, 1.0F);
        minigameWindowWidth = clamp(minigameWindowWidth, 0.01F, 0.5F);
        minigamePenaltyPercent = clamp(minigamePenaltyPercent, 0.0F, 1.0F);
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    public JsonObject serialize() {
        JsonObject json = new JsonObject();

        // general
        json.addProperty("debug", debug);
        json.addProperty("enable_sounds", enableSounds);
        json.addProperty("default_crafting_sound", defaultCraftingSound);
        json.addProperty("default_finish_sound", defaultFinishSound);
        json.addProperty("default_penalty_sound", defaultPenaltySound);
        json.addProperty("default_success_sound", defaultSuccessSound);
        json.addProperty("global_time_multiplier", globalTimeMultiplier);

        // experience
        JsonObject exp = new JsonObject();
        exp.addProperty("multiplier", experienceMultiplier);
        exp.addProperty("base_speed", experienceBaseSpeed);
        exp.addProperty("speed_per_level", experienceSpeedPerLevel);
        exp.addProperty("max_level_cap", experienceMaxLevelCap);
        json.add("experience", exp);

        // decay
        JsonObject decay = new JsonObject();
        decay.addProperty("enabled", decayEnabled);
        decay.addProperty("rate", decayRate);
        json.add("decay", decay);

        // screen shake
        JsonObject shake = new JsonObject();
        shake.addProperty("enabled", screenShakeEnabled);
        shake.addProperty("intensity", screenShakeIntensity);
        json.add("screen_shake", shake);

        // hunger
        JsonObject hunger = new JsonObject();
        hunger.addProperty("exhaustion_cost", hungerExhaustionCost);
        hunger.addProperty("penalty_enabled", hungerPenaltyEnabled);
        hunger.addProperty("threshold", hungerThreshold);
        hunger.addProperty("penalty_multiplier", hungerPenaltyMultiplier);
        json.add("hunger", hunger);

        // minigame
        JsonObject mini = new JsonObject();
        mini.addProperty("enabled", minigameEnabled);
        mini.addProperty("chance", minigameChance);
        mini.addProperty("window_width", minigameWindowWidth);
        mini.addProperty("penalty_percent", minigamePenaltyPercent);
        mini.addProperty("penalty_cancels_craft", minigamePenaltyCancelsCraft);
        json.add("minigame", mini);

        // item sounds
        json.add("item_sounds", serializeStringMap(itemSounds));

        // ingredient multipliers
        JsonObject ingr = new JsonObject();
        ingr.add("by_mod", serializeFloatMap(ingredientMultipliersByMod));
        ingr.add("by_item", serializeFloatMap(ingredientMultipliersByItem));
        ingr.add("by_tag", serializeFloatMap(ingredientMultipliersByTag));
        json.add("ingredient_multipliers", ingr);

        // output multipliers
        JsonObject outp = new JsonObject();
        outp.add("by_mod", serializeFloatMap(outputMultipliersByMod));
        outp.add("by_item", serializeFloatMap(outputMultipliersByItem));
        outp.add("by_tag", serializeFloatMap(outputMultipliersByTag));
        json.add("output_multipliers", outp);

        // recipe multipliers
        JsonObject recipe = new JsonObject();
        recipe.add("by_type", serializeFloatMap(recipeMultipliersByType));
        recipe.add("by_recipe", serializeFloatMap(recipeMultipliersByRecipe));
        json.add("recipe_multipliers", recipe);

        // containers
        JsonArray arr = new JsonArray();
        for (ContainerSettings c : containers) {
            arr.add(c.serialize());
        }
        json.add("containers", arr);

        return json;
    }

    public static PatienceConfig deserialize(JsonObject json) {
        PatienceConfig c = new PatienceConfig();

        // general
        c.debug = getBool(json, "debug", false);
        c.enableSounds = getBool(json, "enable_sounds", true);
        c.defaultCraftingSound = getString(json, "default_crafting_sound", "patience:crafting");
        c.defaultFinishSound = getString(json, "default_finish_sound", "patience:finish");
        c.defaultPenaltySound = getString(json, "default_penalty_sound", "patience:penalty");
        c.defaultSuccessSound = getString(json, "default_success_sound", "patience:success");
        c.globalTimeMultiplier = getFloat(json, "global_time_multiplier", 1.0F);

        // experience
        JsonObject exp = getObject(json, "experience");
        if (exp != null) {
            c.experienceMultiplier = getFloat(exp, "multiplier", 1.0F);
            c.experienceBaseSpeed = getFloat(exp, "base_speed", 1.0F);
            c.experienceSpeedPerLevel = getFloat(exp, "speed_per_level", 0.02F);
            c.experienceMaxLevelCap = getInt(exp, "max_level_cap", 200);
        }

        // decay
        JsonObject decay = getObject(json, "decay");
        if (decay != null) {
            c.decayEnabled = getBool(decay, "enabled", true);
            c.decayRate = getFloat(decay, "rate", 2.0F);
        }

        // screen shake
        JsonObject shake = getObject(json, "screen_shake");
        if (shake != null) {
            c.screenShakeEnabled = getBool(shake, "enabled", false);
            c.screenShakeIntensity = getFloat(shake, "intensity", 0.5F);
        }

        // hunger
        JsonObject hunger = getObject(json, "hunger");
        if (hunger != null) {
            c.hungerExhaustionCost = getFloat(hunger, "exhaustion_cost", 0.1F);
            c.hungerPenaltyEnabled = getBool(hunger, "penalty_enabled", true);
            c.hungerThreshold = getInt(hunger, "threshold", 6);
            c.hungerPenaltyMultiplier = getFloat(hunger, "penalty_multiplier", 0.5F);
        }

        // minigame
        JsonObject mini = getObject(json, "minigame");
        if (mini != null) {
            c.minigameEnabled = getBool(mini, "enabled", true);
            c.minigameChance = getFloat(mini, "chance", 0.5F);
            c.minigameWindowWidth = getFloat(mini, "window_width", 0.15F);
            c.minigamePenaltyPercent = getFloat(mini, "penalty_percent", 0.25F);
            c.minigamePenaltyCancelsCraft = getBool(mini, "penalty_cancels_craft", true);
        }

        // item sounds
        JsonObject sounds = getObject(json, "item_sounds");
        if (sounds != null) {
            c.itemSounds = deserializeStringMap(sounds);
        }

        // ingredient multipliers
        JsonObject ingr = getObject(json, "ingredient_multipliers");
        if (ingr != null) {
            c.ingredientMultipliersByMod = deserializeFloatMap(getObject(ingr, "by_mod"));
            c.ingredientMultipliersByItem = deserializeFloatMap(getObject(ingr, "by_item"));
            c.ingredientMultipliersByTag = deserializeFloatMap(getObject(ingr, "by_tag"));
        }

        // output multipliers
        JsonObject outp = getObject(json, "output_multipliers");
        if (outp != null) {
            c.outputMultipliersByMod = deserializeFloatMap(getObject(outp, "by_mod"));
            c.outputMultipliersByItem = deserializeFloatMap(getObject(outp, "by_item"));
            c.outputMultipliersByTag = deserializeFloatMap(getObject(outp, "by_tag"));
        }

        // recipe multipliers
        JsonObject recipe = getObject(json, "recipe_multipliers");
        if (recipe != null) {
            c.recipeMultipliersByType = deserializeFloatMap(getObject(recipe, "by_type"));
            c.recipeMultipliersByRecipe = deserializeFloatMap(getObject(recipe, "by_recipe"));
        }

        // containers
        if (json.has("containers") && json.get("containers").isJsonArray()) {
            JsonArray arr = json.getAsJsonArray("containers");
            c.containers = new ArrayList<>();
            for (JsonElement el : arr) {
                if (el.isJsonObject()) {
                    c.containers.add(ContainerSettings.deserialize(el.getAsJsonObject()));
                }
            }
        }

        c.validate();
        return c;
    }

    // helper methods for deserialization
    private static boolean getBool(JsonObject json, String key, boolean def) {
        return json.has(key) ? json.get(key).getAsBoolean() : def;
    }

    private static int getInt(JsonObject json, String key, int def) {
        return json.has(key) ? json.get(key).getAsInt() : def;
    }

    private static float getFloat(JsonObject json, String key, float def) {
        return json.has(key) ? json.get(key).getAsFloat() : def;
    }

    private static String getString(JsonObject json, String key, String def) {
        return json.has(key) ? json.get(key).getAsString() : def;
    }

    private static JsonObject getObject(JsonObject json, String key) {
        if (json != null && json.has(key) && json.get(key).isJsonObject()) {
            return json.getAsJsonObject(key);
        }
        return null;
    }

    private static JsonObject serializeFloatMap(Map<String, Float> map) {
        JsonObject json = new JsonObject();
        if (map != null) {
            for (Map.Entry<String, Float> entry : map.entrySet()) {
                json.addProperty(entry.getKey(), entry.getValue());
            }
        }
        return json;
    }

    private static JsonObject serializeStringMap(Map<String, String> map) {
        JsonObject json = new JsonObject();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                json.addProperty(entry.getKey(), entry.getValue());
            }
        }
        return json;
    }

    private static Map<String, Float> deserializeFloatMap(JsonObject json) {
        Map<String, Float> map = new HashMap<>();
        if (json != null) {
            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                map.put(entry.getKey(), entry.getValue().getAsFloat());
            }
        }
        return map;
    }

    private static Map<String, String> deserializeStringMap(JsonObject json) {
        Map<String, String> map = new HashMap<>();
        if (json != null) {
            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                map.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return map;
    }
}
