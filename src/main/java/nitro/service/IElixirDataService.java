package nitro.service;

import java.util.List;
import nitro.data.Elixir;
import nitro.data.Ingredient;

/**
 * Defines a ElixirDataService interface.
 */
public interface IElixirDataService {

    /**
     * Get all the Elixirs.
     * @return The list of Elixirs.
     */
    List<Elixir> getElixirs();

    /**
     * Get all the elixirs that require a specific Ingredient.
     * @param ingredientName The Ingredient name.
     * @return The list of Elixirs.
     */
    List<Elixir> getElixirsByIngredientName(String ingredientName);

    /**
     * Get all the Ingredients.
     * @return The list of Ingredients.
     */
    List<Ingredient> getIngredients();

    /**
     * Search for an Ingredient by name.
     * @param name The name of the Ingredient.
     * @return The found Ingredient.
     */
    Ingredient getIngredientByName(String name);

    /**
     * Validates if the Ingredient exists.
     * @param ingredient The Ingredient name.
     * @return A boolean identifying id it exists or not.
     */
    boolean validateIngredientName(String ingredient);
}
