package nitro.service;

import java.util.HashSet;
import java.util.Set;
import nitro.data.Elixir;
import nitro.data.Ingredient;

/**
 * Defines the ElixirCraftService service.
 */
public class ElixirCraftService implements IElixirCraftService {
    private final IElixirDataService elixirDataService;

    /**
     * Constructs the ElixirCraftService.
     * @param service The ElixirDataService.
     */
    public ElixirCraftService(IElixirDataService service) {

        if (service == null) {
            throw new IllegalArgumentException("IElixirCraftService must not be null");
        }

        this.elixirDataService = service;
    }

    /**
     * Finds all the Elixirs that are craftable with a set of Ingredients.
     * Finds all the potential Elixirs that require a specific Ingredient.
     * Iterates through the potential Elixirs and see if there is a match
     * with the available Ingredients.
     * @param ingredients The set of Ingredients.
     * @return The set of Elixirs that can be crafted.
     */
    @Override
    public Set<Elixir> findCraftableIngredients(Set<String> ingredients) {
        Set<Elixir> potentialElixirs = new HashSet<>();
        Set<Elixir> craftableElixirs = new HashSet<>();

        for (String ingredient : ingredients) {
            potentialElixirs.addAll(elixirDataService.getElixirsByIngredientName(ingredient));
        }

        for (Elixir elixir : potentialElixirs) {
            if (elixir.getIngredients().size() > ingredients.size()) continue;

            if (userHasAllIngredientsForElixir(elixir, ingredients)) craftableElixirs.add(elixir);
        }

        return craftableElixirs;
    }

    /**
     * See if the user has all the Ingredients required to craft a specific Elixir.
     * @param elixir The Elixir to craft.
     * @param ingredientNames The set of Ingredient names.
     * @return True if the user has all the Ingredients to craft the Elixir.
     */
    private boolean userHasAllIngredientsForElixir(Elixir elixir, Set<String> ingredientNames) {
        if (elixir.getIngredients().isEmpty()) return false;

        for (Ingredient ingredient : elixir.getIngredients()) {
            if (!ingredientNames.contains(ingredient.getName())) return false;
        }

        return true;
    }
}
