package etherested.patience.util;

// calculates effective crafting speed from attribute value, experience level, and config parameters
public final class SpeedCalculator {

    public static float getCraftingSpeed(double attributeValue, int experienceLevel, float baseSpeedConfig, float speedPerLevel, int maxLevel) {
        int effectiveLevel = Math.min(experienceLevel, maxLevel);
        return (float) (attributeValue * baseSpeedConfig) + (speedPerLevel * effectiveLevel);
    }

    private SpeedCalculator() {}
}
