package etherested.patience.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import etherested.patience.platform.PlatformHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// loads, saves, and provides server-accessible config state;
// the activeConfig field is set after load and after config sync
public final class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);
    private static final String CONFIG_FILE = "patience.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static PatienceConfig activeConfig;

    private ConfigManager() {}

    public static Path getConfigPath() {
        return PlatformHelper.getConfigDir().resolve(CONFIG_FILE);
    }

    public static PatienceConfig getActiveConfig() {
        return activeConfig;
    }

    public static void setActiveConfig(PatienceConfig config) {
        activeConfig = config;
        PatienceConfig.setInstance(config);
    }

    public static PatienceConfig load() {
        Path configPath = getConfigPath();
        File configFile = configPath.toFile();

        if (!configFile.exists()) {
            createDefault();
        }

        try {
            byte[] bytes = Files.readAllBytes(configPath);
            JsonObject json = JsonParser.parseString(new String(bytes)).getAsJsonObject();
            PatienceConfig config = PatienceConfig.deserialize(json);
            updateWithNewContainers(config);
            activeConfig = config;
            PatienceConfig.setInstance(config);
            return config;
        } catch (Exception e) {
            LOGGER.error("failed to load config, using defaults", e);
            return null;
        }
    }

    public static void save() {
        save(PatienceConfig.getInstance());
    }

    public static void save(PatienceConfig config) {
        try {
            String json = GSON.toJson(config.serialize());
            Files.write(getConfigPath(), json.getBytes());
        } catch (Exception e) {
            LOGGER.error("failed to save config", e);
        }
    }

    private static void createDefault() {
        Path configPath = getConfigPath();
        File configDir = configPath.getParent().toFile();

        if (!configDir.exists() && !configDir.mkdirs()) {
            LOGGER.error("failed to create config directory");
            return;
        }

        PatienceConfig config = PatienceConfig.getInstance();
        config.setContainers(DefaultContainers.getAll());

        // example entries
        config.getItemSounds().put("minecraft:stick", "patience:crafting");

        config.getIngredientMultipliersByMod().put("minecraft", 1.0F);
        config.getIngredientMultipliersByItem().put("minecraft:stick", 1.0F);
        config.getIngredientMultipliersByTag().put("#minecraft:planks", 1.0F);

        config.getOutputMultipliersByMod().put("minecraft", 1.0F);
        config.getOutputMultipliersByItem().put("minecraft:stick", 1.0F);
        config.getOutputMultipliersByTag().put("#minecraft:planks", 1.0F);

        config.getRecipeMultipliersByType().put("minecraft:crafting", 1.0F);
        config.getRecipeMultipliersByRecipe().put("minecraft:stick", 1.0F);

        save(config);
    }

    private static void updateWithNewContainers(PatienceConfig config) {
        Set<String> existingScreens = config.getContainers().stream()
                .map(ContainerSettings::getScreenClass)
                .collect(Collectors.toSet());

        List<ContainerSettings> defaults = DefaultContainers.getAll();
        boolean updated = false;

        for (ContainerSettings defaultContainer : defaults) {
            if (!existingScreens.contains(defaultContainer.getScreenClass())) {
                LOGGER.info("adding new container: {}", defaultContainer.getScreenClass());
                config.getContainers().add(defaultContainer);
                updated = true;
            }
        }

        if (updated) {
            save(config);
        }
    }
}
