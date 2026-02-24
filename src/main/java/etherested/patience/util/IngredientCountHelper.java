package etherested.patience.util;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

// retrieves custom ingredient counts from modded containers via reflection;
// currently supports Hexerei woodcutter
public final class IngredientCountHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientCountHelper.class);

    public static int getCustomIngredientCount(AbstractContainerMenu menu) {
        if (menu == null) {
            return 1;
        }

        String className = menu.getClass().getName();

        if (className.equals("net.joefoxe.hexerei.container.WoodcutterContainer")) {
            return getHexereiWoodcutterIngredientCount(menu);
        }

        return 1;
    }

    private static int getHexereiWoodcutterIngredientCount(AbstractContainerMenu menu) {
        try {
            Field indexField = menu.getClass().getDeclaredField("selectedRecipeIndex");
            indexField.setAccessible(true);
            DataSlot indexSlot = (DataSlot) indexField.get(menu);
            int selectedIndex = indexSlot.get();

            if (selectedIndex < 0) {
                return 1;
            }

            Field recipesField = menu.getClass().getDeclaredField("recipes");
            recipesField.setAccessible(true);
            List<?> recipes = (List<?>) recipesField.get(menu);

            if (recipes.isEmpty() || selectedIndex >= recipes.size()) {
                return 1;
            }

            //? if >=1.21 {
            Object recipeHolder = recipes.get(selectedIndex);
            Method valueMethod = recipeHolder.getClass().getMethod("value");
            Object recipe = valueMethod.invoke(recipeHolder);
            //?} else {
            /*Object recipe = recipes.get(selectedIndex);
            *///?}

            Field countField = recipe.getClass().getField("ingredientCount");
            return Math.max(1, countField.getInt(recipe));

        } catch (Exception e) {
            LOGGER.debug("failed to get custom ingredient count: {}", e.getMessage());
            return 1;
        }
    }

    private IngredientCountHelper() {}
}
