package etherested.patience.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
//? if >=1.21 {
import net.minecraft.world.item.crafting.RecipeHolder;
//?} else {
/*import net.minecraft.world.item.crafting.Recipe;
*///?}
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import etherested.patience.api.CraftingContainer;
import etherested.patience.client.sound.CraftingSoundInstance;
import etherested.patience.client.sound.SoundRegistry;
import etherested.patience.config.ConfigManager;
import etherested.patience.config.ContainerSettings;
import etherested.patience.config.PatienceConfig;
import etherested.patience.network.CraftingExhaustionPayload;
import etherested.patience.platform.PlatformHelper;
import etherested.patience.registry.AttributeRegistry;
import etherested.patience.util.IngredientCountHelper;
import etherested.patience.util.SlotRange;
import etherested.patience.util.SpeedCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// core client-side crafting time logic;
// manages crafting state, progress, sounds, minigame, and completion
public final class CraftingHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CraftingHandler.class);
    private static final float BASE_CRAFT_TIME = 20.0F;
    private static final double MOVEMENT_THRESHOLD = 0.01;
    private static final double VELOCITY_THRESHOLD = 0.01;
    private static final int SOUND_REPLAY_INTERVAL = 8;
    private static final RandomSource RANDOM = RandomSource.create();
    private static final ContainerSettings DISABLED = ContainerSettings.builder().enabled(false).build();

    // maps Mojang-mapped vanilla screen class names to runtime class names;
    // Fabric remaps vanilla classes to intermediary at runtime, so hardcoded
    // Mojang names in the config won't match getClass().getName() in production;
    // Class references here get properly remapped by Loom during build
    private static final Map<String, String> SCREEN_CLASS_MAP = new HashMap<>();

    static {
        resolveScreen("net.minecraft.client.gui.screens.inventory.InventoryScreen", InventoryScreen.class);
        resolveScreen("net.minecraft.client.gui.screens.inventory.CraftingScreen", CraftingScreen.class);
        resolveScreen("net.minecraft.client.gui.screens.inventory.SmithingScreen", SmithingScreen.class);
        resolveScreen("net.minecraft.client.gui.screens.inventory.AnvilScreen", AnvilScreen.class);
        resolveScreen("net.minecraft.client.gui.screens.inventory.GrindstoneScreen", GrindstoneScreen.class);
        resolveScreen("net.minecraft.client.gui.screens.inventory.StonecutterScreen", StonecutterScreen.class);
        resolveScreen("net.minecraft.client.gui.screens.inventory.CartographyTableScreen", CartographyTableScreen.class);
        resolveScreen("net.minecraft.client.gui.screens.inventory.LoomScreen", LoomScreen.class);
    }

    private static void resolveScreen(String mojangName, Class<?> screenClass) {
        SCREEN_CLASS_MAP.put(mojangName, screenClass.getName());
    }

    private static String resolveScreenClass(String configuredName) {
        return SCREEN_CLASS_MAP.getOrDefault(configuredName, configuredName);
    }

    private static CraftingHandler instance;

    private AbstractContainerScreen<?> currentScreen;
    private ContainerSettings currentContainer;

    private boolean crafting;
    private boolean continuous;
    private float currentTime;
    private float totalTime;
    private int waitTicks;

    private double startX;
    private double startY;
    private double startZ;

    private List<ItemStack> lockedIngredients;

    private CraftingSoundInstance currentSound;
    private String cachedItemSound;
    private int soundTicks;
    private float currentShake;

    private boolean miniGameActive;
    private float miniGameStart;
    private float miniGameEnd;
    private int resultTimer;
    private int resultState;

    private CraftingHandler() {}

    public static void initialize() {
        instance = new CraftingHandler();
    }

    public static CraftingHandler getInstance() {
        if (instance == null) {
            initialize();
        }
        return instance;
    }

    private PatienceConfig getConfig() {
        return ConfigManager.getActiveConfig();
    }

    public boolean isCrafting() {
        return crafting;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public float getCurrentShake() {
        return currentShake;
    }

    public void setScreen(Object screen) {
        this.currentScreen = (AbstractContainerScreen<?>) screen;
    }

    public void clearScreen() {
        this.currentScreen = null;
        stopCrafting();
    }

    public int getResultState() {
        return resultState;
    }

    public boolean isMiniGameActive() {
        return miniGameActive;
    }

    public ContainerSettings getCurrentContainerSettings() {
        PatienceConfig config = getConfig();
        if (currentScreen == null || config == null) {
            return DISABLED;
        }

        String screenClass = currentScreen.getClass().getName();
        return config.getContainers().stream()
            .filter(c -> c.getScreenClass() != null && resolveScreenClass(c.getScreenClass()).equals(screenClass))
            .findFirst()
            .orElse(DISABLED);
    }

    public boolean handleSlotClick(Object screen, int slotId, boolean shiftHeld) {
        PatienceConfig config = getConfig();
        setScreen(screen);
        ContainerSettings container = getCurrentContainerSettings();

        if (config != null && config.isDebug()) {
            LOGGER.info("slot click: slot={}, screen={}, shift={}",
                slotId, currentScreen.getClass().getName(), shiftHeld);
        }

        if (isCreative()) {
            return false;
        }

        if (!container.isEnabled()) {
            return false;
        }

        if (slotId != container.getOutputSlot()) {
            return false;
        }

        if (isSlotEmpty(container.getOutputSlot())) {
            return true;
        }

        if (crafting) {
            if (miniGameActive && resultState != 1) {
                checkMiniGame();
            } else if (!miniGameActive) {
                applyEarlyClickPenalty();
            }
            return true;
        }

        if (isPlayerMoving()) {
            logDebug("player moving, blocking craft");
            return true;
        }

        if (!canAffordCraft()) {
            logDebug("cannot afford craft");
            return true;
        }

        if (!crafting) {
            startCrafting(container, shiftHeld);
        }

        return true;
    }

    public boolean shouldBlockSlotClick(Object screen, int slotId) {
        PatienceConfig config = getConfig();
        if (config == null) {
            return false;
        }

        if (isCreative()) {
            return false;
        }

        setScreen(screen);
        ContainerSettings container = getCurrentContainerSettings();
        return container.isEnabled() && slotId == container.getOutputSlot();
    }

    public float[] getOverlayColor() {
        if (resultState == 1) {
            return new float[]{0.0F, 1.0F, 0.0F, 1.0F};
        } else if (resultState == 2) {
            return new float[]{1.0F, 0.0F, 0.0F, 1.0F};
        } else if (miniGameActive) {
            float progress = currentTime / totalTime;
            if (progress >= miniGameStart && progress <= miniGameEnd) {
                return new float[]{1.0F, 1.0F, 0.0F, 1.0F};
            }
        }
        return new float[]{1.0F, 1.0F, 1.0F, 1.0F};
    }

    private void checkMiniGame() {
        PatienceConfig config = getConfig();
        float progress = currentTime / totalTime;
        if (progress >= miniGameStart && progress <= miniGameEnd) {
            resultState = 1;
            resultTimer = 20;
            currentTime = totalTime;
            stopSound();
            if (config.isSoundsEnabled()) {
                playSuccessSound(config.getDefaultSuccessSound());
            }
        } else {
            applyPenalty(config);
        }
    }

    // applies penalty for clicking the output slot early when no minigame zone is active
    private void applyEarlyClickPenalty() {
        applyPenalty(getConfig());
    }

    // reduces progress by the configured penalty amount;
    // stops the looping crafting sound and plays a one-shot penalty sound;
    // optionally cancels the craft if progress drops to zero
    private void applyPenalty(PatienceConfig config) {
        resultState = 2;
        resultTimer = 20;
        currentTime = Math.max(0, currentTime - (totalTime * config.getMinigamePenaltyPercent()));

        stopSound();
        if (config.isSoundsEnabled()) {
            playPenaltySound(config.getDefaultPenaltySound());
        }

        if (currentTime <= 0 && config.isMinigamePenaltyCancelsCraft()) {
            stopCrafting(true);
        }
    }

    private void setupMiniGame() {
        PatienceConfig config = getConfig();
        this.miniGameActive = false;
        this.resultState = 0;
        this.resultTimer = 0;

        if (config.isMinigameEnabled() && RANDOM.nextFloat() < config.getMinigameChance()) {
            this.miniGameActive = true;
            float width = config.getMinigameWindowWidth();
            float safeMaxStart = Math.max(0.2F, 0.9F - width);
            float range = safeMaxStart - 0.2F;

            if (range <= 0) {
                this.miniGameStart = 0.2F;
            } else {
                this.miniGameStart = 0.2F + RANDOM.nextFloat() * range;
            }

            this.miniGameEnd = this.miniGameStart + width;
        }
    }

    private void startCrafting(ContainerSettings container, boolean continuous) {
        PatienceConfig config = getConfig();
        this.currentContainer = container;
        this.continuous = continuous;
        this.crafting = true;

        this.lockedIngredients = getIngredientSnapshot(container.getIngredientSlots());

        float newTotalTime = calculateCraftTime(container);
        if (!config.isDecayEnabled() || currentTime > newTotalTime) {
            this.currentTime = 0;
        }

        this.totalTime = newTotalTime;

        setupMiniGame();

        recordPosition();

        if (totalTime >= 10.0F && config.isSoundsEnabled()) {
            playCraftingSound(getEffectiveCraftingSound(container));
        }
    }

    private void stopCrafting() {
        stopCrafting(false);
    }

    private void stopCrafting(boolean forceReset) {
        PatienceConfig config = getConfig();
        this.crafting = false;
        this.continuous = false;
        this.currentContainer = null;
        this.currentShake = 0;
        this.miniGameActive = false;
        this.resultState = 0;
        this.resultTimer = 0;
        this.cachedItemSound = null;
        this.lockedIngredients = null;

        if (forceReset || config == null || !config.isDecayEnabled()) {
            this.currentTime = 0;
        }

        stopSound();
    }

    public void tick() {
        PatienceConfig config = getConfig();
        if (!hasPlayer() || config == null) {
            return;
        }

        if (isCreative()) {
            if (crafting) stopCrafting();
            return;
        }

        if (currentShake > 0) {
            currentShake -= 0.02F;
            if (currentShake < 0) currentShake = 0;
        }

        if (resultTimer > 0) {
            resultTimer--;
            if (resultTimer == 0) {
                resultState = 0;
            }
        }

        ContainerSettings container = getCurrentContainerSettings();

        if (!container.isEnabled()) {
            if (config.isDecayEnabled() && currentTime > 0) {
                decayProgress();
            } else {
                currentTime = 0;
            }
            return;
        }

        if (crafting) {
            tickSound();
            tickCrafting(container);
        } else if (config.isDecayEnabled() && currentTime > 0) {
            decayProgress();
        }
    }

    private void decayProgress() {
        PatienceConfig config = getConfig();
        currentTime -= config.getDecayRate();
        currentShake = 0;
        if (currentTime <= 0) {
            currentTime = 0;
            stopCrafting();
        }
    }

    private void tickCrafting(ContainerSettings container) {
        PatienceConfig config = getConfig();
        if (config.isDecayEnabled()) {
            if (isPlayerMoving()) {
                decayProgress();
                stopSound();
                recordPosition();
                return;
            } else {
                if (currentSound == null && resultState != 2 && config.isSoundsEnabled() && totalTime >= 10.0F) {
                    playCraftingSound(getEffectiveCraftingSound(container));
                }
                recordPosition();
            }
        } else {
            if (hasPlayerMoved()) {
                logDebug("player moved, stopping");
                stopCrafting();
                return;
            }
            if (currentSound == null && resultState != 2 && config.isSoundsEnabled() && totalTime >= 10.0F) {
                playCraftingSound(getEffectiveCraftingSound(container));
            }
        }

        if (!canAffordCraft()) {
            logDebug("can no longer afford, stopping");
            stopCrafting();
            return;
        }

        if (lockedIngredients != null) {
            List<ItemStack> currentIngredients = getIngredientSnapshot(container.getIngredientSlots());
            if (!areIngredientsEqual(lockedIngredients, currentIngredients)) {
                logDebug("ingredients changed during craft, cancelling");
                stopCrafting(true);
                return;
            }
        }

        int outputSlot = container.getOutputSlot();

        if (isSlotEmpty(outputSlot)) {
            if (++waitTicks > 5) {
                waitTicks = 0;
                stopCrafting(true);
            }
            return;
        }

        if (resultState == 2) {
            return;
        }

        if (currentTime < totalTime) {
            double attributeValue = 1.0;
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                attributeValue = player.getAttributeValue(AttributeRegistry.craftingSpeed());
            }

            float speed = SpeedCalculator.getCraftingSpeed(
                attributeValue,
                getPlayerLevel(),
                config.getExperienceBaseSpeed(),
                config.getExperienceSpeedPerLevel(),
                config.getExperienceMaxLevelCap()
            );

            float hungerMult = 1.0F;
            if (config.isHungerPenaltyEnabled() && player != null) {
                if (player.getFoodData().getFoodLevel() <= config.getHungerThreshold()) {
                    hungerMult = config.getHungerPenaltyMultiplier();
                }
            }

            currentTime += speed * config.getExperienceMultiplier() * hungerMult;
        } else {
            completeCraft(container);
        }
    }

    private void completeCraft(ContainerSettings container) {
        PatienceConfig config = getConfig();
        if (config.getHungerExhaustionCost() > 0) {
            PlatformHelper.sendToServer(new CraftingExhaustionPayload(config.getHungerExhaustionCost()));
        }

        if (config.isSoundsEnabled()) {
            playFinishSound(getEffectiveFinishSound(container));
        }

        SlotRange slots = container.getIngredientSlots();
        Object oldItems = getSlotItems(slots);

        ((CraftingContainer) currentScreen).patience$completeCraft(
            currentScreen.getMenu().getSlot(container.getOutputSlot()),
            container.getOutputSlot()
        );

        if (continuous) {
            Object newItems = getSlotItems(slots);

            if (!oldItems.equals(newItems) || !canAffordCraft()) {
                stopCrafting(true);
            } else {
                waitTicks = 0;
                currentTime = 0;

                this.resultState = 0;
                this.resultTimer = 0;

                startCrafting(container, true);
            }
        } else {
            stopCrafting(true);
        }
    }

    private float calculateCraftTime(ContainerSettings container) {
        PatienceConfig config = getConfig();
        float globalMult = config.getGlobalTimeMultiplier();
        float containerMult = container.getTimeMultiplier();

        String mode = container.getIngredientMode();
        if (mode == null) {
            mode = "slot";
        }

        int customIngredientCount = 1;
        if ("custom".equals(mode)) {
            customIngredientCount = IngredientCountHelper.getCustomIngredientCount(currentScreen.getMenu());
        }

        float ingredientTime = 0.0F;
        for (int slot : container.getIngredientSlots()) {
            if (isSlotEmpty(slot)) continue;

            ItemStack stack = currentScreen.getMenu().getSlot(slot).getItem();
            if (stack.isEmpty()) continue;

            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            String modId = id.getNamespace();
            String itemId = id.toString();

            float modMult = config.getIngredientMultipliersByMod().getOrDefault(modId, 1.0F);
            float itemMult = config.getIngredientMultipliersByItem().getOrDefault(itemId, 1.0F);
            float tagMult = getTagMultiplier(stack, config.getIngredientMultipliersByTag());

            float itemContribution = modMult * itemMult * tagMult;

            switch (mode) {
                case "stack":
                    ingredientTime += itemContribution * stack.getCount();
                    break;
                case "custom":
                    ingredientTime += itemContribution * customIngredientCount;
                    break;
                default:
                    ingredientTime += itemContribution;
                    break;
            }
        }

        float outputMult = 1.0F;
        ItemStack outputStack = currentScreen.getMenu().getSlot(container.getOutputSlot()).getItem();
        if (!outputStack.isEmpty()) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(outputStack.getItem());
            String modId = id.getNamespace();
            String itemId = id.toString();

            float modMult = config.getOutputMultipliersByMod().getOrDefault(modId, 1.0F);
            float itemMult = config.getOutputMultipliersByItem().getOrDefault(itemId, 1.0F);
            float tagMult = getTagMultiplier(outputStack, config.getOutputMultipliersByTag());

            outputMult = modMult * itemMult * tagMult;
        }

        float recipeMult = getRecipeMultiplier(container);

        return BASE_CRAFT_TIME * ingredientTime * outputMult * recipeMult * containerMult * globalMult;
    }

    private ResourceLocation getAutomaticRecipeType() {
        if (currentScreen == null) return BuiltInRegistries.RECIPE_TYPE.getKey(RecipeType.CRAFTING);
        AbstractContainerMenu menu = currentScreen.getMenu();

        if (menu instanceof CraftingMenu || menu instanceof InventoryMenu) return BuiltInRegistries.RECIPE_TYPE.getKey(RecipeType.CRAFTING);
        if (menu instanceof StonecutterMenu) return BuiltInRegistries.RECIPE_TYPE.getKey(RecipeType.STONECUTTING);
        if (menu instanceof SmithingMenu) return BuiltInRegistries.RECIPE_TYPE.getKey(RecipeType.SMITHING);
        if (menu instanceof AbstractFurnaceMenu) return BuiltInRegistries.RECIPE_TYPE.getKey(RecipeType.SMELTING);
        return BuiltInRegistries.RECIPE_TYPE.getKey(RecipeType.CRAFTING);
    }

    private float getRecipeMultiplier(ContainerSettings container) {
        PatienceConfig config = getConfig();
        if (currentScreen == null) return 1.0F;

        if (config.getRecipeMultipliersByType().isEmpty() && config.getRecipeMultipliersByRecipe().isEmpty()) return 1.0F;

        String recipeTypeKey = container.getRecipeType();
        if (recipeTypeKey == null || recipeTypeKey.isEmpty()) {
            recipeTypeKey = getAutomaticRecipeType().toString();
        }

        float typeMult = config.getRecipeMultipliersByType().getOrDefault(recipeTypeKey, 1.0F);

        ItemStack outputStack = currentScreen.getMenu().getSlot(container.getOutputSlot()).getItem();
        if (outputStack.isEmpty()) return typeMult;

        if (!config.getRecipeMultipliersByRecipe().isEmpty()) {
            try {
                if (Minecraft.getInstance().level != null) {
                    //? if >=1.21 {
                    ResourceLocation typeId = ResourceLocation.parse(recipeTypeKey);
                    //?} else {
                    /*ResourceLocation typeId = new ResourceLocation(recipeTypeKey);
                    *///?}
                    Optional<RecipeType<?>> typeOpt = BuiltInRegistries.RECIPE_TYPE.getOptional(typeId);

                    if (typeOpt.isPresent()) {
                        var recipeManager = Minecraft.getInstance().level.getRecipeManager();

                        //? if >=1.21 {
                        @SuppressWarnings("unchecked")
                        List<RecipeHolder<?>> recipes = (List<RecipeHolder<?>>) (List<?>) recipeManager.getAllRecipesFor((RecipeType) typeOpt.get());

                        for (RecipeHolder<?> holder : recipes) {
                            if (ItemStack.isSameItem(holder.value().getResultItem(Minecraft.getInstance().level.registryAccess()), outputStack)) {
                                String recipeId = holder.id().toString();
                                if (config.getRecipeMultipliersByRecipe().containsKey(recipeId)) {
                                    return typeMult * config.getRecipeMultipliersByRecipe().get(recipeId);
                                }
                            }
                        }
                        //?} else {
                        /*@SuppressWarnings("unchecked")
                        List<Recipe<?>> recipes = (List<Recipe<?>>) (List<?>) recipeManager.getAllRecipesFor((RecipeType) typeOpt.get());

                        for (Recipe<?> recipe : recipes) {
                            if (ItemStack.isSameItem(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()), outputStack)) {
                                // recipe ID lookup not available without RecipeHolder in 1.20.1;
                                // recipe-by-ID multiplier is skipped for this version
                            }
                        }
                        *///?}
                    }
                }
            } catch (Exception ignored) {
            }
        }

        return typeMult;
    }

    private float getTagMultiplier(ItemStack stack, Map<String, Float> tagMap) {
        float mult = 1.0F;
        if (tagMap == null || tagMap.isEmpty()) return mult;

        for (var tag : stack.getTags().toList()) {
            String key = "#" + tag.location().toString();
            mult *= tagMap.getOrDefault(key, 1.0F);
        }
        return mult;
    }

    private String getEffectiveCraftingSound(ContainerSettings container) {
        PatienceConfig config = getConfig();
        if (currentScreen != null) {
            ItemStack output = currentScreen.getMenu().getSlot(container.getOutputSlot()).getItem();
            if (!output.isEmpty()) {
                String itemSound = getItemSpecificSound(output);
                if (itemSound != null && !itemSound.isEmpty()) {
                    cachedItemSound = itemSound;
                    return itemSound;
                }
                cachedItemSound = null;
            }
        }

        if (cachedItemSound != null) {
            return cachedItemSound;
        }

        String sound = container.getCraftingSound();
        return (sound != null && !sound.isEmpty()) ? sound : config.getDefaultCraftingSound();
    }

    private String getItemSpecificSound(ItemStack stack) {
        PatienceConfig config = getConfig();
        if (config.getItemSounds() == null) return null;

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String itemSound = config.getItemSounds().get(itemId.toString());
        if (itemSound != null && !itemSound.isEmpty()) return itemSound;

        for (var tag : stack.getTags().toList()) {
            String tagSound = config.getItemSounds().get("#" + tag.location().toString());
            if (tagSound != null && !tagSound.isEmpty()) return tagSound;
        }

        return null;
    }

    private String getEffectiveFinishSound(ContainerSettings container) {
        PatienceConfig config = getConfig();
        String sound = container.getFinishSound();
        return (sound != null && !sound.isEmpty()) ? sound : config.getDefaultFinishSound();
    }

    private void playCraftingSound(String soundId) {
        stopSound();
        soundTicks = 0;
        playSound(soundId);
    }

    private void playSound(String soundId) {
        PatienceConfig config = getConfig();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (config.isScreenShakeEnabled()) {
                currentShake = config.getScreenShakeIntensity();
            }

            player.swing(InteractionHand.MAIN_HAND);

            if (soundId != null && !soundId.isEmpty()) {
                //? if >=1.21 {
                currentSound = new CraftingSoundInstance(ResourceLocation.parse(soundId));
                //?} else {
                /*currentSound = new CraftingSoundInstance(new ResourceLocation(soundId));
                *///?}
            } else {
                currentSound = new CraftingSoundInstance();
            }
            Minecraft.getInstance().getSoundManager().play(currentSound);
        }
    }

    private void stopSound() {
        if (currentSound != null) {
            currentSound.forceStop();
            currentSound = null;
        }
        soundTicks = 0;
    }

    private void tickSound() {
        if (currentSound != null && crafting) {
            if (++soundTicks >= SOUND_REPLAY_INTERVAL) {
                soundTicks = 0;
                if (!currentSound.isForceStopped()) {
                    currentSound.forceStop();
                }

                String soundId = getEffectiveCraftingSound(currentContainer);
                playSound(soundId);
            }
        }
    }

    private void playFinishSound(String soundId) {
        stopSound();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            SoundEvent sound;
            if (soundId != null && !soundId.isEmpty()) {
                //? if >=1.21 {
                sound = SoundEvent.createVariableRangeEvent(ResourceLocation.parse(soundId));
                //?} else {
                /*sound = SoundEvent.createVariableRangeEvent(new ResourceLocation(soundId));
                *///?}
            } else {
                sound = SoundRegistry.finish();
            }
            Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(sound, CraftingSoundInstance.randomizePitch(), 0.1F)
            );
        }
    }

    private void playPenaltySound(String soundId) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            SoundEvent sound;
            if (soundId != null && !soundId.isEmpty()) {
                //? if >=1.21 {
                sound = SoundEvent.createVariableRangeEvent(ResourceLocation.parse(soundId));
                //?} else {
                /*sound = SoundEvent.createVariableRangeEvent(new ResourceLocation(soundId));
                *///?}
            } else {
                sound = SoundRegistry.penalty();
            }
            Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(sound, CraftingSoundInstance.randomizePitch(), 0.1F)
            );
        }
    }

    private void playSuccessSound(String soundId) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            SoundEvent sound;
            if (soundId != null && !soundId.isEmpty()) {
                //? if >=1.21 {
                sound = SoundEvent.createVariableRangeEvent(ResourceLocation.parse(soundId));
                //?} else {
                /*sound = SoundEvent.createVariableRangeEvent(new ResourceLocation(soundId));
                *///?}
            } else {
                sound = SoundRegistry.success();
            }
            Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(sound, CraftingSoundInstance.randomizePitch(), 0.1F)
            );
        }
    }

    private boolean hasPlayer() {
        return Minecraft.getInstance().player != null;
    }

    private int getPlayerLevel() {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null ? player.experienceLevel : 0;
    }

    private boolean isCreative() {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && player.getAbilities().instabuild;
    }

    private void recordPosition() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            startX = player.getX();
            startY = player.getY();
            startZ = player.getZ();
        }
    }

    private boolean hasPlayerMoved() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return false;

        double dx = Math.abs(player.getX() - startX);
        double dy = Math.abs(player.getY() - startY);
        double dz = Math.abs(player.getZ() - startZ);

        return dx > MOVEMENT_THRESHOLD || dy > MOVEMENT_THRESHOLD || dz > MOVEMENT_THRESHOLD;
    }

    private boolean isPlayerMoving() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return false;

        Vec3 velocity = player.getDeltaMovement();
        double speed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        return speed > VELOCITY_THRESHOLD;
    }

    private boolean canAffordCraft() {
        if (currentScreen == null) return true;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return true;

        if (currentScreen instanceof AnvilScreen) {
            AnvilMenu menu = (AnvilMenu) currentScreen.getMenu();
            int cost = menu.getCost();

            if (cost <= 0) return true;
            return player.getAbilities().instabuild || player.experienceLevel >= cost;
        }

        return true;
    }

    private boolean isSlotEmpty(int slot) {
        if (currentScreen == null) return true;
        return currentScreen.getMenu().getSlot(slot).getItem().isEmpty();
    }

    private Object getSlotItems(SlotRange range) {
        List<Item> items = new ArrayList<>();
        if (currentScreen == null) return items;

        for (int slot : range) {
            items.add(currentScreen.getMenu().getSlot(slot).getItem().getItem());
        }
        return items;
    }

    private List<ItemStack> getIngredientSnapshot(SlotRange range) {
        List<ItemStack> snapshot = new ArrayList<>();
        if (currentScreen == null) return snapshot;

        for (int slot : range) {
            snapshot.add(currentScreen.getMenu().getSlot(slot).getItem().copy());
        }
        return snapshot;
    }

    private boolean areIngredientsEqual(List<ItemStack> original, List<ItemStack> current) {
        if (original.size() != current.size()) return false;

        for (int i = 0; i < original.size(); i++) {
            ItemStack o = original.get(i);
            ItemStack c = current.get(i);

            if (!ItemStack.matches(o, c)) {
                return false;
            }
        }
        return true;
    }

    private void logDebug(String message) {
        PatienceConfig config = getConfig();
        if (config != null && config.isDebug()) {
            LOGGER.info(message);
        }
    }
}
