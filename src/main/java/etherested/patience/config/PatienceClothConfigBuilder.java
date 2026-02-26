package etherested.patience.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

// builds the Cloth Config screen for Patience;
// only classloaded when Cloth Config is present (guarded by PatienceConfigScreen.isAvailable())
class PatienceClothConfigBuilder {

    static Screen build(Screen parent) {
        PatienceConfig config = PatienceConfig.getInstance();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("patience.config.title"))
                .setSavingRunnable(ConfigManager::save);

        ConfigEntryBuilder entry = builder.entryBuilder();

        // general
        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("patience.config.category.general"));
        general.addEntry(entry.startBooleanToggle(Component.translatable("patience.config.debug"), config.isDebug())
                .setDefaultValue(false)
                .setSaveConsumer(config::setDebug)
                .build());
        general.addEntry(entry.startBooleanToggle(Component.translatable("patience.config.enable_sounds"), config.isSoundsEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(config::setEnableSounds)
                .build());
        general.addEntry(entry.startStrField(Component.translatable("patience.config.default_crafting_sound"), config.getDefaultCraftingSound())
                .setDefaultValue("patience:crafting")
                .setSaveConsumer(config::setDefaultCraftingSound)
                .build());
        general.addEntry(entry.startStrField(Component.translatable("patience.config.default_finish_sound"), config.getDefaultFinishSound())
                .setDefaultValue("patience:finish")
                .setSaveConsumer(config::setDefaultFinishSound)
                .build());
        general.addEntry(entry.startStrField(Component.translatable("patience.config.default_penalty_sound"), config.getDefaultPenaltySound())
                .setDefaultValue("patience:penalty")
                .setSaveConsumer(config::setDefaultPenaltySound)
                .build());
        general.addEntry(entry.startStrField(Component.translatable("patience.config.default_success_sound"), config.getDefaultSuccessSound())
                .setDefaultValue("patience:success")
                .setSaveConsumer(config::setDefaultSuccessSound)
                .build());
        general.addEntry(entry.startFloatField(Component.translatable("patience.config.global_time_multiplier"), config.getGlobalTimeMultiplier())
                .setDefaultValue(1.0F)
                .setMin(0.0F)
                .setMax(100.0F)
                .setSaveConsumer(config::setGlobalTimeMultiplier)
                .build());

        // experience
        ConfigCategory experience = builder.getOrCreateCategory(Component.translatable("patience.config.category.experience"));
        experience.addEntry(entry.startFloatField(Component.translatable("patience.config.experience_multiplier"), config.getExperienceMultiplier())
                .setDefaultValue(1.0F)
                .setMin(0.0F)
                .setMax(100.0F)
                .setSaveConsumer(config::setExperienceMultiplier)
                .build());
        experience.addEntry(entry.startFloatField(Component.translatable("patience.config.experience_base_speed"), config.getExperienceBaseSpeed())
                .setDefaultValue(1.0F)
                .setMin(0.01F)
                .setMax(100.0F)
                .setSaveConsumer(config::setExperienceBaseSpeed)
                .build());
        experience.addEntry(entry.startFloatField(Component.translatable("patience.config.experience_speed_per_level"), config.getExperienceSpeedPerLevel())
                .setDefaultValue(0.02F)
                .setMin(0.0F)
                .setMax(10.0F)
                .setSaveConsumer(config::setExperienceSpeedPerLevel)
                .build());
        experience.addEntry(entry.startIntField(Component.translatable("patience.config.experience_max_level_cap"), config.getExperienceMaxLevelCap())
                .setDefaultValue(200)
                .setMin(0)
                .setMax(30000)
                .setSaveConsumer(config::setExperienceMaxLevelCap)
                .build());

        // decay
        ConfigCategory decay = builder.getOrCreateCategory(Component.translatable("patience.config.category.decay"));
        decay.addEntry(entry.startBooleanToggle(Component.translatable("patience.config.decay_enabled"), config.isDecayEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(config::setDecayEnabled)
                .build());
        decay.addEntry(entry.startFloatField(Component.translatable("patience.config.decay_rate"), config.getDecayRate())
                .setDefaultValue(2.0F)
                .setMin(0.0F)
                .setMax(100.0F)
                .setSaveConsumer(config::setDecayRate)
                .build());

        // screen shake
        ConfigCategory screenShake = builder.getOrCreateCategory(Component.translatable("patience.config.category.screen_shake"));
        screenShake.addEntry(entry.startBooleanToggle(Component.translatable("patience.config.screen_shake_enabled"), config.isScreenShakeEnabled())
                .setDefaultValue(false)
                .setSaveConsumer(config::setScreenShakeEnabled)
                .build());
        screenShake.addEntry(entry.startFloatField(Component.translatable("patience.config.screen_shake_intensity"), config.getScreenShakeIntensity())
                .setDefaultValue(0.5F)
                .setMin(0.0F)
                .setMax(5.0F)
                .setSaveConsumer(config::setScreenShakeIntensity)
                .build());

        // hunger
        ConfigCategory hunger = builder.getOrCreateCategory(Component.translatable("patience.config.category.hunger"));
        hunger.addEntry(entry.startBooleanToggle(Component.translatable("patience.config.hunger_enabled"), config.isHungerEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(config::setHungerEnabled)
                .build());
        hunger.addEntry(entry.startFloatField(Component.translatable("patience.config.hunger_exhaustion_cost"), config.getHungerExhaustionCost())
                .setDefaultValue(0.1F)
                .setMin(0.0F)
                .setMax(40.0F)
                .setSaveConsumer(config::setHungerExhaustionCost)
                .build());
        hunger.addEntry(entry.startBooleanToggle(Component.translatable("patience.config.hunger_penalty_enabled"), config.isHungerPenaltyEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(config::setHungerPenaltyEnabled)
                .build());
        hunger.addEntry(entry.startIntField(Component.translatable("patience.config.hunger_threshold"), config.getHungerThreshold())
                .setDefaultValue(6)
                .setMin(0)
                .setMax(20)
                .setSaveConsumer(config::setHungerThreshold)
                .build());
        hunger.addEntry(entry.startFloatField(Component.translatable("patience.config.hunger_penalty_multiplier"), config.getHungerPenaltyMultiplier())
                .setDefaultValue(0.5F)
                .setMin(0.0F)
                .setMax(10.0F)
                .setSaveConsumer(config::setHungerPenaltyMultiplier)
                .build());

        // minigame
        ConfigCategory minigame = builder.getOrCreateCategory(Component.translatable("patience.config.category.minigame"));
        minigame.addEntry(entry.startBooleanToggle(Component.translatable("patience.config.minigame_enabled"), config.isMinigameEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(config::setMinigameEnabled)
                .build());
        minigame.addEntry(entry.startFloatField(Component.translatable("patience.config.minigame_chance"), config.getMinigameChance())
                .setDefaultValue(0.5F)
                .setMin(0.0F)
                .setMax(1.0F)
                .setSaveConsumer(config::setMinigameChance)
                .build());
        minigame.addEntry(entry.startFloatField(Component.translatable("patience.config.minigame_window_width"), config.getMinigameWindowWidth())
                .setDefaultValue(0.15F)
                .setMin(0.01F)
                .setMax(0.5F)
                .setSaveConsumer(config::setMinigameWindowWidth)
                .build());
        minigame.addEntry(entry.startFloatField(Component.translatable("patience.config.minigame_penalty_percent"), config.getMinigamePenaltyPercent())
                .setDefaultValue(0.25F)
                .setMin(0.0F)
                .setMax(1.0F)
                .setSaveConsumer(config::setMinigamePenaltyPercent)
                .build());
        minigame.addEntry(entry.startBooleanToggle(Component.translatable("patience.config.minigame_penalty_cancels_craft"), config.isMinigamePenaltyCancelsCraft())
                .setDefaultValue(true)
                .setSaveConsumer(config::setMinigamePenaltyCancelsCraft)
                .build());

        return builder.build();
    }

    private PatienceClothConfigBuilder() {}
}
